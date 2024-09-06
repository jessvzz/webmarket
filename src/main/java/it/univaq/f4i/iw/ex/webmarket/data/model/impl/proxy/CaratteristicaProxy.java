/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy;

import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.CaratteristicaImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicaProxy extends CaratteristicaImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected int categoria_id = 0;
   
    
    public CaratteristicaProxy(DataLayer d) {
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
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setCategoria(Categoria categoria) {
        super.setCategoria(categoria);
        this.categoria_id = categoria.getId();
        this.modified =true;
    }
    

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }
    
}
