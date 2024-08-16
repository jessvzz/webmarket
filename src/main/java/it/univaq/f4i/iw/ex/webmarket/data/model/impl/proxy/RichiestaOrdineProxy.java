package it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy;

import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.Utente;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.RichiestaOrdineImpl;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.StatoRichiesta;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.Date;



public class RichiestaOrdineProxy extends RichiestaOrdineImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;

     public RichiestaOrdineProxy(DataLayer d) {
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
    public void setNote(String note) {
        super.setNote(note);
        this.modified = true;
    }

    @Override
    public void setStato(StatoRichiesta stato) {
        super.setStato(stato);
        this.modified = true;
    }

//TODO: mi da errore
    // @Override
    // public void setData(Date data) {
    //     super.setData(data);
    //     this.modified = true;
    // }

    @Override
    public void setCodiceRichiesta(String codiceRichiesta) {
        super.setCodiceRichiesta(codiceRichiesta);
        this.modified = true;
    }


    @Override
    public void setUtente(Utente utente) {
        super.setUtente(utente);
        this.modified = true;
    }

    

    @Override
    public void setTecnico(Utente tecnico) {
        super.setTecnico(tecnico);
        this.modified = true;
    }

    @Override
    public void setCategoria(Categoria categoria) {
        super.setCategoria(categoria);
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


}
