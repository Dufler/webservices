package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgBudget;

public interface BudgetDAO {
	
	public List<CdgBudget> trovaTutti();
	
	public CdgBudget trova(int id);
	
	public CdgBudget inserisci(CdgBudget budget);
	
	public CdgBudget aggiorna(CdgBudget budget);
	
	public CdgBudget elimina(CdgBudget budget);

}
