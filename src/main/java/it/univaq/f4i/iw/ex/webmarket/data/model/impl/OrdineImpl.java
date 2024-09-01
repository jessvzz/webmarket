package it.univaq.f4i.iw.ex.webmarket.data.model.impl;
import it.univaq.f4i.iw.ex.webmarket.data.model.Ordine;
import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.framework.data.DataItemImpl;
import java.sql.Date;

// Implementazione concreta dell'interfaccia Ordine

public class OrdineImpl extends DataItemImpl<Integer> implements Ordine {
    private int id;
    private StatoOrdine stato;
    private PropostaAcquisto propostaAcquisto;
    private Date data;

    // Costruttori

    // Costruttore di default che inizializza lo stato e la proposta a null
    public OrdineImpl() {
        super();
        stato = null;
        propostaAcquisto = null;
    }

    // Costruttore per inizializzare l'Ordine con valori specificati
    public OrdineImpl(int id, StatoOrdine stato, PropostaAcquisto propostaAcquisto) {
        this.id = id;
        this.stato = stato;
        this.propostaAcquisto = propostaAcquisto;
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
    public StatoOrdine getStato() {
        return stato;
    }

    @Override
    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    @Override
    public PropostaAcquisto getProposta() {
        return propostaAcquisto;
    }

    @Override
    public void setProposta(PropostaAcquisto propostaAcquisto) {
        this.propostaAcquisto = propostaAcquisto;
    }

    @Override
    public Date getData() {
        return data;
       
    }

    @Override
    public void setData(Date data) {
      this.data=data;
    }
}
