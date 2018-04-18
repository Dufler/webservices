package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioni;

public interface VociListinoCorriereScaglioniDAO {
	
	public List<ListinoCorriereVoceScaglioni> trovaTutte();
	
	public List<ListinoCorriereVoceScaglioni> trovaScaglioni(int id);
	
	public ListinoCorriereVoceScaglioni inserisci(ListinoCorriereVoceScaglioni voce);
	
	public ListinoCorriereVoceScaglioni aggiorna(ListinoCorriereVoceScaglioni voce);
	
	public ListinoCorriereVoceScaglioni elimina(ListinoCorriereVoceScaglioni voce);

}
