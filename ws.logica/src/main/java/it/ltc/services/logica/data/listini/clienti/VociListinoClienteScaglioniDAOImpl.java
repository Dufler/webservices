package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioni;
import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioniPK;

@Repository
public class VociListinoClienteScaglioniDAOImpl extends CRUDDao<ListinoCommessaVoceScaglioni> implements VociListinoClienteScaglioniDAO {

	public VociListinoClienteScaglioniDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessaVoceScaglioni.class);
	}

	@Override
	public List<ListinoCommessaVoceScaglioni> trovaTutte() {
		List<ListinoCommessaVoceScaglioni> list = findAll();
		return list;
	}

	@Override
	public List<ListinoCommessaVoceScaglioni> trovaScaglioni(int id) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ListinoCommessaVoceScaglioni> criteria = cb.createQuery(ListinoCommessaVoceScaglioni.class);
        Root<ListinoCommessaVoceScaglioni> member = criteria.from(ListinoCommessaVoceScaglioni.class);
        criteria.select(member).where(cb.equal(member.get("id").get("idVoce"), id));
		List<ListinoCommessaVoceScaglioni> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public ListinoCommessaVoceScaglioni inserisci(ListinoCommessaVoceScaglioni voce) {
		ListinoCommessaVoceScaglioni entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCommessaVoceScaglioni aggiorna(ListinoCommessaVoceScaglioni voce) {
		ListinoCommessaVoceScaglioni entity = update(voce, voce.getId());
		return entity;
	}

	@Override
	public ListinoCommessaVoceScaglioni elimina(ListinoCommessaVoceScaglioni voce) {
		ListinoCommessaVoceScaglioni entity = delete(voce.getId());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessaVoceScaglioni oldEntity, ListinoCommessaVoceScaglioni entity) {
		ListinoCommessaVoceScaglioniPK pk = oldEntity.getId();
		pk.setInizio(entity.getId().getInizio());
		pk.setFine(entity.getId().getFine());
		oldEntity.setValore(entity.getValore());
	}

}
