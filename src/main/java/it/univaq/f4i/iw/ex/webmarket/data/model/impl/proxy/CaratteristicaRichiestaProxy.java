package it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy;

import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.CaratteristicaRichiestaImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

public class CaratteristicaRichiestaProxy extends CaratteristicaRichiestaImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public CaratteristicaRichiestaProxy(DataLayer d) {
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
    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        super.setRichiestaOrdine(richiestaOrdine);
        this.modified = true;
    }

    @Override
    public void setCaratteristica(Caratteristica caratteristica) {
        super.setCaratteristica(caratteristica);
        this.modified = true;
    }

    @Override
    public void setValore(String valore) {
        super.setValore(valore);
        this.modified = true;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    @Override
    public boolean isModified() {
        return modified;
    }
}
