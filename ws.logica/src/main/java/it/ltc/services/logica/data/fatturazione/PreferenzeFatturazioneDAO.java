package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.json.PreferenzeFatturazioneJSON;

public interface PreferenzeFatturazioneDAO {
	
	public List<PreferenzeFatturazioneJSON> trovaTutti();
	
	public PreferenzeFatturazioneJSON trova(int ambito, int commessa);
	
	public PreferenzeFatturazioneJSON inserisci(PreferenzeFatturazioneJSON preferenze);
	
	public PreferenzeFatturazioneJSON aggiorna(PreferenzeFatturazioneJSON preferenze);
	
	public PreferenzeFatturazioneJSON elimina(PreferenzeFatturazioneJSON preferenze);

}
