package it.ltc.services.logica.data.cdg;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgBudgetPercentualiCosto;

@Repository
public class BudgetPercentualiDAOImpl extends CRUDDao<CdgBudgetPercentualiCosto> implements BudgetPercentualiDAO {

	public BudgetPercentualiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgBudgetPercentualiCosto.class);
	}

	@Override
	public List<CdgBudgetPercentualiCosto> trovaTutti() {
		List<CdgBudgetPercentualiCosto> entities = findAll();
		return entities;
	}

	@Override
	public List<CdgBudgetPercentualiCosto> trovaDaIDBudget(int idBudget) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgBudgetPercentualiCosto> criteria = cb.createQuery(CdgBudgetPercentualiCosto.class);
        Root<CdgBudgetPercentualiCosto> member = criteria.from(CdgBudgetPercentualiCosto.class);
        criteria.select(member).where(cb.equal(member.get("budget"), idBudget));
		List<CdgBudgetPercentualiCosto> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public CdgBudgetPercentualiCosto inserisci(CdgBudgetPercentualiCosto percentualeCostoBudget) {
		CdgBudgetPercentualiCosto entity = insert(percentualeCostoBudget);
		return entity;
	}

	@Override
	public CdgBudgetPercentualiCosto aggiorna(CdgBudgetPercentualiCosto percentualeCostoBudget) {
		CdgBudgetPercentualiCosto entity = update(percentualeCostoBudget, percentualeCostoBudget.getPK());
		return entity;
	}

	@Override
	public CdgBudgetPercentualiCosto elimina(CdgBudgetPercentualiCosto percentualeCostoBudget) {
		CdgBudgetPercentualiCosto entity = delete(percentualeCostoBudget.getPK());
		return entity;
	}

	@Override
	protected void updateValues(CdgBudgetPercentualiCosto oldEntity, CdgBudgetPercentualiCosto entity) {
		oldEntity.setPercentuale(entity.getPercentuale());		
	}

}
