package co.jp.arche1.kdrs.pdf.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;

import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    public MailService mails;

    /**
     * sendmail wrapper
     * @param rcpt     String     RCPT TO
     * @param from     String     MAIL FROM
     * @param subject  String     mail subject, should be UTF-8
     * @param body     String     mail body, UTF-8
     * @param bcc      String     Bcc
     * @throws Exception
     */
    public void sendEmail(String rcpt, String from, String subject, String body, String bcc) throws Exception{

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        if (Objects.nonNull(rcpt)) {
            helper.setTo(rcpt);
        }
        if (Objects.nonNull(from)) {
            helper.setFrom(from);
        }
        if (Objects.nonNull(body)) {
            helper.setText(body, true); //true specifies that the content is html
        }
        if (Objects.nonNull(subject)) {
            helper.setSubject(subject);
        }
        if (Objects.nonNull(bcc)) {
            helper.addBcc(bcc);
        }

        mailSender.send(message);

    }

    // if you would like to get config, set arg.
    // public String applyTextTemplate(LifekarteConfig config, String prefix, String filename,
    // IContext context)
    /**
     * make text from thymeleaf-template-file
     * @param prefix   String     template relative dir from src/main/resources/templates/
     * @param filename String     template file name
     * @param context  Context
     * @return String
     */
  /*  public String applyTextTemplate(String prefix, String filename, IContext context) {
        ThymeTextTemplate tt = new ThymeTextTemplate();
        return tt.getText(prefix, filename, context);
        /*
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // String p = prefix;
        String p = "templates/" + prefix;
        // relative path from /WEB-INF/
        // from src/main/resources/

        //FileTemplateResolver resolver = new FileTemplateResolver();
        //String td = config.getTpldir();
        //String p = td + "/" + prefix;

        resolver.setPrefix( p.replaceAll("[/]+", "/") );
        resolver.setTemplateMode(TemplateMode.TEXT); // text mode
        // resolver.setSuffix(".txt");
        resolver.setCharacterEncoding("utf-8");
        resolver.setCacheable(false);

        TemplateEngine engine = new TemplateEngine();
        engine.addTemplateResolver(resolver);

        StringWriter writer = new StringWriter();
        engine.process(filename, context, writer);
        return writer.toString();
        */
  /*  }

    public void sendCancelEMail(Order order,  List<LineItem> lineall,  Salon  salon, String toEmail, String userType,String fromEmail, String subjectPrefix,
            String sysid) throws Exception{
        ThymeTextTemplate tt;
        // MailService mails;
        Map<String, Object> hh;
        IContext ccc;
        String t;
        String mailsubject;

        tt = new ThymeTextTemplate();
        // mails = new MailService();

        hh = new HashMap<String, Object>();


        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        Currency currency = Currency.getInstance(Locale.JAPAN);
        format.setCurrency(currency);

        hh.put("feeCod", format.format(order.getFee_cod()));
        hh.put("feeShip", format.format(order.getFee_shipping()));

        hh.put("ticketVal", order.getDiscount_influencer().intValue());

        DateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN);
        hh.put("issueDate", new SimpleDateFormat("yyyy'年'MM'月'dd'日'(E) HH'時'mm'分'", Locale.JAPAN)
                .format(sf1.parse(order.getIssue_date())));


        DateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        hh.put("date", new SimpleDateFormat("yyyy'年'MM'月'dd'日'(E)", Locale.JAPAN)
                        .format(sf2.parse(order.getUser_delivery_date())));
        hh.put("time", util.getDeliveryTimeString(order.getUser_delivery_time()));


        hh.put("order", order);
        hh.put("lines", lineall);

        if (order.getPayable_status().equals("cash_on_delivery")) {
            hh.put("payment", "代金引換");
        } else {
            hh.put("payment", "クレジットカード");
        }
        List<LineItem> lineprods = new ArrayList<LineItem>(); // for normal products
        List<LineItem> linegwmsg = new ArrayList<LineItem>(); // fo gift-wrapping, message-card
        LineItem linecod = null;
        LineItem lineship = null;
        Integer prods_net_sales = 0, prods_sales_tax = 0;
        Integer reduce_prods_net_sales = 0, reduce_prods_sales_tax = 0, reducedTaxRate=0;
        hh.put("reduceTaxRateItem", false);
        hh.put("normalTaxRateItem", false);
        for (int i = 0; i < lineall.size(); ++i) {
            LineItem o = lineall.get(i);
            if (o.getSku_type().equals("Product") && o.getReduce_tax_rate().equals("yes")) {
                hh.put("reduceTaxRateItem", true);
                String nmj = "※" + o.getSh_nmj();
                o.setSh_nmj(nmj);
                lineprods.add(o);
                reduce_prods_net_sales = prods_net_sales + o.getNet_sales();
                reduce_prods_sales_tax = prods_sales_tax + o.getSales_tax();
                reducedTaxRate =(int) Math.round(o.getSales_tax_rate());
            } else if (o.getSku_type().contentEquals("Product") && o.getReduce_tax_rate().equals("no")) {
                hh.put("normalTaxRateItem", true);
                lineprods.add(o);
                prods_net_sales = prods_net_sales + o.getNet_sales();
                prods_sales_tax = prods_sales_tax + o.getSales_tax();
            } else if (o.getSpecialitem_id() != null && o.getSpecialitem_id() > 1) {
                // gift-wrapping message-card
                linegwmsg.add(o);
                prods_net_sales = prods_net_sales + o.getNet_sales();
                prods_sales_tax = prods_sales_tax + o.getSales_tax();
            } else if (o.getSpecialitem_id() != null && o.getSpecialitem_id() == 1) {
                // cod
                linecod = o;
            } else if (o.getSku_type().equals("ShippingCost")) {
                lineship = o;
            }
            o.setPerItemPrice(format.format(o.getTotal() / o.getQuantity()));
            o.setTotalString(format.format(o.getTotal()));

        }
        hh.put("lineprods", lineprods);
        hh.put("linegwmsg", linegwmsg);
        hh.put("RTaxRate",  reducedTaxRate);
        hh.put("net_sales", format.format(prods_net_sales));
        hh.put("sales_tax", format.format(prods_sales_tax));
        hh.put("reduce_net_sales", format.format(reduce_prods_net_sales));
        hh.put("reduce_sales_tax", format.format(reduce_prods_sales_tax));
        hh.put("Total", format.format(order.getTotal()));
        hh.put("salon", salon);
        int msig = 0;
        if (Objects.nonNull(salon.getMail_signature())) {
            if (salon.getMail_signature().length() > 0) {
                msig = 1;
            }
        }
        if (msig > 0) {
            hh.put("mail_signature", salon.getMail_signature());
        } else {
            hh.put("mail_signature", config.getMailSignature());
        }
        // hh.put("mail_body_word01", ); set site-name
        hh.put("mail_body_word01", subjectPrefix);

        if (userType.equals("one_time_login"))
            hh.put("imloginuser", false); // set login or not. boolean
        else
            hh.put("imloginuser", true);

        ccc = new Context(Locale.getDefault(), hh);
        // t = tt.getText("std/mail/", "cart_thank_you.text.tpl", ccc);
        t = tt.getText("", util.fixTplPath(sysid, "mail/cancel/cancel.text.tpl"), ccc);

        mailsubject = subjectPrefix + " ご注文をキャンセルしました";

        mails.sendEmail(toEmail, fromEmail, mailsubject, t, null);

    }

    public void sendEMail(Order order, List<LineItem> lineall, Salon salon, String toEmail,
            String userType, String fromEmail, String subjectPrefix,
            String sysid)
    throws Exception {
        ThymeTextTemplate tt;
        // MailService mails;
        Map<String, Object> hh;
        IContext ccc;
        String t;
        String mailsubject;

        tt = new ThymeTextTemplate();
        // mails = new MailService();

        hh = new HashMap<String, Object>();


        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        Currency currency = Currency.getInstance(Locale.JAPAN);
        format.setCurrency(currency);

        hh.put("feeCod", format.format(order.getFee_cod()));
        hh.put("feeShip", format.format(order.getFee_shipping()));

        hh.put("ticketVal", order.getDiscount_influencer().intValue());

        DateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN);
        hh.put("issueDate", new SimpleDateFormat("yyyy'年'MM'月'dd'日'(E) HH'時'mm'分'", Locale.JAPAN)
                .format(sf1.parse(order.getIssue_date())));


        DateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        hh.put("date", new SimpleDateFormat("yyyy'年'MM'月'dd'日'(E)", Locale.JAPAN)
                        .format(sf2.parse(order.getUser_delivery_date())));
        hh.put("time", util.getDeliveryTimeString(order.getUser_delivery_time()));


        hh.put("order", order);
        hh.put("lines", lineall);

        if (order.getPayable_status().equals("cash_on_delivery")) {
            hh.put("payment", "代金引換");
        } else {
            hh.put("payment", "クレジットカード");
        }
        List<LineItem> lineprods = new ArrayList<LineItem>(); // for normal products
        List<LineItem> linegwmsg = new ArrayList<LineItem>(); // fo gift-wrapping, message-card
        LineItem linecod = null;
        LineItem lineship = null;
        Integer prods_net_sales = 0, prods_sales_tax = 0;
        Integer reduce_prods_net_sales = 0, reduce_prods_sales_tax = 0, reducedTaxRate=0;
        hh.put("reduceTaxRateItem", false);
        hh.put("normalTaxRateItem", false);
        for (int i = 0; i < lineall.size(); ++i) {
            LineItem o = lineall.get(i);
            if (o.getSku_type().equals("Product") && o.getReduce_tax_rate().equals("yes")) {
                hh.put("reduceTaxRateItem", true);
                String nmj = "※" + o.getSh_nmj();
                o.setSh_nmj(nmj);
                lineprods.add(o);
                reduce_prods_net_sales = prods_net_sales + o.getNet_sales();
                reduce_prods_sales_tax = prods_sales_tax + o.getSales_tax();
                reducedTaxRate =(int) Math.round(o.getSales_tax_rate());
            } else if (o.getSku_type().contentEquals("Product") && o.getReduce_tax_rate().equals("no")) {
                hh.put("normalTaxRateItem", true);
                lineprods.add(o);
                prods_net_sales = prods_net_sales + o.getNet_sales();
                prods_sales_tax = prods_sales_tax + o.getSales_tax();
            } else if (o.getSpecialitem_id() != null && o.getSpecialitem_id() > 1) {
                // gift-wrapping message-card
                linegwmsg.add(o);
                prods_net_sales = prods_net_sales + o.getNet_sales();
                prods_sales_tax = prods_sales_tax + o.getSales_tax();
            } else if (o.getSpecialitem_id() != null && o.getSpecialitem_id() == 1) {
                // cod
                linecod = o;
            } else if (o.getSku_type().equals("ShippingCost")) {
                lineship = o;
            }
            o.setPerItemPrice(format.format(o.getTotal() / o.getQuantity()));
            o.setTotalString(format.format(o.getTotal()));

        }
        hh.put("lineprods", lineprods);
        hh.put("linegwmsg", linegwmsg);
        hh.put("RTaxRate",  reducedTaxRate);
        hh.put("net_sales", format.format(prods_net_sales));
        hh.put("sales_tax", format.format(prods_sales_tax));
        hh.put("reduce_net_sales", format.format(reduce_prods_net_sales));
        hh.put("reduce_sales_tax", format.format(reduce_prods_sales_tax));
        hh.put("Total", format.format(order.getTotal()));
        hh.put("salon", salon);
        int msig = 0;
        if (Objects.nonNull(salon.getMail_signature())) {
            if (salon.getMail_signature().length() > 0) {
                msig = 1;
            }
        }
        if (msig > 0) {
            hh.put("mail_signature", salon.getMail_signature());
        } else {
            hh.put("mail_signature", config.getMailSignature());
        }
        // hh.put("mail_body_word01", ); set site-name
        hh.put("mail_body_word01", subjectPrefix);

        if (userType.equals("one_time_login"))
            hh.put("imloginuser", false); // set login or not. boolean
        else
            hh.put("imloginuser", true);

        ccc = new Context(Locale.getDefault(), hh);
        // t = tt.getText("std/mail/", "cart_thank_you.text.tpl", ccc);
        t = tt.getText("", util.fixTplPath(sysid, "mail/cart_thank_you.text.tpl"), ccc);

        mailsubject = subjectPrefix + "ご注文ありがとうございます";

        mails.sendEmail(toEmail, fromEmail, mailsubject, t, null);
    }*/
}
