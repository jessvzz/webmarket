package it.univaq.f4i.iw.ex.newspaper.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;


public interface Caratteristica extends DataItem<Integer> {
    
    int getId();
    void setId(int id);

    
    String getNome();
    void setNome(String nome);


    Categoria getCategoria();
    void setCategoria(Categoria categoria);

}