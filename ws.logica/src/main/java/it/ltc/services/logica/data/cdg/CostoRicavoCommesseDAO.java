package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgCostiRicaviCommesse;

public interface CostoRicavoCommesseDAO {
	
	public List<CdgCostiRicaviCommesse> trovaTutti();
	
	public CdgCostiRicaviCommesse trova(int id);
	
	public CdgCostiRicaviCommesse inserisci(CdgCostiRicaviCommesse bilancio);
	
	public CdgCostiRicaviCommesse aggiorna(CdgCostiRicaviCommesse bilancio);
	
	public CdgCostiRicaviCommesse elimina(CdgCostiRicaviCommesse bilancio);

}
