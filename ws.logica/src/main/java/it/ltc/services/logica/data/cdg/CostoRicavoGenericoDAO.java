package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgCostiRicaviGenerici;

public interface CostoRicavoGenericoDAO {
	
	public List<CdgCostiRicaviGenerici> trovaTutti();
	
	public CdgCostiRicaviGenerici trova(int id);
	
	public CdgCostiRicaviGenerici inserisci(CdgCostiRicaviGenerici bilancio);
	
	public CdgCostiRicaviGenerici aggiorna(CdgCostiRicaviGenerici bilancio);
	
	public CdgCostiRicaviGenerici elimina(CdgCostiRicaviGenerici bilancio);

}
