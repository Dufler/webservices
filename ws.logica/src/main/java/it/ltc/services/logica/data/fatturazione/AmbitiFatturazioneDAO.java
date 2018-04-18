package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.FatturaAmbito;

public interface AmbitiFatturazioneDAO {
	
	public List<FatturaAmbito> trovaTutti();
	
	public FatturaAmbito trova(int id);
	
	public FatturaAmbito inserisci(FatturaAmbito ambito);
	
	public FatturaAmbito aggiorna(FatturaAmbito ambito);
	
	public FatturaAmbito elimina(FatturaAmbito ambito);

}
