package it.univaq.f4i.iw.ex.newspaper.data.dao;

import it.univaq.f4i.iw.ex.newspaper.data.model.Caratteristica;
import it.univaq.f4i.iw.ex.newspaper.data.model.CaratteristicaRichiesta;
import it.univaq.f4i.iw.ex.newspaper.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

public interface CaratteristicaRichiestaDAO {

    CaratteristicaRichiesta createCaratteristicaRichiesta();

    CaratteristicaRichiesta getCaratteristicaRichiesta(int caratteristica_key, int richiesta_key) throws DataException;

    List<Caratteristica> getCaratteristicheByRichiesta(int richiesta_key) throws DataException;

    List<RichiestaOrdine> getRichiesteByCaratteristica(int caratteristica_key) throws DataException;

    void storeCaratteristicaRichiesta(CaratteristicaRichiesta caratteristica) throws DataException;

}