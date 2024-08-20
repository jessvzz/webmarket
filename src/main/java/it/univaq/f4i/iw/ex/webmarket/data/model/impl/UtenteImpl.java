package it.univaq.f4i.iw.ex.webmarket.data.model.impl;

import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataItemImpl;



public class UtenteImpl extends DataItemImpl<Integer> implements Utente {

    private int id;
    private String username;
    private String email;
    private String password;
    private TipologiaUtente tipologiaUtente;

    // Costruttori
    public UtenteImpl() {
        super();
        username = "";
        email = "";
        password = "";
        tipologiaUtente = null;
    }

    public UtenteImpl(int id, String username, String email, String password, TipologiaUtente tipologiaUtente) {
        this.id= id;
        this.username = username;
        this.email= email;
        this.password= password;
        this.tipologiaUtente= tipologiaUtente;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id= id;
    }

    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public void setUsername(String username) {
        this.username= username;
    }
    
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email= email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password= password;
    }

    @Override
    public TipologiaUtente  getTipologiaUtente() {
        return tipologiaUtente;
    }

    @Override
    public void setTipologiaUtente(TipologiaUtente tipologiaUtente) {
        this.tipologiaUtente = tipologiaUtente;
    }

}
