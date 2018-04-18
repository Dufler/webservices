package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgCostiRicaviGenericiFase;

public interface CostoRicavoGenericoPerFaseDAO {
	
	public List<CdgCostiRicaviGenericiFase> trovaTutti();
	
	public List<CdgCostiRicaviGenericiFase> trovaDaIDGenerico(int id);
	
	public CdgCostiRicaviGenericiFase inserisci(CdgCostiRicaviGenericiFase genericoPerFase);
	
	public CdgCostiRicaviGenericiFase aggiorna(CdgCostiRicaviGenericiFase genericoPerFase);
	
	public CdgCostiRicaviGenericiFase elimina(CdgCostiRicaviGenericiFase genericoPerFase);

}
