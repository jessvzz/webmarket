package it.univaq.f4i.iw.ex.webmarket.data.model.impl;

import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataItemImpl;
import java.util.Date;



public class RichiestaOrdineImpl extends DataItemImpl<Integer> implements RichiestaOrdine {
    private int id;
    private String note;
    private StatoRichiesta stato; //alternativa boolean senza enum
    private Date data;
    private String codiceRichiesta;
    private Utente utente;
    private Utente tecnico;
    private Categoria categoria;

    // Costruttori
    public RichiestaOrdineImpl() {
        super();
        note = "";
        stato = null;
        data = null;
        codiceRichiesta = "";
        utente = null;
        tecnico = null;
        categoria = null;
    }

    public RichiestaOrdineImpl(int id, String note, StatoRichiesta stato, Date data, String codiceRichiesta, Utente utente, Utente tecnico, Categoria categoria) {
        this.id = id;
        this.note = note;
        this.stato = stato;
        this.data = data;
        this.codiceRichiesta = codiceRichiesta;
        this.utente = utente;
        this.tecnico = tecnico;
        this.categoria = categoria;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public StatoRichiesta getStato() {
        return stato;
    }

    @Override
    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    @Override
    public Date getData() {
        return data;
    }

    @Override
    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String getCodiceRichiesta() {
        return codiceRichiesta;
    }

    @Override
    public void setCodiceRichiesta(String codiceRichiesta) {
        this.codiceRichiesta = codiceRichiesta;
    }

    @Override
    public Utente getUtente() {
        return utente;
    }

    @Override
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @Override
    public Utente getTecnico() {
        return tecnico;
    }

    @Override
    public void setTecnico(Utente tecnico) {
        this.tecnico = tecnico;
    }

    @Override
    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}