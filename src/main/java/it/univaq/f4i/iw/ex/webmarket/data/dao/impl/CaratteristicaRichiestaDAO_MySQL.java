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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaratteristicaRichiestaDAO_MySQL extends DAO implements CaratteristicaRichiestaDAO {

    private PreparedStatement sCaratteristicaRichiestaByID, sCaratteristicheRichiestaByOrdine, iCaratteristicaRichiesta, uCaratteristicaRichiesta;

    public CaratteristicaRichiestaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sCaratteristicaRichiestaByID = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE ID = ?");
            sCaratteristicheRichiestaByOrdine = connection.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE richiesta_id = ?");
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
            if (sCaratteristicheRichiestaByOrdine != null) {
                sCaratteristicheRichiestaByOrdine.close();
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

    private CaratteristicaRichiestaProxy createCaratteristicaRichiesta(ResultSet rs) throws DataException {
        CaratteristicaRichiestaProxy a = (CaratteristicaRichiestaProxy) createCaratteristicaRichiesta();
      //TODO: Implementare il caricamento delle chiavi esterne, da fixare praticamente il fatto dell'id o del costruttore (condatalayer) per richiesta ordine e caratteristica.       
        // a.setKey(rs.getInt("ID"));
        // a.setRichiestaOrdine(/* carica RichiestaOrdine usando richiesta_id */);
        // a.setCaratteristica(/* carica Caratteristica usando caratteristica_id */);
        // a.setValore(rs.getString("valore"));

        //Alternativa di codice
//             RichiestaOrdineDAO richiestaOrdineDAO = new RichiestaOrdineDAO_MySQL();
// CaratteristicaDAO caratteristicaDAO = new CaratteristicaDAO_MySQL();

// int richiestaOrdineId = rs.getInt("richiesta_id");
// RichiestaOrdine richiestaOrdine = richiestaOrdineDAO.getRichiestaOrdineById(richiestaOrdineId);
// a.setRichiestaOrdine(richiestaOrdine);

// int caratteristicaId = rs.getInt("caratteristica_id");
// Caratteristica caratteristica = caratteristicaDAO.getCaratteristicaById(caratteristicaId);
// a.setCaratteristica(caratteristica);

// a.setValore(rs.getString("valore"));

        return a;
    }

    @Override
    public CaratteristicaRichiesta getCaratteristicaRichiesta(int caratteristica_key, int richiesta_key)throws DataException {
        CaratteristicaRichiesta c= null;
        /*
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
        }*/
        return c;
    }
    

    // @Override
    // public CaratteristicaRichiesta getCaratteristicaRichiesta(int id) throws DataException {
    //     CaratteristicaRichiesta c = null;
    //     if (dataLayer.getCache().has(CaratteristicaRichiesta.class, id)) {
    //         c = dataLayer.getCache().get(CaratteristicaRichiesta.class, id);
    //     } else {
    //         try {
    //             sCaratteristicaRichiestaByID.setInt(1, id);
    //             try (ResultSet rs = sCaratteristicaRichiestaByID.executeQuery()) {
    //                 if (rs.next()) {
    //                     c = createCaratteristicaRichiesta(rs);
    //                     dataLayer.getCache().add(CaratteristicaRichiesta.class, c);
    //                 }
    //             }
    //         } catch (SQLException ex) {
    //             throw new DataException("Unable to load CaratteristicaRichiesta by ID", ex);
    //         }
    //     }
    //     return c;
    // }

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCaratteristicheByRichiesta'");
    }

    @Override
    public List<RichiestaOrdine> getRichiesteByCaratteristica(int caratteristica_key) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRichiesteByCaratteristica'");
    }



}
