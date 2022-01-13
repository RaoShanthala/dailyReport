package co.jp.arche1.kdrs.pdf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.pdf.service.PdfService;

@RestController
public class PdfController {

	@Autowired
    PdfService pdfService;

	final static Logger logger = LoggerFactory.getLogger(PdfController.class);

	@RequestMapping(value = "/PDF/CreatePdf", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public void getPdf() throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		pdfService.createPDF();

	}

	@RequestMapping(value = "/PDF/SendMail", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public void semdMail() throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		pdfService.sendProfileChangeEmail("shanthala@arche1.co.jp");

	}

}
