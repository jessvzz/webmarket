package it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy;

// Implementazione del Proxy che estende OrdineImpl

import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.OrdineImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoOrdine;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;


public class OrdineProxy extends OrdineImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public OrdineProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setStato(StatoOrdine stato) {
        super.setStato(stato);
        this.modified = true;
    }

    @Override
    public void setProposta(PropostaAcquisto proposta) {
        super.setProposta(proposta);
        this.modified = true;
    }

    // METODI DEL PROXY
    // PROXY-ONLY METHODS

    // Metodo per segnare il proxy come modificato
    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }


    // Metodo per verificare se il proxy è stato modificato
    @Override
    public boolean isModified() {
        return modified;
    }
}
