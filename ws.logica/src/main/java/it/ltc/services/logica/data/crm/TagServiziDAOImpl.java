package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CrmTagServiziRichiesti;
import it.ltc.database.model.centrale.CrmTagServiziRichiestiPK;

@Repository
public class TagServiziDAOImpl extends CRUDDao<CrmTagServiziRichiesti> implements TagServiziDAO {

	public TagServiziDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CrmTagServiziRichiesti.class);
	}

	@Override
	public List<CrmTagServiziRichiesti> trovaTutti() {
		List<CrmTagServiziRichiesti> entities = findAll();
		return entities;
	}

	@Override
	public List<CrmTagServiziRichiesti> trovaDaAzienda(int idAzienda) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CrmTagServiziRichiesti> criteria = cb.createQuery(CrmTagServiziRichiesti.class);
        Root<CrmTagServiziRichiesti> member = criteria.from(CrmTagServiziRichiesti.class);
        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
		List<CrmTagServiziRichiesti> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public List<CrmTagServiziRichiesti> trovaDaTag(String tag) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CrmTagServiziRichiesti> criteria = cb.createQuery(CrmTagServiziRichiesti.class);
        Root<CrmTagServiziRichiesti> member = criteria.from(CrmTagServiziRichiesti.class);
        criteria.select(member).where(cb.equal(member.get("tag"), tag));
		List<CrmTagServiziRichiesti> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public CrmTagServiziRichiesti trova(int idAzienda, String tag) {
		CrmTagServiziRichiestiPK pk = new CrmTagServiziRichiestiPK();
		pk.setAzienda(idAzienda);
		pk.setTag(tag);
		CrmTagServiziRichiesti entity = findByID(pk);
		return entity;
	}

	@Override
	public CrmTagServiziRichiesti inserisci(CrmTagServiziRichiesti tag) {
		CrmTagServiziRichiesti entity = insert(tag);
		return entity;
	}

	@Override
	public CrmTagServiziRichiesti aggiorna(CrmTagServiziRichiesti tag) {
		return null;
	}

	@Override
	public CrmTagServiziRichiesti elimina(CrmTagServiziRichiesti tag) {
		CrmTagServiziRichiesti entity = delete(tag.getPK());
		return entity;
	}

	@Override
	protected void updateValues(CrmTagServiziRichiesti oldEntity, CrmTagServiziRichiesti entity) {
		throw new UnsupportedOperationException();		
	}

}
