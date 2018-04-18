package it.ltc.services.clienti.data.magazzino;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.legacy.Imballi;

public class ImballoDAOImpl extends CRUDDao<Imballi> implements ImballoDAO {

	public ImballoDAOImpl(String persistenceUnit) {
		super(persistenceUnit, Imballi.class);
	}

	@Override
	public Imballi trovaDaID(int id) {
		Imballi entity = findByID(id);
		return entity;
	}

	@Override
	public Imballi trovaDaCodice(String codice) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Imballi> criteria = cb.createQuery(Imballi.class);
        Root<Imballi> member = criteria.from(Imballi.class);
        criteria.select(member).where(cb.equal(member.get("codImballo"), codice));
		List<Imballi> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		Imballi imballo = lista.isEmpty() ? null : lista.get(0);
        return imballo;
	}

	@Override
	public List<Imballi> trovaTutti() {
		List<Imballi> entities = findAll();
		return entities;
	}

	@Override
	protected void updateValues(Imballi oldEntity, Imballi entity) {
		// TODO - Attualmente non utilizzato.
	}

}
