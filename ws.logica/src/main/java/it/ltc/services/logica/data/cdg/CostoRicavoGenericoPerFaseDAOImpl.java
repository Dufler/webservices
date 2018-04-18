package it.ltc.services.logica.data.cdg;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCostiRicaviGenericiFase;

@Repository
public class CostoRicavoGenericoPerFaseDAOImpl extends CRUDDao<CdgCostiRicaviGenericiFase> implements CostoRicavoGenericoPerFaseDAO {

	public CostoRicavoGenericoPerFaseDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCostiRicaviGenericiFase.class);
	}

	@Override
	public List<CdgCostiRicaviGenericiFase> trovaTutti() {
		List<CdgCostiRicaviGenericiFase> entities = findAll();
		return entities;
	}

	@Override
	public List<CdgCostiRicaviGenericiFase> trovaDaIDGenerico(int id) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgCostiRicaviGenericiFase> criteria = cb.createQuery(CdgCostiRicaviGenericiFase.class);
        Root<CdgCostiRicaviGenericiFase> member = criteria.from(CdgCostiRicaviGenericiFase.class);
        criteria.select(member).where(cb.equal(member.get("generico"), id));
        List<CdgCostiRicaviGenericiFase> entities = em.createQuery(criteria).getResultList();
        em.close();
		return entities;
	}

	@Override
	public CdgCostiRicaviGenericiFase inserisci(CdgCostiRicaviGenericiFase genericoPerFase) {
		CdgCostiRicaviGenericiFase entity = insert(genericoPerFase);
		return entity;
	}

	@Override
	public CdgCostiRicaviGenericiFase aggiorna(CdgCostiRicaviGenericiFase genericoPerFase) {
		CdgCostiRicaviGenericiFase entity = update(genericoPerFase, genericoPerFase.getPK());
		return entity;
	}

	@Override
	public CdgCostiRicaviGenericiFase elimina(CdgCostiRicaviGenericiFase genericoPerFase) {
		CdgCostiRicaviGenericiFase entity = delete(genericoPerFase.getPK());
		return entity;
	}

	@Override
	protected void updateValues(CdgCostiRicaviGenericiFase oldEntity, CdgCostiRicaviGenericiFase entity) {
		oldEntity.setPercentuale(entity.getPercentuale());		
	}

}
