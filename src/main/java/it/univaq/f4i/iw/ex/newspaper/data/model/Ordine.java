package it.univaq.f4i.iw.ex.newspaper.data.model;

import it.univaq.f4i.iw.ex.newspaper.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.framework.data.DataItem;


public interface Ordine extends DataItem<Integer> {
  
    int getId();
    void setId(int id);

    StatoOrdine getStato ();
    void setStato(StatoOrdine stato);
    
    PropostaAcquisto getProposta();
    void setProposta(PropostaAcquisto proposta);

}