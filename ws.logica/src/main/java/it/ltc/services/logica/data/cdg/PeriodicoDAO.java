package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgPeriodico;

public interface PeriodicoDAO {
	
	public List<CdgPeriodico> trovaTutte();
	
	public CdgPeriodico trova(int id);
	
	public CdgPeriodico inserisci(CdgPeriodico periodico);
	
	public CdgPeriodico aggiorna(CdgPeriodico periodico);
	
	public CdgPeriodico elimina(CdgPeriodico periodico);

}
