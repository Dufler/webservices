package it.ltc.services.logica.data.cdg;

import java.util.List;

import it.ltc.database.model.centrale.CdgBudgetPercentualiCosto;

public interface BudgetPercentualiDAO {
	
	public List<CdgBudgetPercentualiCosto> trovaTutti();
	
	public List<CdgBudgetPercentualiCosto> trovaDaIDBudget(int idBudget);
	
	public CdgBudgetPercentualiCosto inserisci(CdgBudgetPercentualiCosto percentualeCostoBudget);
	
	public CdgBudgetPercentualiCosto aggiorna(CdgBudgetPercentualiCosto percentualeCostoBudget);
	
	public CdgBudgetPercentualiCosto elimina(CdgBudgetPercentualiCosto percentualeCostoBudget);


}
