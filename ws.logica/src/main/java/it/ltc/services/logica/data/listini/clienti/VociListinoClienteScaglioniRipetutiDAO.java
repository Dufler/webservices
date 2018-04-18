package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioniRipetuti;

public interface VociListinoClienteScaglioniRipetutiDAO {
	
	public List<ListinoCommessaVoceScaglioniRipetuti> trovaTutte();
	
	public ListinoCommessaVoceScaglioniRipetuti trova(int id);
	
	public ListinoCommessaVoceScaglioniRipetuti inserisci(ListinoCommessaVoceScaglioniRipetuti voce);
	
	public ListinoCommessaVoceScaglioniRipetuti aggiorna(ListinoCommessaVoceScaglioniRipetuti voce);
	
	public ListinoCommessaVoceScaglioniRipetuti elimina(ListinoCommessaVoceScaglioniRipetuti voce);

}
