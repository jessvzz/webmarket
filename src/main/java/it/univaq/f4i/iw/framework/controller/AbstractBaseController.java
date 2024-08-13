package it.univaq.f4i.iw.framework.controller;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
public abstract class AbstractBaseController extends HttpServlet {

    private DataSource ds;
    private Pattern protect;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    //creare la propria classe derivata da DataLayer
    //create your own datalayer derived class
    protected abstract DataLayer createDataLayer(DataSource ds) throws ServletException;

    //override per inizializzare altre informazioni da offrire a tutte le servlet
    //override to init other information to offer to all the servlets
    protected void initRequest(HttpServletRequest request, DataLayer dl) {
        String completeRequestURL = request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        request.setAttribute("thispageurl", completeRequestURL);
        request.setAttribute("datalayer", dl);
    }

    //override to enforce your policy and/or change the login url
    protected void accessCheckFailed(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        String completeRequestURL = request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        response.sendRedirect("login?referrer=" + URLEncoder.encode(completeRequestURL, "UTF-8"));
    }

    //override to provide your login information in the request
    protected void accessCheckSuccessful(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        HttpSession s = request.getSession(false);
        if (s != null) {
            Map<String, Object> li = new HashMap<>();
            request.setAttribute("logininfo", li);
            li.put("session-start-ts", s.getAttribute("session-start-ts"));
            li.put("username", s.getAttribute("username"));
            li.put("userid", s.getAttribute("userid"));
            li.put("ip", s.getAttribute("ip"));
        }
    }

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        //WARNING: never declare DB-related objects including references to Connection and Statement (as our data layer)
        //as class variables of a servlet. Since servlet instances are reused, concurrent requests may conflict on such
        //variables leading to unexpected results. To always have different connections and statements on a per-request
        //(i.e., per-thread) basis, declare them in the doGet, doPost etc. (or in methods called by them) and 
        //(possibly) pass such variables through the request.        
        try (DataLayer datalayer = createDataLayer(ds)) {
            datalayer.init();
            initRequest(request, datalayer);
            //questo blocco di controlli può essere usato in alternativa al SessionCheckFilter
            //e richiede la specifica degli url-pattern delle risorse protette come init-parameters della web application
            //this block can be used as an alternative to the SessionCheckFilter
            //and requires the specification of the protected resources url-patterns as web application init-parameters
            if (checkAccess(request, response)) {
                accessCheckSuccessful(request, response);
                processRequest(request, response);
            } else {
                accessCheckFailed(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
            handleError(ex, request, response);
        }
    }

    protected boolean checkAccess(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        HttpSession s = SecurityHelpers.checkSession(request);
        String uri = request.getRequestURI();
        //non ridirezioniamo verso la login se richiediamo risorse da non proteggere
        //do not redirect to login if we are requesting unprotected resources
        return !(s == null && protect != null && protect.matcher(uri).find());
    }

    protected void handleError(String message, HttpServletRequest request, HttpServletResponse response) {
        new FailureResult(getServletContext()).activate(message, request, response);
    }

    protected void handleError(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        new FailureResult(getServletContext()).activate(exception, request, response);
    }

    protected void handleError(HttpServletRequest request, HttpServletResponse response) {
        new FailureResult(getServletContext()).activate(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        //init protection pattern
        String p = config.getServletContext().getInitParameter("security.protect.patterns");
        if (p == null || p.isBlank()) {
            protect = null;
        } else {
            String[] split = p.split("\\s*,\\s*");
            protect = Pattern.compile(Arrays.stream(split).collect(Collectors.joining("$)|(?:", "(?:", "$)")));
        }

        //init data source
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/" + config.getServletContext().getInitParameter("data.source"));
        } catch (NamingException ex) {
            throw new ServletException(ex);
        }
    }

}
