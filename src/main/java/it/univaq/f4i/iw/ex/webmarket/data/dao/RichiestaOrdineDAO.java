package it.univaq.f4i.iw.ex.webmarket.data.dao;

import it.univaq.f4i.iw.ex.webmarket.data.model.RichiestaOrdine;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;


public interface RichiestaOrdineDAO {

    RichiestaOrdine createRichiestaOrdine();

    RichiestaOrdine getRichiestaOrdine(int RichiestaOrdine_key) throws DataException;

    List<RichiestaOrdine> getRichiesteByUtente(int utente_key) throws DataException;

    // Per metterlo nel db
    void storeRichiestaOrdine(RichiestaOrdine RichiestaOrdine) throws DataException;

    void inviaRichiestaOrdine(RichiestaOrdine RichiestaOrdine) throws DataException;

    List<RichiestaOrdine> getRichiesteInoltrate() throws DataException;

    List<RichiestaOrdine> getRichiesteNonEvase(int tecnico_key) throws DataException;

    // Per ogni singolo tecnico
    List<RichiestaOrdine> getRichiesteTecnico(int tecnico_key) throws DataException;

    List<RichiestaOrdine> getRichiesteRisolte() throws DataException;

    void deleteRichiestaOrdine(int richiesta_key) throws DataException;

}