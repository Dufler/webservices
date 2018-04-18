package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.json.CdgCommessaEventoJSON;

public interface CommessaEventoDAO {

	public List<CdgCommessaEventoJSON> trovaTutte();
	
	public CdgCommessaEventoJSON trova(int commessa, int evento);
	
	public CdgCommessaEventoJSON inserisci(CdgCommessaEventoJSON json);
	
	public CdgCommessaEventoJSON aggiorna(CdgCommessaEventoJSON json);
	
	public CdgCommessaEventoJSON elimina(CdgCommessaEventoJSON json);
}
