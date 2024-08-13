package it.univaq.f4i.iw.ex.newspaper.data.dao.impl;


import it.univaq.f4i.iw.ex.newspaper.data.dao.UtenteDAO;
import it.univaq.f4i.iw.ex.newspaper.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.newspaper.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
public class ApplicationDataLayer extends DataLayer {

    public ApplicationDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }

    @Override
    public void init() throws DataException {
        registerDAO(Caratteristica.class, new CaratteristicaDAO_MySQL(this));

    }
    
     public UtenteDAO getUtenteDAO() {
        return (UtenteDAO) getDAO(Utente.class);
    }

}