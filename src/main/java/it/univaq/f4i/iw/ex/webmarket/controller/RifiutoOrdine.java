package it.univaq.f4i.iw.ex.webmarket.controller;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RifiutoOrdine extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        Ordine ordine = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);

        request.setAttribute("ordine", ordine);
        
        request.setAttribute("page_title", "Motivazione rifiuto ordine");
        res.activate("rifiuto_ordine.ftl.html", request, response);
    }
    
    
    private void action_rifiutaOrdine(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        Ordine ordine = ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().getOrdine(n);

        String motivoRifiuto = request.getParameter("azione");
       
        if (motivoRifiuto == null || motivoRifiuto.trim().isEmpty()) {
           
            request.setAttribute("ordine", ordine);
            request.setAttribute("errore", "Devi selezionare una motivazione per il rifiuto dell'ordine!");
            action_default(request, response, n);
            return; 
        }

       
       if (motivoRifiuto.equals("RESPINTO_NON_CONFORME")) {
        ordine.setStato(StatoOrdine.RESPINTO_NON_CONFORME);
    } else if (motivoRifiuto.equals("RESPINTO_NON_FUNZIONANTE")) {
        ordine.setStato(StatoOrdine.RESPINTO_NON_FUNZIONANTE);
    }
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getOrdineDAO().storeOrdine(ordine);
        
        
        //cambio stato proposta
        PropostaAcquisto proposta = ordine.getProposta();
        proposta.setStatoProposta(StatoProposta.ACCETTATO);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().storePropostaAcquisto(proposta);
        
        //cambio stato richiesta
        RichiestaOrdine richiesta = ordine.getProposta().getRichiestaOrdine();
        richiesta.setStato(StatoRichiesta.IN_ATTESA);
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().storeRichiestaOrdine(richiesta);


       String email = richiesta.getTecnico().getEmail();
       String username = richiesta.getTecnico().getUsername();
        
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


            String text = "Ciao " + username + ",\n" +
            "La informiamo che l'ordine effettuato in data " + ordine.getData() + " è stato RIFIUTATO.\n" +
            "Motivo del rifiuto: " + motivoRifiuto.replace("_", " ").toLowerCase() + ".\n\n" +
            "La preghiamo gentilmente di procedere di conseguenza.\n\n" +
            "Saluti,\n" +
            "Il team WebMarket";

EmailSender.sendEmail(session, email, "Notifica Rifiuto Ordine", text);

response.sendRedirect("dettaglio_ordine_ord?n=" + n); 
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
    
    int n = SecurityHelpers.checkNumeric(request.getParameter("n"));

    String action = request.getParameter("action");
    if (action != null && action.equals("rifiutaOrdine")) {
        action_rifiutaOrdine(request, response, n);
    } else {
        action_default(request, response, n);
    }

} catch (IOException | TemplateManagerException | DataException ex) {
    handleError(ex, request, response);
} 
}

@Override
public String getServletInfo() {
    return "Rifiuto Ordine Servlet";
}
}