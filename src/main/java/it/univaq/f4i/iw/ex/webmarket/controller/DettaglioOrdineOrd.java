package it.univaq.f4i.iw.ex.webmarket.controller;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.DocumentException;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;

public class DettaglioOrdineOrd extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response,int user) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Dettaglio ordine ordinante");
        int ordineId = Integer.parseInt(request.getParameter("n"));
        
        // Recupero l'ordine dal database utilizzando il DAO
        request.setAttribute("ordine", ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(ordineId));
        res.activate("dettaglio_ordine_ord.ftl.html", request, response);
    }


    
    private void action_accettaOrdine(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException,DataException {
        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));
       
        Ordine ordine = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);
        ordine.setStato(StatoOrdine.ACCETTATO);
        
        //cambio stato richiesta
        RichiestaOrdine richiesta = ordine.getProposta().getRichiestaOrdine();
        richiesta.setStato(StatoRichiesta.RISOLTA);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().storeRichiestaOrdine(richiesta);

        

        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordine);
        String email = ordine.getProposta().getRichiestaOrdine().getTecnico().getEmail();
        String username = ordine.getProposta().getRichiestaOrdine().getTecnico().getUsername();

        
        //gestione email
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.outlook.com"); 
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("webmarket.univaq@outlook.com", "geagiuliasamanta1");
            }
        });
        String text = "Ciao "+username+",\n La informiamo che l'ordine da lei inviato in data " + ordine.getData() +" è stato ACCETTATO.";
        // genero PDF
         String tipo = "Ordine_";
         String messaggio = "\n Dettagli dell'ordine effettuato per la proposta numero: "+ ordine.getProposta().getCodice()+"\n\n";
         String pdfFilePath = "Ordine_" + ordine.getProposta().getCodice() + ".pdf";
         String codice = ordine.getProposta().getCodice(); //Mi serve per la generazione del pdf

         try {
             EmailSender.createPDF(tipo, messaggio, ordine.getProposta(), codice);

           
             EmailSender.sendEmailWithAttachment(session, email, "Notifica Proposta", text, pdfFilePath);
         } catch (DocumentException e) {
             e.printStackTrace();
         }

        response.sendRedirect("ordini"); 
    }


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
    try {
        HttpSession session = SecurityHelpers.checkSession(request);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }
        // Recupero l'ID dell'utente dalla sessione
        int userId = (int) session.getAttribute("userid");


        
        String action = request.getParameter("action");
        if (action != null && action.equals("accettaOrdine")) {
            action_accettaOrdine(request, response);
        } else{
   
        action_default(request, response, userId);
        }

    } catch (IOException | TemplateManagerException  ex) {
        handleError(ex, request, response);
    }
    catch (DataException ex) {
        Logger.getLogger(Ordini.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Dettaglio ordine ordinante servlet";
    }// </editor-fold>

}