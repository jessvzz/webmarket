/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailSender {

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
    
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress("webmarket.univaq@outlook.com", "WebMarket"));

	      msg.setReplyTo(InternetAddress.parse("webmarket.univaq@outlook.com", false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
        
        public static void createPDF_ordine(String messaggio, PropostaAcquisto proposta) throws FileNotFoundException, DocumentException{
           Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("OrdineProposta_" + proposta.getCodice() + ".pdf"));
            document.open();

            Font font = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Font bold = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK);


            Chunk greetingChunk = new Chunk(messaggio, font);
            document.add(greetingChunk);

            System.out.println("prezzo: "+proposta.getPrezzo());
            Paragraph details = new Paragraph();
            details.add(new Chunk("Codice Proposta: ", bold));
            details.add(new Chunk(proposta.getCodice() + "\n", font));
            details.add(new Chunk("Produttore: ", bold));
            details.add(new Chunk(proposta.getProduttore() + "\n", font));
            details.add(new Chunk("Prodotto: ", bold));
            details.add(new Chunk(proposta.getProdotto() + "\n", font));
            details.add(new Chunk("Codice Prodotto: ", bold));
            details.add(new Chunk(proposta.getCodiceProdotto() + "\n", font));
            details.add(new Chunk("Prezzo: ", bold));
            details.add(new Chunk(proposta.getPrezzo() + "\n", font));
            details.add(new Chunk("URL: ", bold));
            details.add(new Chunk(proposta.getUrl() + "\n", font));

            document.add(details);
            
            document.close();
            System.out.println("PDF generato con successo!");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        }

    public static void createPDF_proposta(String messaggio, RichiestaOrdine richiesta, PropostaAcquisto proposta) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("PropostaRichiesta_" + richiesta.getCodiceRichiesta() + ".pdf"));
            document.open();

            Font font = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Font bold = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK);


            Chunk greetingChunk = new Chunk(messaggio, font);
            document.add(greetingChunk);

            System.out.println("prezzo: "+proposta.getPrezzo());
            Paragraph details = new Paragraph();
            details.add(new Chunk("Codice Proposta: ", bold));
            details.add(new Chunk(proposta.getCodice() + "\n", font));
            details.add(new Chunk("Produttore: ", bold));
            details.add(new Chunk(proposta.getProduttore() + "\n", font));
            details.add(new Chunk("Prodotto: ", bold));
            details.add(new Chunk(proposta.getProdotto() + "\n", font));
            details.add(new Chunk("Codice Prodotto: ", bold));
            details.add(new Chunk(proposta.getCodiceProdotto() + "\n", font));
            details.add(new Chunk("Prezzo: ", bold));
            details.add(new Chunk(proposta.getPrezzo() + "\n", font));
            details.add(new Chunk("URL: ", bold));
            details.add(new Chunk(proposta.getUrl() + "\n", font));

            document.add(details);

            document.close();
            System.out.println("PDF generato con successo!");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
        
        public static void sendEmailWithAttachment(Session session, String toEmail, String subject, String body, String pdfFilePath) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("webmarket.univaq@outlook.com", "WebMarket"));

            msg.setReplyTo(InternetAddress.parse("webmarket.univaq@outlook.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body, "UTF-8");

            // Creazione della parte allegato
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(pdfFilePath));
            attachmentBodyPart.setFileName(MimeUtility.encodeText(new File(pdfFilePath).getName(), "UTF-8", null));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            msg.setContent(multipart);

            // Invio dell'email
            Transport.send(msg);

            System.out.println("Email con allegato PDF inviata con successo!");

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

}