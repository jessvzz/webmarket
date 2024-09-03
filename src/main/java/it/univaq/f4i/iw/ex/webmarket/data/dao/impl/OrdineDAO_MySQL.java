package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;
import it.univaq.f4i.iw.ex.webmarket.data.dao.OrdineDAO;
import it.univaq.f4i.iw.ex.webmarket.data.dao.PropostaAcquistoDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.OrdineProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implementazione MySQL dell'interfaccia OrdineDAO
public class OrdineDAO_MySQL extends DAO implements OrdineDAO {
    // Query SQL precompilate
    private PreparedStatement sOrdineByID, sOrdiniByUtente, sOrdiniByTecnico, sAllOrdini, iOrdine, uOrdine, dOrdine, ordiniDaNotificare;

    public OrdineDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            // Inizializzazione delle prepared statements con le query SQL
            sOrdineByID = connection.prepareStatement("SELECT * FROM ordine WHERE ID = ?");
            sOrdiniByUtente = connection.prepareStatement("SELECT o.* FROM ordine o JOIN proposta_acquisto pa ON o.proposta_id = pa.ID JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID WHERE ro.utente = ?  ORDER BY CASE WHEN o.stato = 'IN_ATTESA' THEN 1 ELSE 2 END");
            sOrdiniByTecnico = connection.prepareStatement("SELECT o.* FROM ordine o JOIN proposta_acquisto pa ON o.proposta_id = pa.ID JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID WHERE ro.tecnico = ? ORDER BY CASE WHEN o.stato = 'RESPINTO_NON_CONFORME' OR o.stato = 'RESPINTO_NON_FUNZIONANTE' THEN 1 ELSE 2 END");
            sAllOrdini = connection.prepareStatement("SELECT * FROM ordine");
            iOrdine = connection.prepareStatement("INSERT INTO ordine (stato, proposta_id, data) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uOrdine = connection.prepareStatement("UPDATE ordine SET stato=?, proposta_id=? , data=? WHERE ID=?");
            dOrdine = connection.prepareStatement("DELETE FROM ordine WHERE ID=?");
            ordiniDaNotificare = connection.prepareStatement( "SELECT EXISTS( SELECT 1 FROM ordine o JOIN proposta_acquisto pa ON o.proposta_id = pa.ID JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID WHERE (o.stato = 'RESPINTO_NON_CONFORME' OR o.stato = 'RESPINTO_NON_FUNZIONANTE') AND ro.tecnico = ?) AS notifica_ordine;");

        } catch (SQLException ex) {
            throw new DataException("Error initializing ordine data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            // Chiusura delle prepared statements
            sOrdineByID.close();
            sOrdiniByUtente.close();
            sAllOrdini.close();
            iOrdine.close();
            uOrdine.close();
            dOrdine.close();
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Ordine createOrdine() {
        return new OrdineProxy(getDataLayer());
    }

     // Metodo helper per creare un oggetto OrdineProxy da un ResultSet
    private OrdineProxy createOrdine(ResultSet rs) throws DataException {
        try {
            OrdineProxy o = (OrdineProxy) createOrdine();
             int id = rs.getInt("ID");
             o.setKey(id);
             o.setStato(StatoOrdine.valueOf(rs.getString("stato")));
              PropostaAcquistoDAO propostaAcquistoDAO = (PropostaAcquistoDAO) dataLayer.getDAO(PropostaAcquisto.class);
             o.setProposta(propostaAcquistoDAO.getPropostaAcquisto(rs.getInt("proposta_id")));
             o.setData(rs.getDate("data"));
            return o;
        } catch (SQLException ex) {
            throw new DataException("Unable to create ordine object from ResultSet", ex);
        }
    }

    @Override
    public Ordine getOrdine(int ordine_key) throws DataException {
        Ordine o = null;
        if (dataLayer.getCache().has(Ordine.class, ordine_key)) {
            o = dataLayer.getCache().get(Ordine.class, ordine_key);
        } else {
            try {
                sOrdineByID.setInt(1, ordine_key);
                try (ResultSet rs = sOrdineByID.executeQuery()) {
                    if (rs.next()) {
                        o = createOrdine(rs);
                        dataLayer.getCache().add(Ordine.class, o);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load ordine by ID", ex);
            }
        }
        return o;
    }

    @Override
    public List<Ordine> getOrdiniByUtente(int utente_key) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sOrdiniByUtente.setInt(1, utente_key);
            try (ResultSet rs = sOrdiniByUtente.executeQuery()) {
                while (rs.next()) {
                    ordini.add(createOrdine(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load ordini by utente ID", ex);
        }
        return ordini;
    }

    @Override
    public List<Ordine> getOrdiniByTecnico(int tecnico_key) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sOrdiniByTecnico.setInt(1, tecnico_key);
            try (ResultSet rs = sOrdiniByTecnico.executeQuery()) {
                while (rs.next()) {
                    ordini.add(createOrdine(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load ordini by tecnico ID", ex);
        }
        return ordini;
    }

    @Override
    public List<Ordine> getAllOrdini() throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try (ResultSet rs = sAllOrdini.executeQuery()) {
            while (rs.next()) {
                ordini.add(createOrdine(rs));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load all ordini", ex);
        }
        return ordini;
    }

    @Override
    public void storeOrdine(Ordine ordine) throws DataException {
        try {
            if (ordine.getKey() != null && ordine.getKey() > 0) {
                 // Se l'ordine è un proxy e non è stato modificato, salta l'aggiornamento
                if (ordine instanceof DataItemProxy && !((DataItemProxy) ordine).isModified()) {
                    return;
                }
                 // Aggiorna l'ordine esistente
                uOrdine.setString(1, ordine.getStato().toString());
                // uOrdine.setInt(2, ordine.getProposta().getId());
                uOrdine.setInt(2, ordine.getProposta().getKey());
                uOrdine.setDate(3, new java.sql.Date(ordine.getData().getTime()));
                uOrdine.setInt(4, ordine.getKey());
                uOrdine.executeUpdate();
            } else {
                // Inserisce un nuovo ordine nel database
                iOrdine.setString(1, ordine.getStato().toString());
                iOrdine.setInt(2, ordine.getProposta().getKey());
                iOrdine.setDate(3, new java.sql.Date(ordine.getData().getTime()));
                if (iOrdine.executeUpdate() == 1) {
                    try (ResultSet keys = iOrdine.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            ordine.setKey(key);
                            dataLayer.getCache().add(Ordine.class, ordine);
                        }
                    }
                }
            }
            if (ordine instanceof DataItemProxy) {
                ((DataItemProxy) ordine).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store ordine", ex);
        }
    }

    @Override
    public void deleteOrdine(int ordine_key) throws DataException {
        try {
            dOrdine.setInt(1, ordine_key);
            int rowsAffected = dOrdine.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataException("No ordine found with the given ID.");
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to delete ordine", ex);
        }
    }
    
     @Override
    public boolean notificaOrdine(int tecnicoId) throws DataException {

        try {
            ordiniDaNotificare.setInt(1, tecnicoId);
            try (ResultSet rs = ordiniDaNotificare.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("notifica_ordine");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Non sia riusciti a controllare se ci sono ordini respinti", ex);
        }
        return false;
    }
}
