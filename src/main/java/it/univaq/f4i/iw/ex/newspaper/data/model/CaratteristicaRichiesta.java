package it.univaq.f4i.iw.ex.newspaper.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;


public interface CaratteristicaRichiesta extends DataItem<Integer> {
    
    int getId();
    void setId(int id);

    
    RichiestaOrdine getRichiestaOrdine();
    void setRichiestaOrdine(RichiestaOrdine richiestaOrdine);


    Caratteristica getCaratteristica();
    void setCaratteristica (Caratteristica caratteristica);

    String getValore();
    void setValore(String valore);


}