package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioni;
import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioniPK;

@Repository
public class VociListinoCorriereScaglioniDAOImpl extends CRUDDao<ListinoCorriereVoceScaglioni> implements VociListinoCorriereScaglioniDAO {

	public VociListinoCorriereScaglioniDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriereVoceScaglioni.class);
	}

	@Override
	public List<ListinoCorriereVoceScaglioni> trovaTutte() {
		List<ListinoCorriereVoceScaglioni> list = findAll();
		return list;
	}

	@Override
	public List<ListinoCorriereVoceScaglioni> trovaScaglioni(int id) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ListinoCorriereVoceScaglioni> criteria = cb.createQuery(ListinoCorriereVoceScaglioni.class);
        Root<ListinoCorriereVoceScaglioni> member = criteria.from(ListinoCorriereVoceScaglioni.class);
        criteria.select(member).where(cb.equal(member.get("id").get("idVoce"), id));
		List<ListinoCorriereVoceScaglioni> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public ListinoCorriereVoceScaglioni inserisci(ListinoCorriereVoceScaglioni voce) {
		ListinoCorriereVoceScaglioni entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCorriereVoceScaglioni aggiorna(ListinoCorriereVoceScaglioni voce) {
		ListinoCorriereVoceScaglioni entity = update(voce, voce.getId());
		return entity;
	}

	@Override
	public ListinoCorriereVoceScaglioni elimina(ListinoCorriereVoceScaglioni voce) {
		ListinoCorriereVoceScaglioni entity = delete(voce.getId());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriereVoceScaglioni oldEntity, ListinoCorriereVoceScaglioni entity) {
		ListinoCorriereVoceScaglioniPK pk = oldEntity.getId();
		pk.setInizio(entity.getId().getInizio());
		pk.setFine(entity.getId().getFine());
		oldEntity.setValore(entity.getValore());
	}

}
