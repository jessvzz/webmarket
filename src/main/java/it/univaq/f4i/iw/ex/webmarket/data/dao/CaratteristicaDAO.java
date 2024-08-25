package it.univaq.f4i.iw.ex.webmarket.data.dao;

import it.univaq.f4i.iw.ex.webmarket.data.model.Caratteristica;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;



public interface CaratteristicaDAO {
    
    Caratteristica createCaratteristica();

    Caratteristica getCaratteristica(int Caratteristica_key) throws DataException;

    List<Caratteristica> getCaratteristicheByRichiesta(int richiesta_key) throws DataException;
    
    List<Caratteristica> getCaratteristicheByCategoria(int richiesta_key) throws DataException;


    void storeCaratteratica(Caratteristica caratteristica) throws DataException;


    List<Caratteristica> getCaratteristiche() throws DataException;

    void deleteCaratteristica(int caratteristica_key) throws DataException;

}