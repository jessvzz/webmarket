package it.univaq.f4i.iw.ex.webmarket.data.model.impl;

import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.framework.data.DataItemImpl;



public class CaratteristicaRichiestaImpl extends DataItemImpl<Integer> implements CaratteristicaRichiesta {
    private int id;
    private RichiestaOrdine richiestaOrdine;
    private Caratteristica caratteristica;
    private String valore;

    // Costruttori
    public CaratteristicaRichiestaImpl() {
        super();
        richiestaOrdine=null;
        caratteristica=null;
        valore=null;
    }

    public CaratteristicaRichiestaImpl(int id, RichiestaOrdine richiestaOrdine, Caratteristica caratteristica, String valore) {
        this.id = id;
        this.richiestaOrdine = richiestaOrdine;
        this.caratteristica = caratteristica;
        this.valore = valore;
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
    public RichiestaOrdine getRichiestaOrdine() {
        return richiestaOrdine;
    }

    @Override
    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        this.richiestaOrdine = richiestaOrdine;
    }

    @Override
    public Caratteristica getCaratteristica() {
        return caratteristica;
    }

    @Override
    public void setCaratteristica(Caratteristica caratteristica) {
        this.caratteristica = caratteristica;
    }

    @Override
    public String getValore() {
        return valore;
    }

    @Override
    public void setValore(String valore) {
        this.valore = valore;
    }
}