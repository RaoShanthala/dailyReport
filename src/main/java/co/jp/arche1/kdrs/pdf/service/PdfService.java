package co.jp.arche1.kdrs.pdf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.pdf.lib.MailService;
import co.jp.arche1.kdrs.pdf.lib.PDFGenerator;

@Service
public class PdfService extends BaseService {

	final static Logger logger = LoggerFactory.getLogger(PdfService.class);

	@Autowired
	PDFGenerator pdfGenerator;

	@Autowired
    public MailService mails;

	public void createPDF() {

      pdfGenerator.PDFGenerate();

	}

	public void sendProfileChangeEmail(String toEmail) throws Exception {
        String mailContent;
        String mailsubject;

        // need system_id - to get system id specific fromEmail and mailSubjectPrefix
        String mailSubjectPrefix = null;
        String mailFrom = null;

        mailSubjectPrefix = "【KitaniMaintenance】";
        mailsubject = mailSubjectPrefix + "プロフィール情報更新のお知らせ";
        mailFrom = "shanthala@arche1.co.jp";

        mailContent = "<p><b>Sender E-Mail:</b> " + mailFrom + "</p>";
        mailContent += "<p>This is to inform that your profile has been changed </p>";

        mails.sendEmail(toEmail, mailFrom, mailsubject, mailContent, null);
    }

}
