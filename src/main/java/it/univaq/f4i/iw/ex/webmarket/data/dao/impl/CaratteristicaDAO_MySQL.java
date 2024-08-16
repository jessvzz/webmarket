package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import it.univaq.f4i.iw.ex.webmarket.data.dao.CaratteristicaDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.CaratteristicaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


public class CaratteristicaDAO_MySQL extends DAO implements CaratteristicaDAO{


    private PreparedStatement sCaratteristicaByID, sCaratteristicheByRichiesta, sCaratteristiche, iCaratteristica, uCaratteristica, dCaratteristica;


    public CaratteristicaDAO_MySQL(DataLayer d) {
        super(d);
    }


        @Override
        public void init() throws DataException {
        try {
            super.init();

            sCaratteristicaByID = connection.prepareStatement("SELECT * FROM caratteristica WHERE ID = ?");
            sCaratteristicheByRichiesta = connection.prepareStatement("SELECT c.* FROM caratteristica c JOIN caratteristica_richiesta cr ON c.ID = cr.caratteristica_id WHERE cr.richiesta_id = ?");
            sCaratteristiche = connection.prepareStatement("SELECT * FROM caratteristica");
            iCaratteristica = connection.prepareStatement("INSERT INTO caratteristica (nome, categoria_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            uCaratteristica = connection.prepareStatement("UPDATE caratteristica SET nome=?, categoria_id=? WHERE ID=?");
            dCaratteristica = connection.prepareStatement("DELETE FROM caratteristica WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing caratteristica data layer", ex);
  
        }
    }


    @Override
    public void destroy() throws DataException {
        try {
            sCaratteristicaByID.close();
            sCaratteristicheByRichiesta.close();
            sCaratteristiche.close();
            iCaratteristica.close();
            uCaratteristica.close();
            dCaratteristica.close();
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }



    @Override
    public Caratteristica createCaratteristica() {
         throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    
        // return new CaratteristicaProxy(getDataLayer()); //TODO: errore: The constructor CaratteristicaProxy(DataLayer) is undefinedJava(134217858)

    }

    private CaratteristicaProxy createCaratteristica(ResultSet rs) throws DataException {
        try {
            CaratteristicaProxy c = (CaratteristicaProxy) createCaratteristica();
            //c.setKey(rs.getInt("ID"));
            c.setNome(rs.getString("nome"));
            //  c.setCategoriaId(rs.getInt("categoria_id")); //TODO: mi da l'errore:The method setCategoriaId(int) is undefined for the type CaratteristicaProxyJava(67108964)
            return c;
        } catch (SQLException ex) {
            throw new DataException("Unable to create caratteristica object from ResultSet", ex);
        }
    }


    //TODO: il tipo di ritorno è Caratteristica o CaratteristicaDAO?
     @Override
     public CaratteristicaDAO getCaratteristica(int caratteristica_key) throws DataException {
     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
   
    //     Caratteristica c = null;
    //     if (dataLayer.getCache().has(Caratteristica.class, caratteristica_key)) {
    //         c = dataLayer.getCache().get(Caratteristica.class, caratteristica_key);
    //     } else {
    //         try {
    //             sCaratteristicaByID.setInt(1, caratteristica_key);
    //             try (ResultSet rs = sCaratteristicaByID.executeQuery()) {
    //                 if (rs.next()) {
    //                     c = createCaratteristica(rs);
    //                     dataLayer.getCache().add(Caratteristica.class, c);
    //                 }
    //             }
    //         } catch (SQLException ex) {
    //             throw new DataException("Unable to load caratteristica by ID", ex);
    //         }
    //     }
    //     return c;
   
    }

    @Override
    public List<Caratteristica> getCaratteristicheByRichiesta(int richiesta_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void storeCaratteratica(RichiestaOrdine RichiestaOrdine) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Caratteristica> getCaratteristiche() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteCaratteristica(int caratteristica_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}