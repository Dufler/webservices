package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriereVoce;

public interface VociListinoCorriereDAO {
	
	public List<ListinoCorriereVoce> trovaTutte();
	
	public ListinoCorriereVoce trova(int id);
	
	public ListinoCorriereVoce inserisci(ListinoCorriereVoce voce);
	
	public ListinoCorriereVoce aggiorna(ListinoCorriereVoce voce);
	
	public ListinoCorriereVoce elimina(ListinoCorriereVoce voce);

}
