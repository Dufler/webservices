package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessaVoceFissa;

public interface VociListinoClienteFissaDAO {
	
	public List<ListinoCommessaVoceFissa> trovaTutte();
	
	public ListinoCommessaVoceFissa trova(int id);
	
	public ListinoCommessaVoceFissa inserisci(ListinoCommessaVoceFissa voce);
	
	public ListinoCommessaVoceFissa aggiorna(ListinoCommessaVoceFissa voce);
	
	public ListinoCommessaVoceFissa elimina(ListinoCommessaVoceFissa voce);

}
