package it.univaq.f4i.iw.ex.webmarket.data.model.impl;

import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author user
 */
public class CaratteristicaImpl extends DataItemImpl<Integer> implements Caratteristica{

    private int id;
    private String nome;
    private Categoria categoria;
    
    // Costruttori
    public CaratteristicaImpl() {
        super();
        nome = "";
        categoria = null;
    }

    public CaratteristicaImpl(int id, String nome, Categoria categoria) {
        this.id= id;
        this.nome= nome;
        this.categoria= categoria;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id= id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome= nome;
    }

    @Override
    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public void setCategoria(Categoria categoria) {
        this.categoria= categoria;
    }
}