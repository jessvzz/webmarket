/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.newspaper.data.model.impl.proxy;

import it.univaq.f4i.iw.ex.newspaper.data.model.Categoria;
import it.univaq.f4i.iw.ex.newspaper.data.model.impl.CaratteristicaImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicaProxy extends CaratteristicaImpl implements DataItemProxy{
    
    protected boolean modified;
    protected int categoria_id = 0;
    /*
    protected int author_key = 0;
    protected int issue_key = 0;
*/
    protected DataLayer dataLayer;
    


    @Override
    public void setId(int id) {
        super.setId(id);
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

    public void setKey(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
