/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;


import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;

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
        
        // la creazione di questo pdf ha richiesto piu tempo di tutto il progetto
        public static void createPDF(String tipo, String messaggio, PropostaAcquisto proposta, String codice) throws FileNotFoundException, DocumentException{
           Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(tipo + codice + ".pdf"));
            document.open();
            
            //font che uso
            Font headerFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 24, BaseColor.BLACK);
            Font subHeaderFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK);
            Font font = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Font bold = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK);
            Font bluFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLUE);
            
            //header
            Paragraph header = new Paragraph("WebMarket Univaq", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            
            document.add(new Paragraph("\n"));
            
            //qui il messaggio
            Paragraph subHeader = new Paragraph(messaggio, subHeaderFont);
            subHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(subHeader);
            
            //divisore
            LineSeparator line = new LineSeparator();
            line.setOffset(-2);
            document.add(new Chunk(line));
            document.add(new Paragraph("\n"));
            
            // creo tabella per riepilogo con due colonne
            PdfPTable table = new PdfPTable(2); 
            table.setWidthPercentage(100); 
            table.setSpacingBefore(10f); 
            table.setSpacingAfter(10f); 

            table.addCell(createCell("Codice Proposta:", bold));
            table.addCell(createCell(codice, font));
            table.addCell(createCell("Produttore:", bold));
            table.addCell(createCell(proposta.getProduttore(), font));
            table.addCell(createCell("Prodotto:", bold));
            table.addCell(createCell(proposta.getProdotto(), font));
            table.addCell(createCell("Codice Prodotto:", bold));
            table.addCell(createCell(proposta.getCodiceProdotto(), font));
            table.addCell(createCell("Prezzo:", bold));
            table.addCell(createCell("€ " + proposta.getPrezzo(), font));
            

            document.add(table);
            
            document.add(new Paragraph("\n"));
            
            //link 
            Paragraph linkParagraph = new Paragraph();
            linkParagraph.add(new Chunk("Per visualizzare il sito web di riferimento ", font));
            Anchor link = new Anchor("clicca qui", bluFont);
            link.setReference(proposta.getUrl());
            linkParagraph.add(link);
            linkParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(linkParagraph);
            
            document.close();
            System.out.println("PDF generato con successo!");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        }
        
        private static PdfPCell createCell(String content, Font font) {
            PdfPCell cell = new PdfPCell(new Phrase(content, font));
            cell.setBorder(Rectangle.NO_BORDER); 
            return cell;
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