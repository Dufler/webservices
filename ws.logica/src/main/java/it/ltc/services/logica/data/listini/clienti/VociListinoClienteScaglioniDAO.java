package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioni;

public interface VociListinoClienteScaglioniDAO {
	
	public List<ListinoCommessaVoceScaglioni> trovaTutte();
	
	public List<ListinoCommessaVoceScaglioni> trovaScaglioni(int id);
	
	public ListinoCommessaVoceScaglioni inserisci(ListinoCommessaVoceScaglioni voce);
	
	public ListinoCommessaVoceScaglioni aggiorna(ListinoCommessaVoceScaglioni voce);
	
	public ListinoCommessaVoceScaglioni elimina(ListinoCommessaVoceScaglioni voce);

}
