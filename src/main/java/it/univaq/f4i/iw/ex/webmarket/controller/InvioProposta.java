package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.PropostaAcquistoImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
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

/**
 *
 * @author Giulia Di Flamminio & Gea Viozzi
 */
public class InvioProposta extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Invio proposta");

        //Recupero la key passata come parametro
        int richiesta_key = Integer.parseInt(request.getParameter("n"));
        
        //Recupero la richiesta usando la key
        RichiestaOrdine richiesta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().getRichiestaOrdine(richiesta_key);
        request.setAttribute("richiesta", richiesta);

        res.activate("invioproposta.ftl.html", request, response);
    }

    private void action_sendProposta(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        RichiestaOrdine richiesta = ((ApplicationDataLayer) request.getAttribute("datalayer")).getRichiestaOrdineDAO().getRichiestaOrdine(n);

        // Recupero i dati dal form
        String produttore = request.getParameter("produttore");
        String prodotto = request.getParameter("prodotto");
        String codiceProdotto = request.getParameter("codiceProdotto");
        float prezzo = Float.parseFloat(request.getParameter("prezzo"));
        String url = request.getParameter("url");
        String note;
        if (request.getParameter("note").isEmpty()) {
            note = null;
        } else {
            note = request.getParameter("note");
        }

        // Creo una nuova proposta
        PropostaAcquisto proposta = new PropostaAcquistoImpl();
        proposta.setProduttore(produttore);
        proposta.setProdotto(prodotto);
        proposta.setCodiceProdotto(codiceProdotto);
        proposta.setPrezzo(prezzo);
        proposta.setUrl(url);
        proposta.setNote(note);
        proposta.setStatoProposta(StatoProposta.IN_ATTESA);
        proposta.setMotivazione(null);
        proposta.setRichiestaOrdine(richiesta);

        // Salvo la proposta nel database
        ((ApplicationDataLayer) request.getAttribute("datalayer")).getPropostaAcquistoDAO().storePropostaAcquisto(proposta);

        // Recupero l'email dell'utente
        String email = richiesta.getUtente().getEmail();

        // Configuro le proprietà per l'invio dell'email
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

        String tipo = "PropostaRichiesta_";
        String text = "Gentile Utente, Le è stata inviata una proposta d'acquisto per la sua richiesta numero " + richiesta.getCodiceRichiesta() + ". In allegato trova i dettagli.\n\nCordiali Saluti,\nIl team di WebMarket";
                        
        String messaggio = "Dettagli dell'ordine effettuato per la richiesta numero: " + richiesta.getCodiceRichiesta() + "\n\n";
        String pdfFilePath = "PropostaRichiesta_" + proposta.getCodice() + ".pdf";
        System.out.println("PROPOSTA:" + proposta.getCodice());

        try {
            EmailSender.createPDF(tipo, messaggio, proposta);
            EmailSender.sendEmailWithAttachment(session, email, "Notifica Proposta", text, pdfFilePath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Reindirizzo alla pagina di dettaglio della proposta
        response.sendRedirect("detailproposta_tecnico?n=" + proposta.getKey());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        try {
            HttpSession session = SecurityHelpers.checkSession(request);
            int n = SecurityHelpers.checkNumeric(request.getParameter("n"));

            if (session == null) {
                response.sendRedirect("login");
                return;
            }

            String action = request.getParameter("action");
            if (action != null && action.equals("invioProposta")) {
                action_sendProposta(request, response, n);
            } else {
                action_default(request, response, n);
            }

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (DataException ex) {
            Logger.getLogger(InvioProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet per l'invio di una nuova proposta d'acquisto";
    }
}