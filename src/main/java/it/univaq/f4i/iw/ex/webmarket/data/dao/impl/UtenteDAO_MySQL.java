package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import it.univaq.f4i.iw.ex.webmarket.data.dao.UtenteDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.TipologiaUtente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.UtenteProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.sql.Statement;

/**
 *
 * @author Giuseppe Della Penna, Giulia Di Flamminio & Samanta Di Stefano
 */

public class UtenteDAO_MySQL extends DAO implements UtenteDAO {

    private PreparedStatement sUserByID, sUserByEmail, sUserByUsername, iUser, uUser;

    public UtenteDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sUserByID = connection.prepareStatement("SELECT * FROM utente WHERE ID = ?");
            sUserByEmail = connection.prepareStatement("SELECT ID FROM utente WHERE email = ?");
            sUserByUsername = connection.prepareStatement("SELECT ID FROM utente WHERE username = ?");
            iUser = connection.prepareStatement("INSERT INTO utente (email,password, tipologia_utente, username) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uUser = connection.prepareStatement("UPDATE utente SET email=?,password=?, tipologia_utente=?, username=?, version=? WHERE ID=? AND version=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            sUserByID.close();
            sUserByEmail.close();
            iUser.close();
            uUser.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

//metodi "factory" che permettono di creare
//e inizializzare opportune implementazioni
//delle interfacce del modello dati, nascondendo
//all'utente tutti i particolari
//factory methods to create and initialize
//suitable implementations of the data model interfaces,
//hiding all the implementation details
    @Override
    public Utente createUtente() {
        return new UtenteProxy(getDataLayer());
    }

    //helper
    private UtenteProxy createUtente(ResultSet rs) throws DataException {
        try {
            UtenteProxy a = (UtenteProxy) createUtente();
            a.setKey(rs.getInt("ID"));
            a.setUsername(rs.getString("username"));
            a.setEmail(rs.getString("email"));
            a.setPassword(rs.getString("password"));
            a.setTipologiaUtente(TipologiaUtente.valueOf(rs.getString("tipologia_utente")));
            a.setVersion(rs.getLong("version"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Utente getUtente(int user_key) throws DataException {
        Utente u = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Utente.class, user_key)) {
            u = dataLayer.getCache().get(Utente.class, user_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sUserByID.setInt(1, user_key);
                try ( ResultSet rs = sUserByID.executeQuery()) {
                    if (rs.next()) {
                        //notare come utilizziamo il costrutture
                        //"helper" della classe AuthorImpl
                        //per creare rapidamente un'istanza a
                        //partire dal record corrente
                        //note how we use here the helper constructor
                        //of the AuthorImpl class to quickly
                        //create an instance from the current record

                        u = createUtente(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Utente.class, u);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load user by ID", ex);
            }
        }
        return u;
    }

    @Override
    public Utente getUtenteByEmail(String email) throws DataException {

        try {
            sUserByEmail.setString(1, email);
            try ( ResultSet rs = sUserByEmail.executeQuery()) {
                if (rs.next()) {
                    return getUtente(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find user", ex);
        }
        return null;
    }
    
    @Override
    public Utente getUtenteByUsername(String username) throws DataException {

        try {
            sUserByUsername.setString(1, username);
            try ( ResultSet rs = sUserByUsername.executeQuery()) {
                if (rs.next()) {
                    return getUtente(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find user", ex);
        }
        return null;
    }

    @Override
    public void storeUtente(Utente user) throws DataException {
        try {
            if (user.getKey() != null && user.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (user instanceof UtenteProxy && !((UtenteProxy) user).isModified()) {
                    return;
                }
                uUser.setString(1, user.getEmail());
                uUser.setString(2, user.getPassword());
                uUser.setString(3, user.getTipologiaUtente().name());
                uUser.setString(4, user.getUsername());
                long oldVersion = user.getVersion();
                long versione = oldVersion + 1;
                uUser.setLong(5, versione);
                uUser.setInt(6, user.getKey());
                uUser.setLong(7, oldVersion);
                if(uUser.executeUpdate() == 0){
                    throw new OptimisticLockException(user);
                }else {
                    user.setVersion(versione);
                }

            } else { //insert
                iUser.setString(1, user.getEmail());
                iUser.setString(2, user.getPassword());
                iUser.setString(3, user.getTipologiaUtente().name());
                iUser.setString(4, user.getUsername());


                if (iUser.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try ( ResultSet keys = iUser.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                            user.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(Utente.class, user);
                        }
                    }
                }
            }

            if (user instanceof DataItemProxy) {
                ((DataItemProxy) user).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store user", ex);
        }
    }

 
    
}