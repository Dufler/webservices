package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.FatturaDocumento;


public interface DocumentiFatturazioneDAO {
	
	public List<FatturaDocumento> trovaTutti();
	
	public FatturaDocumento trova(int id);
	
	public FatturaDocumento inserisci(FatturaDocumento documento);
	
	public FatturaDocumento aggiorna(FatturaDocumento documento);
	
	public FatturaDocumento elimina(FatturaDocumento documento);

}
