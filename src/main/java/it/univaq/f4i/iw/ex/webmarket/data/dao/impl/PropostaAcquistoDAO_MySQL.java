package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import it.univaq.f4i.iw.ex.webmarket.data.dao.PropostaAcquistoDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.dao.RichiestaOrdineDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.PropostaAcquistoProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;
import java.time.LocalDate;


public class PropostaAcquistoDAO_MySQL extends DAO implements PropostaAcquistoDAO {

    private PreparedStatement sPropostaByID,sProposteByOrdine,sProposteByUtente,sProposteByTecnico,sProposteByRichiesta, sAllProposte, iProposta, uProposta,uInviaProposta, dProposta, proposteDaNotificare, proposteDaNotificareOrd;

    public PropostaAcquistoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            sPropostaByID = connection.prepareStatement("SELECT * FROM proposta_acquisto WHERE ID = ?");
            sProposteByRichiesta = connection.prepareStatement("SELECT * FROM proposta_acquisto WHERE richiesta_id = ?");
            sAllProposte = connection.prepareStatement("SELECT * FROM proposta_acquisto");

            sProposteByTecnico = connection.prepareStatement("SELECT pa.* FROM proposta_acquisto pa JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID WHERE ro.tecnico = ? ORDER BY CASE WHEN (pa.stato = 'ACCETTATO') THEN 1 ELSE 2 END");
            sProposteByUtente = connection.prepareStatement("SELECT pa.* FROM proposta_acquisto pa JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID WHERE ro.utente = ? ORDER BY CASE WHEN pa.stato = 'IN_ATTESA' THEN 1 ELSE 2 END");
            
            iProposta = connection.prepareStatement("INSERT INTO proposta_acquisto (produttore, prodotto, codice, codice_prodotto, prezzo, URL, note, stato, data, motivazione, richiesta_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uProposta = connection.prepareStatement("UPDATE proposta_acquisto SET produttore=?, prodotto=?, codice=?, codice_prodotto=?, prezzo=?, URL=?, note=?, stato=?, data=?, motivazione=?, richiesta_id=?, version=? WHERE ID=? AND version=?");
            uInviaProposta = connection.prepareStatement("UPDATE proposta_acquisto SET stato=? WHERE ID=?");
            dProposta = connection.prepareStatement("DELETE FROM proposta_acquisto WHERE ID=?");
            //ho pianto
            proposteDaNotificare = connection.prepareStatement( "SELECT EXISTS(" +
            "    SELECT 1 " +
            "    FROM proposta_acquisto pa " +
            "    JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID " +
            "    WHERE (pa.stato = 'ACCETTATO' OR pa.stato = 'RIFIUTATO') " +
            "    AND ro.tecnico = ?" +
            ") AS notifica_proposta;");
            
            proposteDaNotificareOrd = connection.prepareStatement( "SELECT EXISTS(" +
            "    SELECT 1 " +
            "    FROM proposta_acquisto pa " +
            "    JOIN richiesta_ordine ro ON pa.richiesta_id = ro.ID " +
            "    WHERE (pa.stato = 'IN_ATTESA') " +
            "    AND ro.utente = ?" +
            ") AS notifica_proposta;");
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'inizializzazione del data layer per le proposte d'acquisto", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sPropostaByID.close();
            sProposteByRichiesta.close();
            sAllProposte.close();
            sProposteByTecnico.close();
            sProposteByUtente.close();
            iProposta.close();
            uProposta.close();
            uInviaProposta.close();
            dProposta.close();
        } catch (SQLException ex) {
        }
        super.destroy();
    }

    @Override
    public PropostaAcquisto createPropostaAcquisto() {
        return new PropostaAcquistoProxy(getDataLayer());
    }

    private PropostaAcquistoProxy createPropostaAcquisto(ResultSet rs) throws DataException {
        try {
            PropostaAcquistoProxy p = (PropostaAcquistoProxy) createPropostaAcquisto();
            p.setKey(rs.getInt("ID"));
            p.setProduttore(rs.getString("produttore"));
            p.setProdotto(rs.getString("prodotto"));
            p.setCodice(rs.getString("codice"));
            p.setCodiceProdotto(rs.getString("codice_prodotto"));
            p.setPrezzo(rs.getFloat("prezzo"));
            p.setUrl(rs.getString("URL"));
            p.setNote(rs.getString("note"));
            p.setStatoProposta(StatoProposta.valueOf(rs.getString("stato")));
            p.setData(rs.getDate("data"));
            p.setMotivazione(rs.getString("motivazione"));
            p.setVersion(rs.getLong("version"));
            RichiestaOrdineDAO richiestaOrdineDAO = (RichiestaOrdineDAO) dataLayer.getDAO(RichiestaOrdine.class);
            p.setRichiestaOrdine(richiestaOrdineDAO.getRichiestaOrdine(rs.getInt("richiesta_id")));
            return p;
        } catch (SQLException ex) {
            throw new DataException("Impossibile creare l'oggetto proposta d'acquisto dal ResultSet", ex);
        }
    }

    @Override
    public PropostaAcquisto getPropostaAcquisto(int proposta_key) throws DataException {
        PropostaAcquisto p = null;
        if (dataLayer.getCache().has(PropostaAcquisto.class, proposta_key)) {
            p = dataLayer.getCache().get(PropostaAcquisto.class, proposta_key);
        } else {
            try {
                sPropostaByID.setInt(1, proposta_key);
                try (ResultSet rs = sPropostaByID.executeQuery()) {
                    if (rs.next()) {
                        p = createPropostaAcquisto(rs);
                        dataLayer.getCache().add(PropostaAcquisto.class, p);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Impossibile caricare la proposta d'acquisto tramite ID", ex);
            }
        }
        return p;
    }

    @Override
    public List<PropostaAcquisto> getProposteAcquistoByRichiesta(int richiesta_id) throws DataException {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        try {
            sProposteByRichiesta.setInt(1, richiesta_id);
            try (ResultSet rs = sProposteByRichiesta.executeQuery()) {
                while (rs.next()) {
                    proposte.add(createPropostaAcquisto(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare le proposte d'acquisto per la richiesta specificata", ex);
        }
        return proposte;
    }

    @Override
    public List<PropostaAcquisto> getAllProposteAcquisto() throws DataException {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        try (ResultSet rs = sAllProposte.executeQuery()) {
            while (rs.next()) {
                proposte.add(createPropostaAcquisto(rs));
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare tutte le proposte d'acquisto", ex);
        }
        return proposte;
    }

    @Override
    public void storePropostaAcquisto(PropostaAcquisto proposta) throws DataException {
        try {
            if (proposta.getKey() != null && proposta.getKey() > 0) {

                if (proposta instanceof PropostaAcquistoProxy && !((PropostaAcquistoProxy) proposta).isModified()) {
                    return;
                }

                uProposta.setString(1, proposta.getProduttore());
                uProposta.setString(2, proposta.getProdotto());
                uProposta.setString(3, proposta.getCodice());
                uProposta.setString(4, proposta.getCodiceProdotto());
                uProposta.setDouble(5, proposta.getPrezzo());
                uProposta.setString(6, proposta.getUrl());
                uProposta.setString(7, proposta.getNote());
                uProposta.setString(8, proposta.getStatoProposta().toString());
                uProposta.setDate(9, Date.valueOf(LocalDate.now()));
                uProposta.setString(10, proposta.getMotivazione());
                uProposta.setInt(11, proposta.getRichiestaOrdine().getKey());
                long oldVersion = proposta.getVersion();
                long versione = oldVersion + 1;
                uProposta.setLong(12, versione);
                uProposta.setInt(13, proposta.getKey());
                uProposta.setLong(14, oldVersion);
                if(uProposta.executeUpdate() == 0){
                    throw new OptimisticLockException(proposta);
                }else {
                    proposta.setVersion(versione);
                }
            } else {

                iProposta.setString(1, proposta.getProduttore());
                iProposta.setString(2, proposta.getProdotto());
                iProposta.setString(3, proposta.getCodice());
                iProposta.setString(4, proposta.getCodiceProdotto());
                iProposta.setDouble(5, proposta.getPrezzo());
                iProposta.setString(6, proposta.getUrl());
                iProposta.setString(7, proposta.getNote());
                iProposta.setString(8, proposta.getStatoProposta().toString());
                iProposta.setDate(9, Date.valueOf(LocalDate.now()));
                iProposta.setString(10, proposta.getMotivazione());
                iProposta.setInt(11, proposta.getRichiestaOrdine().getKey());
                if (iProposta.executeUpdate() == 1) {
                    try (ResultSet keys = iProposta.getGeneratedKeys()) {
                        if (keys.next()) {
                            proposta.setKey(keys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile salvare la proposta d'acquisto", ex);
        }
    }

    @Override
    public List<PropostaAcquisto> getProposteByUtente(int utente_key) throws DataException {
           List<PropostaAcquisto> proposte = new ArrayList<>();
    try {
        sProposteByUtente.setInt(1, utente_key);
        try (ResultSet rs = sProposteByUtente.executeQuery()) {
            while (rs.next()) {
                proposte.add(createPropostaAcquisto(rs));
            }
        }
    } catch (SQLException ex) {
        throw new DataException("Impossibile caricare le proposte d'acquisto per l'utente specificato", ex);
    }
    return proposte;
    }

    @Override
    public List<PropostaAcquisto> getProposteByOrdine(int ordine_key) throws DataException {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        try {
            sProposteByOrdine.setInt(1, ordine_key);
            try (ResultSet rs = sProposteByOrdine.executeQuery()) {
                while (rs.next()) {
                    proposte.add(createPropostaAcquisto(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare le proposte d'acquisto per l'ordine specificato", ex);
        }
        return proposte;
    }
    

    @Override
    public void inviaPropostaAcquisto(PropostaAcquisto proposta) throws DataException {
        try {
            if (proposta.getKey() != null && proposta.getKey() > 0) {
                uInviaProposta.setString(1, StatoProposta.IN_ATTESA.toString());
                uInviaProposta.setInt(2, proposta.getKey());
                uInviaProposta.executeUpdate();
            } else {
                throw new DataException("Impossibile inviare una proposta non esistente");
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile inviare la proposta d'acquisto", ex);
        }
    }

    @Override
    public List<PropostaAcquisto> getProposteByTecnico(int tecnico_key) throws DataException {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        try {
            sProposteByTecnico.setInt(1, tecnico_key);
            try (ResultSet rs = sProposteByTecnico.executeQuery()) {
                while (rs.next()) {
                    proposte.add(createPropostaAcquisto(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare le proposte d'acquisto per il tecnico specificato", ex);
        }
        return proposte;
        }
    
    @Override
    public boolean notificaProposte(int tecnicoId) throws DataException {
        try {
            proposteDaNotificare.setInt(1, tecnicoId);

            try (ResultSet rs = proposteDaNotificare.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("notifica_proposta");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Non sia riusciti a controllare se ci sono porposte accettate o rifiutate", ex);
        }
        return false;
    }
    
    @Override
    public boolean notificaProposteOrd(int ordinanteId) throws DataException {
        try {
            proposteDaNotificareOrd.setInt(1, ordinanteId);

            try (ResultSet rs = proposteDaNotificareOrd.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("notifica_proposta");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Non sia riusciti a controllare se ci sono porposte accettate o rifiutate", ex);
        }
        return false;
    }
}
