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
public class AggiungiCategoria extends BaseController{
     private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            request.setAttribute("categorie", ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getAllCategorie());

                
            request.setAttribute("page_title", "aggiungi categoria");
                
            TemplateResult res = new TemplateResult(getServletContext());
              
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("aggiungi_categoria.ftl.html", request, response);
          
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
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

        String action = request.getParameter("action");
        if (action != null && action.equals("createCategory")) {
            action_createCategory(request, response);
        } else {
            action_default(request, response);
        } 
        } catch (IOException | TemplateManagerException | DataException ex) {
        handleError(ex, request, response);
    }
    }
    
    private void action_createCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataException, TemplateManagerException {
        String name = request.getParameter("category-name");
        int parent = Integer.parseInt(request.getParameter("parent-category"));
        
        Categoria categoria = new CategoriaImpl();
        categoria.setNome(name);
        categoria.setPadre(parent);

        ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().storeCategoria(categoria);

        int categoriaId = categoria.getKey();

        String redirectUrl = "categoria?n=" + categoriaId;
        response.sendRedirect(redirectUrl);
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
