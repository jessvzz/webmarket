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
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaratteristicaRichiestaDAO_MySQL extends DAO implements CaratteristicaRichiestaDAO {

    private PreparedStatement sCaratteristicaRichiestaByID, sCaratteristicheByRichiesta, sRichiesteByCaratteristica, iCaratteristicaRichiesta, uCaratteristicaRichiesta;

    /**
     * Costruttore della classe.
     * 
     * @param d il DataLayer da utilizzare
     */
    public CaratteristicaRichiestaDAO_MySQL(DataLayer d) {
        super(d);
    }

    /**
     * Inizializza le PreparedStatement.
     * 
     * @throws DataException se si verifica un errore durante l'inizializzazione
     */
    @Override
    public void init() throws DataException {
        try {
            super.init();
            sCaratteristicaRichiestaByID = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE ID = ?");
            sCaratteristicheByRichiesta = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE richiesta_id = ?");
            sRichiesteByCaratteristica = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE caratteristica_id = ?");
            iCaratteristicaRichiesta = connection.prepareStatement("INSERT INTO caratteristica_richiesta (richiesta_id, caratteristica_id, valore) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            uCaratteristicaRichiesta = connection.prepareStatement("UPDATE caratteristica_richiesta SET richiesta_id=?, caratteristica_id=?, valore=?, version=? WHERE ID=? AND version=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing data layer", ex);
        }
    }

    /**
     * Chiude le PreparedStatement.
     * 
     * @throws DataException se si verifica un errore durante la chiusura
     */
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
    
    /**
     * Crea una nuova istanza di CaratteristicaRichiesta.
     * 
     * @return una nuova istanza di CaratteristicaRichiestaProxy
     */
    @Override
    public CaratteristicaRichiesta createCaratteristicaRichiesta() {
        return new CaratteristicaRichiestaProxy(getDataLayer());
    }

    /**
     * Crea una CaratteristicaRichiestaProxy a partire da un ResultSet.
     * 
     * @param rs il ResultSet da cui creare la CaratteristicaRichiestaProxy
     * @return una nuova istanza di CaratteristicaRichiestaProxy
     * @throws DataException se si verifica un errore durante la creazione
     */
    private CaratteristicaRichiestaProxy createCaratteristicaRichiesta(ResultSet rs) throws DataException {
        try{
            CaratteristicaRichiestaProxy cr = (CaratteristicaRichiestaProxy) createCaratteristicaRichiesta();
            cr.setKey(rs.getInt("ID"));
             RichiestaOrdineDAO richiestaOrdineDAO = (RichiestaOrdineDAO) dataLayer.getDAO(RichiestaOrdine.class);
             cr.setRichiestaOrdine(richiestaOrdineDAO.getRichiestaOrdine(rs.getInt("richiesta_id")));
            CaratteristicaDAO caratteristicaDAO = (CaratteristicaDAO) dataLayer.getDAO(Caratteristica.class);
            cr.setCaratteristica(caratteristicaDAO.getCaratteristica(rs.getInt("caratteristica_id")));
            cr.setValore(rs.getString("valore"));
            cr.setVersion(rs.getLong("version"));
            return cr;
        } catch (SQLException ex) {
            throw new DataException("Unable to create CaratteristicaRichiesta from ResultSet", ex);
        }
        
    }

    /**
     * Recupera una caratteristica richiesta dato il suo ID.
     * 
     * @param cr_key l'ID della caratteristica richiesta
     * @return la caratteristica richiesta corrispondente all'ID
     * @throws DataException se si verifica un errore durante il recupero
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

    /**
     * Recupera le caratteristiche associate a una richiesta.
     * 
     * @param richiesta_key l'ID della richiesta
     * @return una lista di caratteristiche associate alla richiesta
     * @throws DataException se si verifica un errore durante il recupero
     */
    @Override
    public List<Caratteristica> getCaratteristicheByRichiesta(int richiesta_key) throws DataException {
        List<Caratteristica> caratteristiche = new ArrayList<>();
        try {
            sCaratteristicheByRichiesta.setInt(1, richiesta_key);
            try (ResultSet rs = sCaratteristicheByRichiesta.executeQuery()) {
                while (rs.next()) {

                    CaratteristicaRichiesta caratteristicaRichiesta = createCaratteristicaRichiesta(rs);

                    caratteristiche.add(caratteristicaRichiesta.getCaratteristica());
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Caratteristiche by Richiesta", ex);
        }
        return caratteristiche;
    }
    
    /**
     * Recupera le caratteristiche richieste associate a una richiesta.
     * 
     * @param richiesta_key l'ID della richiesta
     * @return una lista di caratteristiche richieste associate alla richiesta
     * @throws DataException se si verifica un errore durante il recupero
     */
    @Override
    public List<CaratteristicaRichiesta> getCaratteristicaRichiestaByRichiesta(int richiesta_key) throws DataException {
        List<CaratteristicaRichiesta> caratteristiche = new ArrayList<>();
        try {
            sCaratteristicheByRichiesta.setInt(1, richiesta_key);
            try (ResultSet rs = sCaratteristicheByRichiesta.executeQuery()) {
                while (rs.next()) {
                    CaratteristicaRichiesta caratteristicaRichiesta = createCaratteristicaRichiesta(rs);
                    caratteristiche.add(caratteristicaRichiesta);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Caratteristiche by Richiesta", ex);
        }
        return caratteristiche;
    }
    
    /**
     * Recupera le richieste associate a una caratteristica.
     * 
     * @param caratteristica_key l'ID della caratteristica
     * @return una lista di richieste associate alla caratteristica
     * @throws DataException se si verifica un errore durante il recupero
     */
    @Override
    public List<RichiestaOrdine> getRichiesteByCaratteristica(int caratteristica_key) throws DataException {
        List<RichiestaOrdine> richiesteOrdine = new ArrayList<>();
        try {
            sRichiesteByCaratteristica.setInt(1, caratteristica_key);
            try (ResultSet rs = sRichiesteByCaratteristica.executeQuery()) {
                while (rs.next()) {

                    CaratteristicaRichiesta caratteristicaRichiesta = createCaratteristicaRichiesta(rs);

                    richiesteOrdine.add(caratteristicaRichiesta.getRichiestaOrdine());
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Richieste by Caratteristica", ex);
        }
        return richiesteOrdine;
    }
    
    /**
     * Memorizza una caratteristica richiesta nel database.
     * 
     * @param caratteristicaRichiesta la caratteristica richiesta da memorizzare
     * @throws DataException se si verifica un errore durante la memorizzazione
     */
    @Override
    public void storeCaratteristicaRichiesta(CaratteristicaRichiesta caratteristicaRichiesta) throws DataException {
        try {
            if (caratteristicaRichiesta.getKey() != null && caratteristicaRichiesta.getKey() > 0) {
                if (caratteristicaRichiesta instanceof CaratteristicaRichiestaProxy && !((CaratteristicaRichiestaProxy) caratteristicaRichiesta).isModified()) {
                    return;
                }
                uCaratteristicaRichiesta.setInt(1, caratteristicaRichiesta.getRichiestaOrdine().getKey());
                uCaratteristicaRichiesta.setInt(2, caratteristicaRichiesta.getCaratteristica().getKey());
                uCaratteristicaRichiesta.setString(3, caratteristicaRichiesta.getValore());
                long oldVersion = caratteristicaRichiesta.getVersion();
                long versione = oldVersion + 1;
                uCaratteristicaRichiesta.setLong(4, versione);
                uCaratteristicaRichiesta.setInt(5, caratteristicaRichiesta.getKey());
                uCaratteristicaRichiesta.setLong(6, oldVersion);
                if(uCaratteristicaRichiesta.executeUpdate() == 0){
                    throw new OptimisticLockException(caratteristicaRichiesta);
                }else {
                    caratteristicaRichiesta.setVersion(versione);
                }
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


}