package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessaVocePercentuale;

public interface VociListinoClientePercentualeDAO {
	
	public List<ListinoCommessaVocePercentuale> trovaTutte();
	
	public ListinoCommessaVocePercentuale trova(int id);
	
	public ListinoCommessaVocePercentuale inserisci(ListinoCommessaVocePercentuale voce);
	
	public ListinoCommessaVocePercentuale aggiorna(ListinoCommessaVocePercentuale voce);
	
	public ListinoCommessaVocePercentuale elimina(ListinoCommessaVocePercentuale voce);

}
