package it.univaq.f4i.iw.ex.webmarket.data.dao;



import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;

public interface UtenteDAO {

    Utente createUtente();

    Utente getUtente(int utente_key) throws DataException;

    void storeUtente(Utente utente) throws DataException;

    Utente getUtenteByEmail(String email) throws DataException;

    Utente getUtenteByUsername(String username) throws DataException;
    

}