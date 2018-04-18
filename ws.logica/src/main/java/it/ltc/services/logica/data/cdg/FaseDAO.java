package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgFase;

public interface FaseDAO {
	
	public List<CdgFase> trovaTutte();
	
	public CdgFase trova(int id);
	
	public CdgFase inserisci(CdgFase fase);
	
	public CdgFase aggiorna(CdgFase fase);
	
	public CdgFase elimina(CdgFase fase);

}
