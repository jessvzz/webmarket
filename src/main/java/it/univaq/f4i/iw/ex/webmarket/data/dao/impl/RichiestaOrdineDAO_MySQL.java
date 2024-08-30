package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import it.univaq.f4i.iw.ex.webmarket.data.dao.RichiestaOrdineDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.RichiestaOrdineProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import java.sql.Statement;

/**
 * Implementazione di RichiestaOrdineDAO per MySQL.
 */
public class RichiestaOrdineDAO_MySQL extends DAO implements RichiestaOrdineDAO {

    private PreparedStatement sRichiestaOrdineByID, sRichiesteByUtente, iRichiestaOrdine, uRichiestaOrdine, sRichiesteInoltrate, sRichiesteNonEvase, sRichiesteTecnico, sRichiesteRisolte;

    public RichiestaOrdineDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            // Precompilazione delle query utilizzate nella classe
            sRichiestaOrdineByID = connection.prepareStatement("SELECT * FROM richiesta_ordine WHERE ID = ?");
            sRichiesteByUtente = connection.prepareStatement("SELECT * FROM richiesta_ordine WHERE utente = ?");
            sRichiesteInoltrate = connection.prepareStatement("SELECT * FROM richiesta_ordine WHERE stato = ?");
            sRichiesteNonEvase = connection.prepareStatement(
                "SELECT r.ID, r.note, r.stato, r.data, r.codice_richiesta, r.utente, r.tecnico, r.categoria_id " +
                "FROM richiesta_ordine r " +
                "WHERE r.stato = ? AND r.tecnico = ? " +
                "AND NOT EXISTS (SELECT 1 FROM proposta_acquisto p WHERE p.richiesta_id = r.ID)"
            );
            sRichiesteTecnico = connection.prepareStatement("SELECT * FROM richiesta_ordine WHERE tecnico_id = ?");
            sRichiesteRisolte = connection.prepareStatement("SELECT * FROM richiesta_ordine WHERE stato = ?");
            iRichiestaOrdine = connection.prepareStatement("INSERT INTO richiesta_ordine (note, stato, data, utente, categoria_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uRichiestaOrdine = connection.prepareStatement("UPDATE richiesta_ordine SET note=?, stato=?, data=?, codice_richiesta=?, utente_id=?, tecnico_id=?, categoria_id=? WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing RichiestaOrdine data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sRichiestaOrdineByID.close();
            sRichiesteByUtente.close();
            sRichiesteInoltrate.close();
            sRichiesteNonEvase.close();
            sRichiesteTecnico.close();
            sRichiesteRisolte.close();
            iRichiestaOrdine.close();
            uRichiestaOrdine.close();
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public RichiestaOrdine createRichiestaOrdine() {
        return new RichiestaOrdineProxy(getDataLayer());
    }

    private RichiestaOrdineProxy createRichiestaOrdine(ResultSet rs) throws DataException {
        try {
            RichiestaOrdineProxy richiesta = (RichiestaOrdineProxy) createRichiestaOrdine();
            richiesta.setKey(rs.getInt("ID"));
            richiesta.setNote(rs.getString("note"));
            richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));
            richiesta.setData(rs.getDate("data"));
            richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
            // Impostazione di utente, tecnico e categoria non implementata qui, potrebbe essere fatta attraverso ulteriori query
            int categoriaId = rs.getInt("categoria_id");
            Categoria categoria = ((ApplicationDataLayer) getDataLayer()).getCategoriaDAO().getCategoria(categoriaId);
            richiesta.setCategoria(categoria);

            int utenteId = rs.getInt("utente");
            Utente utente = ((ApplicationDataLayer) getDataLayer()).getUtenteDAO().getUtente(utenteId);
            richiesta.setUtente(utente);

            return richiesta;
        } catch (SQLException ex) {
            throw new DataException("Unable to create RichiestaOrdine object from ResultSet", ex);
        }
    }

    @Override
    public RichiestaOrdine getRichiestaOrdine(int richiesta_key) throws DataException {
        RichiestaOrdine richiesta = null;
        if (dataLayer.getCache().has(RichiestaOrdine.class, richiesta_key)) {
            richiesta = dataLayer.getCache().get(RichiestaOrdine.class, richiesta_key);
        } else {
            try {
                sRichiestaOrdineByID.setInt(1, richiesta_key);
                try (ResultSet rs = sRichiestaOrdineByID.executeQuery()) {
                    if (rs.next()) {
                        richiesta = createRichiestaOrdine(rs);
                        dataLayer.getCache().add(RichiestaOrdine.class, richiesta);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load RichiestaOrdine by ID", ex);
            }
        }
        return richiesta;
    }

    @Override
    public List<RichiestaOrdine> getRichiesteByUtente(int utente_key) throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteByUtente.setInt(1, utente_key);
            try (ResultSet rs = sRichiesteByUtente.executeQuery()) {
                while (rs.next()) {
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load RichiesteOrdine by Utente", ex);
        }
        return result;
    }

    @Override
    public void storeRichiestaOrdine(RichiestaOrdine richiesta) throws DataException {
        try {
            if (richiesta.getKey() != null && richiesta.getKey() > 0) {
                if (richiesta instanceof DataItemProxy && !((DataItemProxy) richiesta).isModified()) {
                    return;
                }
                uRichiestaOrdine.setString(1, richiesta.getNote());
                uRichiestaOrdine.setString(2, richiesta.getStato().name());
                uRichiestaOrdine.setDate(3, new java.sql.Date(richiesta.getData().getTime()));
                uRichiestaOrdine.setString(4, richiesta.getCodiceRichiesta());
                uRichiestaOrdine.setInt(5, richiesta.getUtente().getKey());
                uRichiestaOrdine.setInt(6, richiesta.getTecnico().getKey());
                uRichiestaOrdine.setInt(7, richiesta.getCategoria().getKey());
                uRichiestaOrdine.setInt(8, richiesta.getKey());
                uRichiestaOrdine.executeUpdate();
            } else {
                iRichiestaOrdine.setString(1, richiesta.getNote());
                iRichiestaOrdine.setString(2, richiesta.getStato().name());
                iRichiestaOrdine.setDate(3, new java.sql.Date(richiesta.getData().getTime()));
                iRichiestaOrdine.setInt(4, richiesta.getUtente().getKey());
                iRichiestaOrdine.setInt(5, richiesta.getCategoria().getKey());

                if (iRichiestaOrdine.executeUpdate() == 1) {
                    try (ResultSet keys = iRichiestaOrdine.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            richiesta.setKey(key);
                            dataLayer.getCache().add(RichiestaOrdine.class, richiesta);
                        }
                    }
                }
            }
            if (richiesta instanceof DataItemProxy) {
                ((DataItemProxy) richiesta).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store RichiestaOrdine", ex);
        }
    }

    
    @Override
    public List<RichiestaOrdine> getRichiesteInoltrate() throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteInoltrate.setString(1, StatoRichiesta.IN_ATTESA.name());
            try (ResultSet rs = sRichiesteInoltrate.executeQuery()) {
                while (rs.next()) {
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load RichiesteOrdine Inoltrate", ex);
        }
        return result;
    }

    @Override
    public List<RichiestaOrdine> getRichiesteNonEvase(int tecnico_key) throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteNonEvase.setString(1, StatoRichiesta.PRESA_IN_CARICO.name());
            sRichiesteNonEvase.setInt(2, tecnico_key);
            try (ResultSet rs = sRichiesteNonEvase.executeQuery()) {
                while (rs.next()) {
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load RichiesteOrdine non evase", ex);
        }
        return result;
    }

    @Override
    public List<RichiestaOrdine> getRichiesteTecnico(int tecnico_key) throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteTecnico.setInt(1, tecnico_key);
            try (ResultSet rs = sRichiesteTecnico.executeQuery()) {
                while (rs.next()) {
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load RichiesteOrdine by Tecnico", ex);
        }
        return result;
    }

    @Override
    public List<RichiestaOrdine> getRichiesteRisolte() throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteRisolte.setString(1, StatoRichiesta.RISOLTA.name());
            try (ResultSet rs = sRichiesteRisolte.executeQuery()) {
                while (rs.next()) {
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load RichiesteOrdine Risolte", ex);
        }
        return result;
    }


    // Questo metodo si occupa di inviare una richiesta ordine, il che potrebbe 
    // significare cambiare lo stato della richiesta e fare altre operazioni associate 
    // (come inviare una notifica, se necessario). Supponendo che inviare la richiesta 
    // significhi semplicemente cambiare il suo stato a IN_ATTESA

    @Override
    public void inviaRichiestaOrdine(RichiestaOrdine richiestaOrdine) throws DataException {
        if (richiestaOrdine != null && richiestaOrdine.getKey() != null) {
            richiestaOrdine.setStato(StatoRichiesta.IN_ATTESA);
            storeRichiestaOrdine(richiestaOrdine);
        } else {
            throw new DataException("Invalid RichiestaOrdine object or missing key.");
        }
    }

    @Override
    public void deleteRichiestaOrdine(int richiesta_key) throws DataException {
      try {
        PreparedStatement dRichiestaOrdine = connection.prepareStatement("DELETE FROM richiesta_ordine WHERE ID=?");
        dRichiestaOrdine.setInt(1, richiesta_key);
        dRichiestaOrdine.executeUpdate();
        dataLayer.getCache().delete(RichiestaOrdine.class, richiesta_key);  // Rimuove l'elemento dalla cache, se esistente
        dRichiestaOrdine.close();
    } catch (SQLException ex) {
        throw new DataException("Unable to delete RichiestaOrdine", ex);
    }
}
}
