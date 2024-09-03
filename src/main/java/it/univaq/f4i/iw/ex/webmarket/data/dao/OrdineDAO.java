package it.univaq.f4i.iw.ex.webmarket.data.dao;

import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.framework.data.DataException;
import java.sql.SQLException;
import java.util.List;

// Interfaccia DAO per gestire le operazioni sul database relative agli Ordini

public interface OrdineDAO {

    // Crea un nuovo Ordine
    Ordine createOrdine();

    // Recupera un Ordine per chiave
    Ordine getOrdine(int ordine_key) throws DataException;

    // Recupera gli Ordini associati ad un utente specifico
    List<Ordine> getOrdiniByUtente(int utente_key) throws DataException;

    // Recupera gli Ordini associati ad un tecnico specifico
    List<Ordine> getOrdiniByTecnico(int tecnico_key) throws DataException;

    // Recupera tutti gli Ordini nel database
    List<Ordine> getAllOrdini() throws DataException;

     // Salva o aggiorna un Ordine nel database
    void storeOrdine(Ordine ordine) throws DataException;

     // Elimina un Ordine per chiave
    void deleteOrdine(int ordine_key) throws DataException;

    boolean notificaOrdine(int tecnicoId) throws DataException ;
    
    boolean notificaOrdineOrd(int utenteId) throws DataException;
}