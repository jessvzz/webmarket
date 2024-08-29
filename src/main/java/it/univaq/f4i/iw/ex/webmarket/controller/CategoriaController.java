/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
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
public class CategoriaController extends BaseController{
     private void action_categoria(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);
            if (categoria != null) {
                request.setAttribute("categoria", categoria);
                Categoria padre = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(categoria.getPadre());
                if (padre != null) {
                    request.setAttribute("padre", padre);
                }
                List<Categoria> categorieFiglie = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategorieByPadre(n);
                    request.setAttribute("categorieFiglie", categorieFiglie);
                request.setAttribute("page_title", categoria.getNome());
                //verr� usato automaticamente il template di outline spcificato tra i context parameters
                //the outlne template specified through the context parameters will be added by the TemplateResult to the specified template
                TemplateResult res = new TemplateResult(getServletContext());
                //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
                //add to the template a wrapper object that allows to call the stripslashes function
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("categoria.ftl.html", request, response);
            } else {
                handleError("Unable to load category", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
     
     private void action_deleteCategory(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, DataException {
        try {
            Categoria categoria = ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().getCategoria(n);
            if (categoria != null) {
                ((ApplicationDataLayer) request.getAttribute("datalayer")).getCategoriaDAO().deleteCategoria(categoria);
                response.sendRedirect("gestionecategorie");
            } else {
                handleError("Unable to find category to delete", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int n;
        try {
            n = SecurityHelpers.checkNumeric(request.getParameter("n"));
            String action = request.getParameter("action");
            if ("deleteCategory".equals(action)) {
                action_deleteCategory(request, response, n);
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
        return "Categoria servlet";
    }// </editor-fold>
}
