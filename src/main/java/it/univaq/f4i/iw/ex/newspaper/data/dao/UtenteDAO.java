package it.univaq.f4i.iw.ex.newspaper.data.dao;



import it.univaq.f4i.iw.ex.newspaper.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

public interface UtenteDAO {

    Utente createUtente();

    Utente getUtente(int utente_key) throws DataException;

    //List<Utente> getAllUtenti() throws DataException;

    void storeUtente(Utente utente) throws DataException;

    Utente getUtenteByEmail(String email) throws DataException;

    void deleteUtente(int utente_key) throws DataException;
    

}