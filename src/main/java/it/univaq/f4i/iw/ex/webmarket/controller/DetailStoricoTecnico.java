package it.univaq.f4i.iw.ex.webmarket.controller;

import com.itextpdf.text.DocumentException;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.OrdineImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gea Viozzi
 */
public class DetailStoricoTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Detail storico");
        
        Ordine ordine = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);
        System.out.println("proposta: " + ordine.getProposta().getCodice());
        
        request.setAttribute("ordine", ordine);
        
        res.activate("detailstorico_tecnico.ftl.html", request, response);
    }

     private void action_sendOrdine(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        Ordine ordineVecchio = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);

        PropostaAcquisto proposta = ordineVecchio.getProposta();
        proposta.setStatoProposta(StatoProposta.ORDINATO);
        String email = proposta.getRichiestaOrdine().getUtente().getEmail();
        
        //Update stato vecchio ordine
        ordineVecchio.setStato(StatoOrdine.RIFIUTATO);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordineVecchio);

        
        //creazione nuovo ordine
        Ordine ordineNuovo = new OrdineImpl();
        ordineNuovo.setProposta(proposta);
        ordineNuovo.setStato(StatoOrdine.IN_ATTESA);
        ordineNuovo.setData(new Date(System.currentTimeMillis()));
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordineNuovo);


        //update proposta
        proposta.setStatoProposta(StatoProposta.ORDINATO);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().storePropostaAcquisto(proposta);

        //update richiesta
        RichiestaOrdine richiesta = proposta.getRichiestaOrdine();
        richiesta.setStato(StatoRichiesta.ORDINATA);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().storeRichiestaOrdine(richiesta);

        
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
        String text = "Gentile Utente, la informiamo che è stato effettuato un nuovo ordine per la sua proposta numero " + proposta.getCodice() +"\n\n In allegato trova i dettagli del suo ordine.";
        // genero PDF
        String tipo = "OrdineProposta_";
        String messaggio = "\n Dettagli dell'ordine effettuato per la proposta numero: "+ proposta.getCodice()+"\n\n";
        String pdfFilePath = "OrdineProposta_" + proposta.getCodice() + ".pdf";

        String codice = proposta.getCodice();

        try {
            EmailSender.createPDF(tipo, messaggio, proposta, codice);

           
            EmailSender.sendEmailWithAttachment(session, email, "Notifica Ordine", text, pdfFilePath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("storico_tecnico"); 
        

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
        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));
        
        String action = request.getParameter("action");
       if (action != null && action.equals("reinviaOrdine")) {
            action_sendOrdine(request, response, n);
        } else {
            action_default(request, response, n);
        }

    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(DetailStoricoTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
}


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Detail storico servlet";
    }// </editor-fold>

}