package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgEvento;

public interface EventoDAO {
	
	public List<CdgEvento> trovaTutti();
	
	public CdgEvento trova(int id);
	
	public CdgEvento inserisci(CdgEvento evento);
	
	public CdgEvento aggiorna(CdgEvento evento);
	
	public CdgEvento elimina(CdgEvento evento);

}
