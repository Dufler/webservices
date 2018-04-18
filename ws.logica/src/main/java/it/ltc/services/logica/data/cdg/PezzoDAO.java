package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgPezzo;

public interface PezzoDAO {
	
	public List<CdgPezzo> trovaTutte();
	
	public CdgPezzo trova(int id);
	
	public CdgPezzo inserisci(CdgPezzo pezzo);
	
	public CdgPezzo aggiorna(CdgPezzo pezzo);
	
	public CdgPezzo elimina(CdgPezzo pezzo);

}
