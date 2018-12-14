package it.ltc.services.sede.data.magazzino;

import it.ltc.services.sede.model.carico.ColloCaricoJSON;
import it.ltc.services.sede.model.magazzino.UbicazioneJSON;

public interface GestoreUbicazioniDao {
	
	public void setUtente(String utente);
	
	public ColloCaricoJSON ubicaCollo(ColloCaricoJSON collo);
	
	public UbicazioneJSON inserisci(UbicazioneJSON ubicazione);
	
	public UbicazioneJSON aggiona(UbicazioneJSON ubicazione);
	
	public UbicazioneJSON elimina(UbicazioneJSON ubicazione);

}
