package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessaVoceProporzionale;

public interface VociListinoClienteProporzionaleDAO {
	
	public List<ListinoCommessaVoceProporzionale> trovaTutte();
	
	public ListinoCommessaVoceProporzionale trova(int id);
	
	public ListinoCommessaVoceProporzionale inserisci(ListinoCommessaVoceProporzionale voce);
	
	public ListinoCommessaVoceProporzionale aggiorna(ListinoCommessaVoceProporzionale voce);
	
	public ListinoCommessaVoceProporzionale elimina(ListinoCommessaVoceProporzionale voce);

}
