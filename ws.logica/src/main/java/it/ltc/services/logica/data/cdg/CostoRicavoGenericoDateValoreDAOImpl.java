package it.ltc.services.logica.data.cdg;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCostiRicaviGenericiDateValore;

@Repository
public class CostoRicavoGenericoDateValoreDAOImpl extends CRUDDao<CdgCostiRicaviGenericiDateValore> implements CostoRicavoGenericoDateValoreDAO {

	public CostoRicavoGenericoDateValoreDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCostiRicaviGenericiDateValore.class);
	}

	@Override
	public List<CdgCostiRicaviGenericiDateValore> trovaTutti() {
		List<CdgCostiRicaviGenericiDateValore> entitites = findAll();
		return entitites;
	}

	@Override
	public List<CdgCostiRicaviGenericiDateValore> trovaDaIDGenerico(int id) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgCostiRicaviGenericiDateValore> criteria = cb.createQuery(CdgCostiRicaviGenericiDateValore.class);
        Root<CdgCostiRicaviGenericiDateValore> member = criteria.from(CdgCostiRicaviGenericiDateValore.class);
        criteria.select(member).where(cb.equal(member.get("generico"), id));
		List<CdgCostiRicaviGenericiDateValore> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public CdgCostiRicaviGenericiDateValore trova(int id) {
		CdgCostiRicaviGenericiDateValore entity = findByID(id);
		return entity;
	}

	@Override
	public CdgCostiRicaviGenericiDateValore inserisci(CdgCostiRicaviGenericiDateValore generico) {
		CdgCostiRicaviGenericiDateValore entity = insert(generico);
		return entity;
	}

	@Override
	public CdgCostiRicaviGenericiDateValore aggiorna(CdgCostiRicaviGenericiDateValore generico) {
		CdgCostiRicaviGenericiDateValore entity = update(generico, generico.getId());
		return entity;
	}

	@Override
	public CdgCostiRicaviGenericiDateValore elimina(CdgCostiRicaviGenericiDateValore generico) {
		CdgCostiRicaviGenericiDateValore entity = delete(generico.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgCostiRicaviGenericiDateValore oldEntity, CdgCostiRicaviGenericiDateValore entity) {
		oldEntity.setDataEffettiva(entity.getDataEffettiva());
		oldEntity.setDataFine(entity.getDataFine());
		oldEntity.setDataInizio(entity.getDataInizio());
		oldEntity.setValore(entity.getValore());
		oldEntity.setSede(entity.getSede());
		oldEntity.setDescrizione(entity.getDescrizione());
	}

}
