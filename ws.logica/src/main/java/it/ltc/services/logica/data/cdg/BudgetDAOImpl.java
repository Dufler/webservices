package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgBudget;

@Repository
public class BudgetDAOImpl extends CRUDDao<CdgBudget> implements BudgetDAO {

	public BudgetDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgBudget.class);
	}

	@Override
	public List<CdgBudget> trovaTutti() {
		List<CdgBudget> entities = findAll();
		return entities;
	}

	@Override
	public CdgBudget trova(int id) {
		CdgBudget entity = findByID(id);
		return entity;
	}

	@Override
	public CdgBudget inserisci(CdgBudget budget) {
		CdgBudget entity = insert(budget);
		return entity;
	}

	@Override
	public CdgBudget aggiorna(CdgBudget budget) {
		CdgBudget entity = update(budget, budget.getId());
		return entity;
	}

	@Override
	public CdgBudget elimina(CdgBudget budget) {
		CdgBudget entity = delete(budget.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgBudget oldEntity, CdgBudget entity) {
		oldEntity.setCommessa(entity.getCommessa());
		oldEntity.setCosto(entity.getCosto());
		oldEntity.setRicavo(entity.getRicavo());
		oldEntity.setDataFine(entity.getDataFine());
		oldEntity.setDataInizio(entity.getDataInizio());
		oldEntity.setDescrizione(entity.getDescrizione());
	}

}
