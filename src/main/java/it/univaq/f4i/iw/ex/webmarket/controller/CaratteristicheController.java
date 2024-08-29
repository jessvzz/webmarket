/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.CaratteristicaImpl;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicheController extends BaseController{
     private void action_categoria(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);
            if (categoria != null) {
                request.setAttribute("categoria", categoria);
                
                List<Caratteristica> caratteristiche = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCaratteristicaDAO().getCaratteristicheByCategoria(n);
                
                request.setAttribute("caratteristiche", caratteristiche);
                
                request.setAttribute("page_title", "Caratteristiche di "+categoria.getNome());
                
                TemplateResult res = new TemplateResult(getServletContext());
               
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("gestione_caratteristiche.ftl.html", request, response);
            } else {
                handleError("Unable to load category", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
     
   

     
    private void createCaratteristica(HttpServletRequest request, HttpServletResponse response, int n) 
        throws ServletException, IOException, DataException {
    String caratteristicaNome = request.getParameter("nuova-caratteristica");
    System.out.println("sono nel metodo");
    if (caratteristicaNome != null && !caratteristicaNome.isEmpty()) {
        System.out.println("sono nell'if");
        Caratteristica nuovaCaratteristica = new CaratteristicaImpl();
        nuovaCaratteristica.setNome(caratteristicaNome);

        Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer"))
                .getCategoriaDAO().getCategoria(n);
        nuovaCaratteristica.setCategoria(categoria);

        ((ApplicationDataLayer) request.getAttribute("datalayer"))
                .getCaratteristicaDAO().storeCaratteratica(nuovaCaratteristica);

        System.out.println("parametro nel create car: "+n);
        response.sendRedirect("gestisci_caratteristiche?n=" + n);
    } else {
        handleError("Invalid characteristic name", request, response);
    }
}
    
    private void deleteCaratteristica(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException, DataException {
        System.out.println("sono qui");
    String caratteristicaId = request.getParameter("id");

    if (caratteristicaId != null && !caratteristicaId.isEmpty()) {
        ((ApplicationDataLayer) request.getAttribute("datalayer"))
                .getCaratteristicaDAO().deleteCaratteristica(Integer.parseInt(caratteristicaId));

        response.sendRedirect("gestisci_caratteristiche?n=" + request.getParameter("n"));
    } else {
        handleError("Invalid characteristic ID", request, response);
    }
}
    
    
    private void handleDelete(HttpServletRequest request, HttpServletResponse response, Integer caratteristicaKey, int n) {
        try {
            ApplicationDataLayer dl = (ApplicationDataLayer) request.getAttribute("datalayer");
            Caratteristica caratteristica = dl.getCaratteristicaDAO().getCaratteristica(caratteristicaKey);
            dl.getCaratteristicaDAO().deleteCaratteristica(caratteristicaKey);
            response.sendRedirect("gestisci_caratteristiche?n=" + n);
            
        } catch (IOException | DataException ex) {
            handleError(ex, request, response);
        }
    }
    


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int n;
        try {
            n = SecurityHelpers.checkNumeric(request.getParameter("n"));
            Map<String, String[]> parameterMap = request.getParameterMap();

            String action = request.getParameter("action");
            
            if ("createCaratteristica".equals(action)) {
                createCaratteristica(request, response, n);
            } 
            else if ("Elimina".equals(action)) {

                handleDelete(request, response, Integer.parseInt(parameterMap.get("id")[0]), n);
            }else{
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
        return "Categoria servlet";
    }// </editor-fold>
}
