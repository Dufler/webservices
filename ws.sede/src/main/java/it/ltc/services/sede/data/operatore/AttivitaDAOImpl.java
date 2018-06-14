package it.ltc.services.sede.data.operatore;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.sede.Attivita;

@Repository
public class AttivitaDAOImpl extends CRUDDao<Attivita> implements AttivitaDAO {
	
	private static final Logger logger = Logger.getLogger("AttivitaDaoImpl");

	public AttivitaDAOImpl() {
		super(LOCAL_SEDE_PERSISTENCE_UNIT_NAME, Attivita.class);
	}

	@Override
	public List<Attivita> trovaTutte() {
		List<Attivita> entities = findAll();
		return entities;
	}

	@Override
	public List<Attivita> trovaTutteDaOperatore(String operatore) {
		List<Attivita> lista;
		EntityManager em = getManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Attivita> criteria = cb.createQuery(c);
	        Root<Attivita> member = criteria.from(c);
	        criteria.select(member).where(cb.equal(member.get("utente"), operatore));
			lista = em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.error(e);
			lista = null;
		} finally {
			em.close();
		}		
        return lista;
	}

	@Override
	public Attivita trova(int id) {
		Attivita entity = findByID(id);
		return entity;
	}

	@Override
	public Attivita inserisci(Attivita attivita) {
		Attivita entity = insert(attivita);
		return entity;
	}

	@Override
	public Attivita aggiorna(Attivita attivita) {
		Attivita entity = update(attivita, attivita.getId());
		return entity;
	}

	@Override
	public Attivita elimina(Attivita attivita) {
		Attivita entity = delete(attivita.getId());
		return entity;
	}

	@Override
	protected void updateValues(Attivita oldEntity, Attivita entity) {
		oldEntity.setAssegnatoDa(entity.getAssegnatoDa());
		oldEntity.setCommessa(entity.getCommessa());
		oldEntity.setDataFine(entity.getDataFine());
		oldEntity.setDataInizio(entity.getDataInizio());
		oldEntity.setNote(entity.getNote());
		oldEntity.setPriorita(entity.getPriorita());
		oldEntity.setRiferimento(entity.getRiferimento());
		oldEntity.setTipo(entity.getTipo());
		oldEntity.setUtente(entity.getUtente());
	}

}
