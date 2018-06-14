package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgCommessaEvento;

public interface CommessaEventoDAO {

	public List<CdgCommessaEvento> trovaTutte();
	
	public CdgCommessaEvento trova(int commessa, int evento);
	
	public CdgCommessaEvento inserisci(CdgCommessaEvento json);
	
	public CdgCommessaEvento aggiorna(CdgCommessaEvento json);
	
	public CdgCommessaEvento elimina(CdgCommessaEvento json);
}
