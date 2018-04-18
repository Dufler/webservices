package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.GadgetInviati;

@Repository
public class GadgetInviatiDAOImpl extends CRUDDao<GadgetInviati> implements GadgetInviatiDAO {

	public GadgetInviatiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, GadgetInviati.class);
	}

	@Override
	public List<GadgetInviati> trovaTutti() {
		List<GadgetInviati> entities = findAll();
		return entities;
	}

	@Override
	public List<GadgetInviati> trovaDaAzienda(int idAzienda) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GadgetInviati> criteria = cb.createQuery(GadgetInviati.class);
        Root<GadgetInviati> member = criteria.from(GadgetInviati.class);
        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
		List<GadgetInviati> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public List<GadgetInviati> trovaDaGadget(int idGadget) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GadgetInviati> criteria = cb.createQuery(GadgetInviati.class);
        Root<GadgetInviati> member = criteria.from(GadgetInviati.class);
        criteria.select(member).where(cb.equal(member.get("gadget"), idGadget));
		List<GadgetInviati> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public GadgetInviati trova(int id) {
		GadgetInviati entity = findByID(id);
		return entity;
	}

	@Override
	public GadgetInviati inserisci(GadgetInviati gadgetInviati) {
		GadgetInviati entity = insert(gadgetInviati);
		return entity;
	}

	@Override
	public GadgetInviati aggiorna(GadgetInviati gadgetInviati) {
		GadgetInviati entity = update(gadgetInviati, gadgetInviati.getId());
		return entity;
	}

	@Override
	public GadgetInviati elimina(GadgetInviati gadgetInviati) {
		GadgetInviati entity = delete(gadgetInviati.getId());
		return entity;
	}

	@Override
	protected void updateValues(GadgetInviati oldEntity, GadgetInviati entity) {
		oldEntity.setAzienda(entity.getAzienda());
		oldEntity.setDataInvio(entity.getDataInvio());
		oldEntity.setGadget(entity.getGadget());
		oldEntity.setNote(entity.getNote());
		oldEntity.setQuantita(entity.getQuantita());
	}

}
