package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriereVocePercentuale;

public interface VociListinoCorrierePercentualeDAO {
	
	public List<ListinoCorriereVocePercentuale> trovaTutte();
	
	public ListinoCorriereVocePercentuale trova(int id);
	
	public ListinoCorriereVocePercentuale inserisci(ListinoCorriereVocePercentuale voce);
	
	public ListinoCorriereVocePercentuale aggiorna(ListinoCorriereVocePercentuale voce);
	
	public ListinoCorriereVocePercentuale elimina(ListinoCorriereVocePercentuale voce);

}
