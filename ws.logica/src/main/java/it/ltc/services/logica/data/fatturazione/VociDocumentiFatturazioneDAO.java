package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import javax.persistence.EntityManager;

import it.ltc.database.model.centrale.FatturaVoce;
import it.ltc.database.model.centrale.enumcondivise.Fatturazione;
import it.ltc.services.logica.model.fatturazione.ElementoFatturazioneJSON;

public interface VociDocumentiFatturazioneDAO {
	
	public List<FatturaVoce> trovaTuttePerFattura(int idFattura);
	
	public FatturaVoce trova(int id);
	
	public FatturaVoce inserisci(FatturaVoce voce);
	
	public FatturaVoce aggiorna(FatturaVoce voce);
	
	public FatturaVoce elimina(FatturaVoce voce);
	
	public boolean inserisciVoci(List<ElementoFatturazioneJSON> voci);
	
	public boolean aggiornaStatoFatturazioneDatoBase(int ambito, int riferimento, double totale, Fatturazione stato, EntityManager em);

}
