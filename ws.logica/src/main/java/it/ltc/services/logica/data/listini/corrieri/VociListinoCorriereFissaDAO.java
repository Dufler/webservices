package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriereVoceFissa;

public interface VociListinoCorriereFissaDAO {
	
	public List<ListinoCorriereVoceFissa> trovaTutte();
	
	public ListinoCorriereVoceFissa trova(int id);
	
	public ListinoCorriereVoceFissa inserisci(ListinoCorriereVoceFissa voce);
	
	public ListinoCorriereVoceFissa aggiorna(ListinoCorriereVoceFissa voce);
	
	public ListinoCorriereVoceFissa elimina(ListinoCorriereVoceFissa voce);

}
