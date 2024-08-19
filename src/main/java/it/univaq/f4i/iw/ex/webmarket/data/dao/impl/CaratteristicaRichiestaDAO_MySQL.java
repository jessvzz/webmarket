package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import it.univaq.f4i.iw.ex.webmarket.data.dao.CaratteristicaDAO;
import it.univaq.f4i.iw.ex.webmarket.data.dao.CaratteristicaRichiestaDAO;
import it.univaq.f4i.iw.ex.webmarket.data.dao.RichiestaOrdineDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.CaratteristicaRichiestaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaratteristicaRichiestaDAO_MySQL extends DAO implements CaratteristicaRichiestaDAO {

    private PreparedStatement sCaratteristicaRichiestaByID, sCaratteristicheByRichiesta, sRichiesteByCaratteristica, iCaratteristicaRichiesta, uCaratteristicaRichiesta;

    public CaratteristicaRichiestaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sCaratteristicaRichiestaByID = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE ID = ?");
            sCaratteristicheByRichiesta = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE richiesta_id = ?");
            sRichiesteByCaratteristica = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE caratteristica_id = ?");
            iCaratteristicaRichiesta = connection.prepareStatement("INSERT INTO caratteristica_richiesta (richiesta_id, caratteristica_id, valore) VALUES(?,?,?)");
            uCaratteristicaRichiesta = connection.prepareStatement("UPDATE caratteristica_richiesta SET richiesta_id=?, caratteristica_id=?, valore=? WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            // Chiudiamo le PreparedStatement aperte
            if (sCaratteristicaRichiestaByID != null) {
                sCaratteristicaRichiestaByID.close();
            }
            if (sCaratteristicheByRichiesta != null) {
                sCaratteristicheByRichiesta.close();
            }

            if (sRichiesteByCaratteristica != null) {
                sRichiesteByCaratteristica.close();
            }

            if (iCaratteristicaRichiesta != null) {
                iCaratteristicaRichiesta.close();
            }
            if (uCaratteristicaRichiesta != null) {
                uCaratteristicaRichiesta.close();
            }
        } catch (SQLException ex) {
            throw new DataException("Error while closing resources in CaratteristicaRichiestaDAO_MySQL", ex);
        } finally {
            // Assicuriamoci di chiamare il destroy() della classe padre
            super.destroy();
        }
    }
    


    @Override
    public CaratteristicaRichiesta createCaratteristicaRichiesta() {
        return new CaratteristicaRichiestaProxy(getDataLayer());
    }

    //helper
    private CaratteristicaRichiestaProxy createCaratteristicaRichiesta(ResultSet rs) throws DataException {
        try{
            CaratteristicaRichiestaProxy a = (CaratteristicaRichiestaProxy) createCaratteristicaRichiesta();

            a.setKey(rs.getInt("ID"));
           
            // Ottenere RichiestaOrdineDAO
             RichiestaOrdineDAO richiestaOrdineDAO = (RichiestaOrdineDAO) dataLayer.getDAO(RichiestaOrdine.class);
             a.setRichiestaOrdine(richiestaOrdineDAO.getRichiestaOrdine(rs.getInt("richiesta_id")));

            // Ottenere CaratteristicaDAO
            //TODO: errore su setCaratteristica: The method setCaratteristica(Caratteristica) in the type CaratteristicaRichiestaProxy is not applicable for the arguments (CaratteristicaDAO)Java(67108979)
            CaratteristicaDAO caratteristicaDAO = (CaratteristicaDAO) dataLayer.getDAO(Caratteristica.class);
            // a.setCaratteristica(caratteristicaDAO.getCaratteristica(rs.getInt("caratteristica_id")));
           
            a.setValore(rs.getString("valore"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create CaratteristicaRichiesta from ResultSet", ex);
        }
        
    }


    //TODO: errore nell'has di dataLayer.getCache() e nel get di sotto: The method has(Class<C>, C) in the type DataCache is not applicable for the arguments (Class<CaratteristicaRichiesta>, int, int)Java(67108979)
  /* 
    @Override
    public CaratteristicaRichiesta getCaratteristicaRichiesta(int caratteristica_key, int richiesta_key)throws DataException {
        CaratteristicaRichiesta c= null;
        
        if (dataLayer.getCache().has(CaratteristicaRichiesta.class, caratteristica_key,richiesta_key)) {
            c = dataLayer.getCache().get(CaratteristicaRichiesta.class, caratteristica_key, richiesta_key);
        } else {
            try {
                sCaratteristicaRichiestaByID.setInt(1, caratteristica_key);
                sCaratteristicaRichiestaByID.setInt(2, richiesta_key);
                try (ResultSet rs= sCaratteristicaRichiestaByID.executeQuery()) {
                    if (rs.next()) {
                        c = createCaratteristicaRichiesta(rs);
                        dataLayer.getCache().add(CaratteristicaRichiesta.class, c);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load CaratteristicaRichiesta by caratteristica_key and richiesta_key", ex);
            }
        }
        return c;
    }
    */
    

     @Override
     public CaratteristicaRichiesta getCaratteristicaRichiesta(int cr_key) throws DataException {
         CaratteristicaRichiesta c = null;
          if (dataLayer.getCache().has(CaratteristicaRichiesta.class, cr_key)) {
             c = dataLayer.getCache().get(CaratteristicaRichiesta.class, cr_key);
          } else {
              try {
                  sCaratteristicaRichiestaByID.setInt(1, cr_key);
                  try (ResultSet rs = sCaratteristicaRichiestaByID.executeQuery()) {
                      if (rs.next()) {
                          c = createCaratteristicaRichiesta(rs);
                          dataLayer.getCache().add(CaratteristicaRichiesta.class, c);
                      }
                  }
              } catch (SQLException ex) {
                  throw new DataException("Unable to load CaratteristicaRichiesta by ID", ex);
              }
          }
          return c;
      }

    @Override
    public void storeCaratteristicaRichiesta(CaratteristicaRichiesta caratteristicaRichiesta) throws DataException {
        try {
            if (caratteristicaRichiesta.getKey() != null && caratteristicaRichiesta.getKey() > 0) {
                if (caratteristicaRichiesta instanceof DataItemProxy && !((DataItemProxy) caratteristicaRichiesta).isModified()) {
                    return;
                }
                uCaratteristicaRichiesta.setInt(1, caratteristicaRichiesta.getRichiestaOrdine().getKey());
                uCaratteristicaRichiesta.setInt(2, caratteristicaRichiesta.getCaratteristica().getKey());
                uCaratteristicaRichiesta.setString(3, caratteristicaRichiesta.getValore());
                uCaratteristicaRichiesta.setInt(4, caratteristicaRichiesta.getKey());
                uCaratteristicaRichiesta.executeUpdate();
            } else {
                iCaratteristicaRichiesta.setInt(1, caratteristicaRichiesta.getRichiestaOrdine().getKey());
                iCaratteristicaRichiesta.setInt(2, caratteristicaRichiesta.getCaratteristica().getKey());
                iCaratteristicaRichiesta.setString(3, caratteristicaRichiesta.getValore());
                if (iCaratteristicaRichiesta.executeUpdate() == 1) {
                    try (ResultSet keys = iCaratteristicaRichiesta.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            caratteristicaRichiesta.setKey(key);
                            dataLayer.getCache().add(CaratteristicaRichiesta.class, caratteristicaRichiesta);
                        }
                    }
                }
            }
            if (caratteristicaRichiesta instanceof DataItemProxy) {
                ((DataItemProxy) caratteristicaRichiesta).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store CaratteristicaRichiesta", ex);
        }
    }

    @Override
    public List<Caratteristica> getCaratteristicheByRichiesta(int richiesta_key) throws DataException {
        List<Caratteristica> caratteristiche = new ArrayList<>();
        try {
            sCaratteristicheByRichiesta.setInt(1, richiesta_key);
            try (ResultSet rs = sCaratteristicheByRichiesta.executeQuery()) {
                while (rs.next()) {
                    // Crea una nuova CaratteristicaRichiesta usando i dati del ResultSet
                    CaratteristicaRichiesta caratteristicaRichiesta = createCaratteristicaRichiesta(rs);
                    // Aggiungi la caratteristica alla lista
                    caratteristiche.add(caratteristicaRichiesta.getCaratteristica());
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Caratteristiche by Richiesta", ex);
        }
        return caratteristiche;
    }
    

    @Override
    public List<RichiestaOrdine> getRichiesteByCaratteristica(int caratteristica_key) throws DataException {
        List<RichiestaOrdine> richiesteOrdine = new ArrayList<>();
        try {
            sRichiesteByCaratteristica.setInt(1, caratteristica_key);
            try (ResultSet rs = sRichiesteByCaratteristica.executeQuery()) {
                while (rs.next()) {
                    // Crea una nuova CaratteristicaRichiesta usando i dati del ResultSet
                    CaratteristicaRichiesta caratteristicaRichiesta = createCaratteristicaRichiesta(rs);
                    // Aggiungi la richiesta alla lista
                    richiesteOrdine.add(caratteristicaRichiesta.getRichiestaOrdine());
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Richieste by Caratteristica", ex);
        }
        return richiesteOrdine;
    }
    



}
