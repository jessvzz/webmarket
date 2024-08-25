package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import it.univaq.f4i.iw.ex.webmarket.data.dao.CaratteristicaDAO;
import it.univaq.f4i.iw.ex.webmarket.data.dao.CategoriaDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.CaratteristicaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CaratteristicaDAO_MySQL extends DAO implements CaratteristicaDAO{


    private PreparedStatement sCaratteristicaByID, sCaratteristicheByRichiesta, sCaratteristiche, iCaratteristica, uCaratteristica, dCaratteristica, sCaratteristicaByCategoria;


    public CaratteristicaDAO_MySQL(DataLayer d) {
        super(d);
    }


        @Override
        public void init() throws DataException {
        try {
            super.init();

            sCaratteristicaByID = connection.prepareStatement("SELECT * FROM caratteristica WHERE ID = ?");
            sCaratteristicaByCategoria = connection.prepareStatement("SELECT * FROM caratteristica WHERE categoria_id=?");
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
    
         return new CaratteristicaProxy(getDataLayer()); 

    }

    private CaratteristicaProxy createCaratteristica(ResultSet rs) throws DataException {
        try {
            CaratteristicaProxy c = (CaratteristicaProxy) createCaratteristica();
            c.setKey(rs.getInt("ID"));
            c.setNome(rs.getString("nome"));
            // c.setCategoria(rs.getInt("categoria_id"));
            CategoriaDAO categoriaDAO = (CategoriaDAO) dataLayer.getDAO(CategoriaDAO.class);
            Categoria categoria = categoriaDAO.getCategoria(rs.getInt("categoria_id"));
            c.setCategoria(categoria);

            return c;
        } catch (SQLException ex) {
            throw new DataException("Unable to create caratteristica object from ResultSet", ex);
        }
    }

    @Override
    public List<Caratteristica> getCaratteristiche() throws DataException {
        List<Caratteristica> result = new ArrayList<>();
    try (ResultSet rs = sCaratteristiche.executeQuery()) {
        while (rs.next()) {
            result.add(createCaratteristica(rs));
        }
    } catch (SQLException ex) {
        throw new DataException("Unable to load all caratteristiche", ex);
    }
    return result;
    } 


     @Override
     public Caratteristica getCaratteristica(int caratteristica_key) throws DataException {
   
         Caratteristica c = null;
         if (dataLayer.getCache().has(Caratteristica.class, caratteristica_key)) {
             c = dataLayer.getCache().get(Caratteristica.class, caratteristica_key);
         } else {
             try {
                 sCaratteristicaByID.setInt(1, caratteristica_key);
                 try (ResultSet rs = sCaratteristicaByID.executeQuery()) {
                     if (rs.next()) {
                         c = createCaratteristica(rs);
                         dataLayer.getCache().add(Caratteristica.class, c);
                     }
                 }
             } catch (SQLException ex) {
                 throw new DataException("Unable to load caratteristica by ID", ex);
             }
         }
         return c;
   
    }

    @Override
    public List<Caratteristica> getCaratteristicheByRichiesta(int richiesta_key) throws DataException {
          List<Caratteristica> result = new ArrayList<>();
    try {
        sCaratteristicheByRichiesta.setInt(1, richiesta_key);
        try (ResultSet rs = sCaratteristicheByRichiesta.executeQuery()) {
            while (rs.next()) {
                result.add(createCaratteristica(rs));
            }
        }
    } catch (SQLException ex) {
        throw new DataException("Unable to load caratteristiche by richiesta ID", ex);
    }
    return result;
    }
    
    @Override
    public List<Caratteristica> getCaratteristicheByCategoria(int categoria) throws DataException {
        List<Caratteristica> result = new ArrayList<>();
    try {
        sCaratteristicheByRichiesta.setInt(1, categoria);
        try (ResultSet rs = sCaratteristicheByRichiesta.executeQuery()) {
            while (rs.next()) {
                result.add(createCaratteristica(rs));
            }
        }
    } catch (SQLException ex) {
        throw new DataException("Unable to load caratteristiche by categoria ID", ex);
    }
    return result;
    }


    @Override
    public void storeCaratteratica(Caratteristica caratteristica) throws DataException {
        try {
            if (caratteristica.getKey() != null && caratteristica.getKey() > 0) {
                // Update
                uCaratteristica.setString(1, caratteristica.getNome());
                uCaratteristica.setInt(2, caratteristica.getCategoria().getKey());
                uCaratteristica.executeUpdate();
            } else {
                // Insert
                    iCaratteristica.setString(1, caratteristica.getNome());
                    iCaratteristica.setInt(2, caratteristica.getCategoria().getKey());
                if (iCaratteristica.executeUpdate() == 1) {
                    try (ResultSet keys = iCaratteristica.getGeneratedKeys()) {
                        if (keys.next()) {
                            caratteristica.setKey(keys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store caratteristica", ex);
        }
    }

  
    @Override
    public void deleteCaratteristica(int caratteristica_key) throws DataException {
        try {
            dCaratteristica.setInt(1, caratteristica_key);
            dCaratteristica.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete caratteristica", ex);
        }
    }
}