package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgCostoAssociazione;

public interface CostoAssociazioneDAO {
	
	public List<CdgCostoAssociazione> trovaTutte();
	
	public CdgCostoAssociazione trova(int id);
	
	public CdgCostoAssociazione inserisci(CdgCostoAssociazione costo);
	
	public CdgCostoAssociazione aggiorna(CdgCostoAssociazione costo);
	
	public CdgCostoAssociazione elimina(CdgCostoAssociazione costo);

}
