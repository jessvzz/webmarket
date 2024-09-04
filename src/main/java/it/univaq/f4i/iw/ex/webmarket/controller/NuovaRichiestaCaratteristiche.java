/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.CaratteristicaRichiestaImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.RichiestaOrdineImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jessviozzi
 */
public class NuovaRichiestaCaratteristiche extends BaseController{
     private void action_categoria(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);
            if (categoria != null) {
                request.setAttribute("categoria", categoria);
                
                 List<Caratteristica> caratteristiche= ((ApplicationDataLayer) request.getAttribute("datalayer")).getCaratteristicaDAO().getCaratteristicheByCategoria(n);

                request.setAttribute("caratteristiche", caratteristiche);
                
                request.setAttribute("page_title", "Selezione Caratteristiche");
                //verr� usato automaticamente il template di outline spcificato tra i context parameters
                //the outlne template specified through the context parameters will be added by the TemplateResult to the specified template
                TemplateResult res = new TemplateResult(getServletContext());
                //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
                //add to the template a wrapper object that allows to call the stripslashes function
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("nuova_richiesta_caratteristiche.ftl.html", request, response);
            } else {
                handleError("Unable to load category", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
     
    private void send_request(HttpServletRequest request, HttpServletResponse response, int n) throws DataException, IOException{        
        RichiestaOrdine richiestaOrdine = new RichiestaOrdineImpl();  // Creo istanza della richiesta d'ordine
        
       // trovo user
        HttpSession session = SecurityHelpers.checkSession(request);

        int userId = (int) session.getAttribute("userid");
        Utente u = ((ApplicationDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(userId);
        richiestaOrdine.setUtente(u);
        richiestaOrdine.setStato(StatoRichiesta.IN_ATTESA);  

        Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);
        richiestaOrdine.setCategoria(categoria);

        richiestaOrdine.setData(new Date());
        
        String note = request.getParameter("note");
        if (!note.isEmpty()) {
            richiestaOrdine.setNote(note);
        } else {
            richiestaOrdine.setNote(null);
        }

        //creo nuova richiesta nel db
        ((ApplicationDataLayer) request.getAttribute("datalayer"))
            .getRichiestaOrdineDAO().storeRichiestaOrdine(richiestaOrdine);

        // recupero caratteristiche
        List<Caratteristica> caratteristiche = ((ApplicationDataLayer) request.getAttribute("datalayer"))
                .getCaratteristicaDAO().getCaratteristicheByCategoria(n);

        // associo caratteristiche alla richiesta d'ordine
        for (Caratteristica caratteristica : caratteristiche) {
            System.out.println("key: " + caratteristica.getKey());
            CaratteristicaRichiesta caratteristicaRichiesta = new CaratteristicaRichiestaImpl();
            caratteristicaRichiesta.setCaratteristica(caratteristica);
            caratteristicaRichiesta.setRichiestaOrdine(richiestaOrdine);

            // vedo se è indifferente
            String indifferente = request.getParameter(caratteristica.getKey() + "-indifferente");
            if (indifferente != null && indifferente.equals("on")) {
                caratteristicaRichiesta.setValore("indifferente");
            } else {
                // prendo valore
                String valore = request.getParameter("caratteristica" + caratteristica.getKey());
                System.out.println("valore: "+valore);
                if (valore != ""){
                    caratteristicaRichiesta.setValore(valore);
                } else{
                    caratteristicaRichiesta.setValore("indifferente");
                }
                
            }

            // salvo caratteristica nel db
            ((ApplicationDataLayer) request.getAttribute("datalayer"))
                .getCaratteristicaRichiestaDAO().storeCaratteristicaRichiesta(caratteristicaRichiesta);
            
        }
        response.sendRedirect("richieste_ordinante");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int n;
        try {
            String param =  request.getParameter("n");
            System.out.println("Parametro: " + param); 
            n = SecurityHelpers.checkNumeric(request.getParameter("n"));
            
            System.out.println("Parametro n: " + n); 

            
            String action = request.getParameter("action");
            if (action != null && action.equals("createRichiesta")) {
                send_request(request, response, n);
            } else {
                action_categoria(request, response, n);
            }

            
        } catch (NumberFormatException ex) {
            handleError("Invalid number specified", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (DataException ex) {
             Logger.getLogger(CategoriaController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Nuova Richiesta servlet";
    }// </editor-fold>
}
