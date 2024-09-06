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
 * @author Giulia Di Flamminio & Gea Viozzi
 */
public class DetailPropostaTecnico extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        PropostaAcquisto proposta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(n);
        request.setAttribute("proposta", proposta);

        request.setAttribute("page_title", "Detail proposta");
        res.activate("detailproposta_tecnico.ftl.html", request, response);
    }
    
  
    private void action_sendOrdine(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        PropostaAcquisto proposta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().getPropostaAcquisto(n);
        
        //cambio stato proposta
        proposta.setStatoProposta(StatoProposta.ORDINATO);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().storePropostaAcquisto(proposta);
        
        //cambio stato richiesta
        RichiestaOrdine richiesta = proposta.getRichiestaOrdine();
        richiesta.setStato(StatoRichiesta.ORDINATA);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().storeRichiestaOrdine(richiesta);
        
        //trovo email utente
        String email = richiesta.getUtente().getEmail();

        Ordine ordine = new OrdineImpl();
        ordine.setProposta(proposta);
        ordine.setStato(StatoOrdine.IN_ATTESA);
        ordine.setData(new Date(System.currentTimeMillis()));
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordine);
        
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
        String text = "Gentile Utente, la informiamo che è stato effettuato un ordine per la sua proposta numero " + proposta.getCodice() +"\n\n In allegato trova i dettagli del suo ordine.";
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
        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));

        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action != null && action.equals("inviaOrdine")) {
            action_sendOrdine(request, response, n);
        } else {
            action_default(request, response, n);
        }

    } catch (IOException | TemplateManagerException /* | DataException */ ex) {
        handleError(ex, request, response);
    }   catch (DataException ex) {
            Logger.getLogger(DetailPropostaTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    @Override
    public String getServletInfo() {
        return "Servlet per la visualizzazione di una proposta inviata";
    }

}