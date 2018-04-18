package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessaVoce;

public interface VociListinoClienteDAO {
	
	public List<ListinoCommessaVoce> trovaTutte();
	
	public ListinoCommessaVoce trova(int id);
	
	public ListinoCommessaVoce inserisci(ListinoCommessaVoce voce);
	
	public ListinoCommessaVoce aggiorna(ListinoCommessaVoce voce);
	
	public ListinoCommessaVoce elimina(ListinoCommessaVoce voce);

}
