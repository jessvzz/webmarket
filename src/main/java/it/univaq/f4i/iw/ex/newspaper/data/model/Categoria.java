package it.univaq.f4i.iw.ex.newspaper.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;
import java.util.List;


public interface Categoria extends DataItem<Integer> {
  
    int getId();
    void setId(int id);

    String getNome();
    void setNome(String nome);

    Categoria getPadre();
    void setPadre(Categoria padre);

    List<Caratteristica> getCaratteristiche ();
    void setCaratteristiche(List<Caratteristica> caratteristiche);

}