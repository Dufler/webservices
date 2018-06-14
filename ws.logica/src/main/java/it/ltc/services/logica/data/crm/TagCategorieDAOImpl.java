package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CrmTagCategoriaMerceologica;
import it.ltc.database.model.centrale.CrmTagCategoriaMerceologicaPK;

@Repository
public class TagCategorieDAOImpl extends CRUDDao<CrmTagCategoriaMerceologica> implements TagCategorieDAO {

	public TagCategorieDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CrmTagCategoriaMerceologica.class);
	}

	@Override
	public List<CrmTagCategoriaMerceologica> trovaTutti() {
		List<CrmTagCategoriaMerceologica> entities = findAll();
		return entities;
	}

	@Override
	public List<CrmTagCategoriaMerceologica> trovaDaAzienda(int idAzienda) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CrmTagCategoriaMerceologica> criteria = cb.createQuery(CrmTagCategoriaMerceologica.class);
        Root<CrmTagCategoriaMerceologica> member = criteria.from(CrmTagCategoriaMerceologica.class);
        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
		List<CrmTagCategoriaMerceologica> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public List<CrmTagCategoriaMerceologica> trovaDaTag(String tag) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CrmTagCategoriaMerceologica> criteria = cb.createQuery(CrmTagCategoriaMerceologica.class);
        Root<CrmTagCategoriaMerceologica> member = criteria.from(CrmTagCategoriaMerceologica.class);
        criteria.select(member).where(cb.equal(member.get("tag"), tag));
		List<CrmTagCategoriaMerceologica> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public CrmTagCategoriaMerceologica trova(int idAzienda, String tag) {
		CrmTagCategoriaMerceologicaPK pk = new CrmTagCategoriaMerceologicaPK();
		pk.setAzienda(idAzienda);
		pk.setTag(tag);
		CrmTagCategoriaMerceologica entity = findByID(pk);
		return entity;
	}

	@Override
	public CrmTagCategoriaMerceologica inserisci(CrmTagCategoriaMerceologica tag) {
		CrmTagCategoriaMerceologica entity = insert(tag);
		return entity;
	}

	@Override
	public CrmTagCategoriaMerceologica aggiorna(CrmTagCategoriaMerceologica tag) {
		return null;
	}

	@Override
	public CrmTagCategoriaMerceologica elimina(CrmTagCategoriaMerceologica tag) {
		CrmTagCategoriaMerceologica entity = delete(tag.getPK());
		return entity;
	}

	@Override
	protected void updateValues(CrmTagCategoriaMerceologica oldEntity, CrmTagCategoriaMerceologica entity) {
		throw new UnsupportedOperationException();		
	}

}
