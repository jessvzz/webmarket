package it.univaq.f4i.iw.framework.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.CaratteristicaDAO_MySQL;
import it.univaq.f4i.iw.ex.webmarket.data.dao.impl.RichiestaOrdineDAO_MySQL;
import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;

/**
 *
 * @author Giuseppe Della Penna
 */
public class DataLayer implements AutoCloseable {

    private final DataSource datasource;
    private Connection connection;
    private final Map<Class, DAO> daos;
    private final DataCache cache;

    public DataLayer(DataSource datasource) throws SQLException {
        super();
        this.datasource = datasource;
        this.connection = datasource.getConnection();
        this.daos = new HashMap<>();
        this.cache = new DataCache();
    }

    public void registerDAO(Class entityClass, DAO dao) throws DataException {
        daos.put(entityClass, dao);
        dao.init();
    }

    public DAO getDAO(Class entityClass) {
        return daos.get(entityClass);
    }

    public void init() throws DataException {
         registerDAO(RichiestaOrdine.class, new RichiestaOrdineDAO_MySQL(this));
         registerDAO(Caratteristica.class, new CaratteristicaDAO_MySQL(this));
         //Altri DAO
        // registerDAO(RichiestaOrdine.class, new RichiestaOrdineDAO_MySQL(this));
        //call registerDAO for your own DAOs
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ex) {
            //
        }
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public Connection getConnection() {
        return connection;
    }

    public DataCache getCache() {
        return cache;
    }

    //metodo dell'interfaccia AutoCloseable (permette di usare questa classe nei try-with-resources)
    //method from the Autocloseable interface (allows this class to be used in try-with-resources)
    @Override
    public void close() throws Exception {
        destroy();
    }
}
