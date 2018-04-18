package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioniRipetuti;

public interface VociListinoCorriereScaglioniRipetutiDAO {
	
	public List<ListinoCorriereVoceScaglioniRipetuti> trovaTutte();
	
	public ListinoCorriereVoceScaglioniRipetuti trova(int id);
	
	public ListinoCorriereVoceScaglioniRipetuti inserisci(ListinoCorriereVoceScaglioniRipetuti voce);
	
	public ListinoCorriereVoceScaglioniRipetuti aggiorna(ListinoCorriereVoceScaglioniRipetuti voce);
	
	public ListinoCorriereVoceScaglioniRipetuti elimina(ListinoCorriereVoceScaglioniRipetuti voce);

}
