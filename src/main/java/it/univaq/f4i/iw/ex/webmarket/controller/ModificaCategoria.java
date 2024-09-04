/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.CategoriaImpl;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jessviozzi
 */
public class ModificaCategoria extends BaseController{
     private void action_default(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);

            request.setAttribute("categoria", categoria);
                
            request.setAttribute("page_title", "modifica categoria");
                
            TemplateResult res = new TemplateResult(getServletContext());
              
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("modifica_categoria.ftl.html", request, response);
          
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
     
    private void action_updateCategory(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, DataException, TemplateManagerException {
        String name = request.getParameter("category-name");
        
        System.out.println("da qui ho questo nome: "+name+", e questa n: "+n);
        
        Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);
        categoria.setNome(name);        


        ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().storeCategoria(categoria);

        request.setAttribute("success", "Categoria creata con successo");
              
        response.sendRedirect("categoria?n="+n);    
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int n;
        n = SecurityHelpers.checkNumeric(request.getParameter("n"));

        try {
            String action = request.getParameter("action");
            if (action != null && action.equals("updateCategory")) {
                action_updateCategory(request, response, n);
            } else{
            action_default(request, response, n);}
            
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
