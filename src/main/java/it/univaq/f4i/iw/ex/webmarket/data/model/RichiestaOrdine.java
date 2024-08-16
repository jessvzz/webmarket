package it.univaq.f4i.iw.ex.webmarket.data.model;

import java.util.Date;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.framework.data.DataItem;

public interface RichiestaOrdine extends DataItem<Integer> {

    int getId();

    void setId(int id);

    String getNote();

    void setNote(String note);

    StatoRichiesta getStato();

    void setStato(StatoRichiesta stato);

    Date getData();

    void setData(Date data);

    String getCodiceRichiesta();

    void setCodiceRichiesta(String codiceRichiesta);

    Utente getUtente();

    void setUtente(Utente utente);

    Utente getTecnico();

    void setTecnico(Utente tecnico);

    Categoria getCategoria();

    void setCategoria(Categoria categoria);
}