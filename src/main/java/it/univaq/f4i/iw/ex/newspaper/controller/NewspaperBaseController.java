package it.univaq.f4i.iw.ex.newspaper.controller;

import it.univaq.f4i.iw.ex.newspaper.data.dao.impl.ApplicationDataLayer;
import it.univaq.f4i.iw.framework.controller.AbstractBaseController;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
public abstract class NewspaperBaseController extends AbstractBaseController {

    @Override
    protected DataLayer createDataLayer(DataSource ds) throws ServletException {
        try {
            return new ApplicationDataLayer(ds);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

}
