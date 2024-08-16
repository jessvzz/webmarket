package it.univaq.f4i.iw.ex.webmarket.data.dao;


import it.univaq.f4i.iw.ex.webmarket.data.model.PropostaAcquisto;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

public interface PropostaAcquistoDAO {

    PropostaAcquisto createPropostaAcquisto();

    PropostaAcquisto getPropostaAcquisto(int proposta_key) throws DataException;

    List<PropostaAcquisto> getProposteByUtente(int utente_key) throws DataException;

    List<PropostaAcquisto> getProposteByOrdine(int ordine_key) throws DataException;

    List<PropostaAcquisto> getAllProposteAcquisto() throws DataException;

    List<PropostaAcquisto> getProposteAcquistoByRichiesta(int richiesta_id) throws DataException; 

    void deletePropostaAcquisto(int proposta_key) throws DataException;
    // Per metterlo nel db
    void storePropostaAcquisto(PropostaAcquisto proposta) throws DataException;

    void inviaPropostaAcquisto(PropostaAcquisto proposta) throws DataException;

    //void deletePropostaAcquisto(int proposta_key) throws DataException;
}