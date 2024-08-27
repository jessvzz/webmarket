package it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy;

import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.PropostaAcquistoImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoProposta;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

public class PropostaAcquistoProxy extends PropostaAcquistoImpl implements DataItemProxy {


    protected boolean modified;
    protected DataLayer dataLayer;

    public PropostaAcquistoProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
    }

  
    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }


    @Override
    public void setProduttore(String produttore) {
        super.setProduttore(produttore);
        this.modified = true;
    }

    @Override
    public void setProdotto(String prodotto) {
        super.setProdotto(prodotto);
        this.modified = true;
    }

    @Override
    public void setCodiceProdotto(String codiceProdotto) {
        super.setCodiceProdotto(codiceProdotto);
        this.modified = true;
    }


    @Override
    public void setCodice(String codice) {
        super.setCodice(codice);
        this.modified = true;
    }

    @Override
    public void setUrl(String url) {
        super.setUrl(url);
        this.modified = true;
    }



    @Override
    public void setNote(String note) {
        super.setNote(note);
        this.modified = true;
    }

    @Override
    public void setStatoProposta(StatoProposta stato) {
        super.setStatoProposta(stato);
        this.modified = true;
    }

    

    @Override
    public void setMotivazione(String motivazione) {
        super.setProduttore(motivazione);
        this.modified = true;
    }

    @Override
    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        super.setRichiestaOrdine(richiestaOrdine);
        this.modified = true;
    }

    //METODI DEL PROXY
    //PROXY-ONLY METHODS

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    @Override
    public boolean isModified() {
        return modified;
    }



    // @Override
    // public boolean isModified() {
        
    //     throw new UnsupportedOperationException("Unimplemented method 'isModified'");
    // }

    // @Override
    // public void setModified(boolean dirty) {
        
    //     throw new UnsupportedOperationException("Unimplemented method 'setModified'");
    // }
    
}
