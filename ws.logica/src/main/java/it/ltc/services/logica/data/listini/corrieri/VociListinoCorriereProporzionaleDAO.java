package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriereVoceProporzionale;

public interface VociListinoCorriereProporzionaleDAO {
	
	public List<ListinoCorriereVoceProporzionale> trovaTutte();
	
	public ListinoCorriereVoceProporzionale trova(int id);
	
	public ListinoCorriereVoceProporzionale inserisci(ListinoCorriereVoceProporzionale voce);
	
	public ListinoCorriereVoceProporzionale aggiorna(ListinoCorriereVoceProporzionale voce);
	
	public ListinoCorriereVoceProporzionale elimina(ListinoCorriereVoceProporzionale voce);

}
