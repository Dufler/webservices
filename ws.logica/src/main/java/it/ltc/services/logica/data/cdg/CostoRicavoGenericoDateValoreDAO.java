package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgCostiRicaviGenericiDateValore;

public interface CostoRicavoGenericoDateValoreDAO {
	
	public List<CdgCostiRicaviGenericiDateValore> trovaTutti();
	
	public List<CdgCostiRicaviGenericiDateValore> trovaDaIDGenerico(int id);
	
	public CdgCostiRicaviGenericiDateValore trova(int id);
	
	public CdgCostiRicaviGenericiDateValore inserisci(CdgCostiRicaviGenericiDateValore generico);
	
	public CdgCostiRicaviGenericiDateValore aggiorna(CdgCostiRicaviGenericiDateValore generico);
	
	public CdgCostiRicaviGenericiDateValore elimina(CdgCostiRicaviGenericiDateValore generico);

}
