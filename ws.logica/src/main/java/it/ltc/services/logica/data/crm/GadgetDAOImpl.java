package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.Gadget;

@Repository
public class GadgetDAOImpl extends CRUDDao<Gadget> implements GadgetDAO {

	public GadgetDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Gadget.class);
	}

	@Override
	public List<Gadget> trovaTutti() {
		List<Gadget> entities = findAll();
		return entities;
	}
	
	public List<Gadget> trovaDaNome(String testo) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Gadget> criteria = cb.createQuery(Gadget.class);
        Root<Gadget> member = criteria.from(Gadget.class);
        Predicate condizioneNome = cb.like(member.get("nome"), testo + "%");
        criteria.select(member).where(condizioneNome);
		List<Gadget> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public Gadget trova(int id) {
		Gadget entity = findByID(id);
		return entity;
	}

	@Override
	public Gadget inserisci(Gadget gadget) {
		Gadget entity = insert(gadget);
		return entity;
	}

	@Override
	public Gadget aggiorna(Gadget gadget) {
		Gadget entity = update(gadget, gadget.getId());
		return entity;
	}

	@Override
	public Gadget elimina(Gadget gadget) {
		Gadget entity = delete(gadget.getId());
		return entity;
	}

	@Override
	protected void updateValues(Gadget oldEntity, Gadget entity) {
		oldEntity.setNome(entity.getNome());
		oldEntity.setDescrizione(entity.getDescrizione());		
	}

}
