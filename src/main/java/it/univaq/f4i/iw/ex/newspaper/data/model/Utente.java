package it.univaq.f4i.iw.ex.newspaper.data.model;

import it.univaq.f4i.iw.ex.newspaper.data.model.impl.TipologiaUtente;
import it.univaq.f4i.iw.framework.data.DataItem;



public interface Utente extends DataItem<Integer> {

    int getId();

    void setId(int id);

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);

    TipologiaUtente getTipologiaUtenteId();

    void setTipologiaUtenteId(TipologiaUtente tipologiaUtente);
}