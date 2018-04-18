package it.ltc.services.logica.data.trasporti;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.centrale.Cap;
import it.ltc.database.model.centrale.CapPK;
import it.ltc.services.logica.model.trasporti.CapJSON;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

@Repository
public class CapDAOImpl extends Dao implements CapDAO {
	
	private static final Logger logger = Logger.getLogger("CapDAOImpl");

	public CapDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME);
	}

	@Override
	public boolean insert(CapJSON json) {
		boolean insert;
		Cap cap = deserializza(json);
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(cap);
			t.commit();
			insert = true;
		} catch (Exception e) {
			logger.error(e);
			t.rollback();
			insert = false;
		} finally {
			em.close();
		}
		return insert;
	}

	@Override
	public boolean update(CapJSON cap) {
		boolean update;
		EntityManager em = getManager();
		CapPK pk = new CapPK();
		pk.setCap(cap.getCap());
		pk.setLocalita(cap.getLocalita());
		Cap entity = em.find(Cap.class, pk);
		if (entity != null) {
			entity.setBrtDisagiate(cap.isBrtDisagiate());
			entity.setBrtIsole(cap.isBrtIsole());
			entity.setBrtZtl(cap.isBrtZtl());
			entity.setProvincia(cap.getProvincia());
			entity.setRegione(cap.getRegione());
			entity.setTntOreDieci(cap.getTntOreDieci());
			entity.setTntOreDodici(cap.getTntOreDodici());
			
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(entity);
				t.commit();
				update = true;
			} catch (Exception e) {
				logger.error(e);
				t.rollback();
				update = false;
			} finally {
				em.close();
			}
		} else {
			logger.error("Nessun cap corrispondente trovato da aggiornare (" + cap + ")");
			update = false;
		}
		return update;
	}

	@Override
	public boolean delete(CapJSON cap) {
		boolean delete;
		EntityManager em = getManager();
		CapPK pk = new CapPK();
		pk.setCap(cap.getCap());
		pk.setLocalita(cap.getLocalita());
		Cap entity = em.find(Cap.class, pk);
		if (entity != null) {			
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.remove(entity);
				t.commit();
				delete = true;
			} catch (Exception e) {
				logger.error(e);
				t.rollback();
				delete = false;
			} finally {
				em.close();
			}
		} else {
			logger.error("Nessun cap corrispondente trovato da eliminare (" + cap + ")");
			delete = false;
		}
		return delete;
	}
	
	@Override
	public List<CapJSON> trovaDaUltimaModifica(CriteriUltimaModifica criteri) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cap> criteria = cb.createQuery(Cap.class);
        Root<Cap> member = criteria.from(Cap.class);
        criteria.select(member).where(cb.greaterThan(member.get("dataUltimaModifica"), criteri.getDataUltimaModifica()));
        List<Cap> lista = em.createQuery(criteria).getResultList();
        em.close();
		List<CapJSON> jsons = new LinkedList<>();
		for (Cap cap : lista) {
			CapJSON json = serializza(cap);
			jsons.add(json);
		}
        return jsons;
	}

	@Override
	public List<CapJSON> findAll() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cap> criteria = cb.createQuery(Cap.class);
        Root<Cap> member = criteria.from(Cap.class);
        criteria.select(member);
		List<Cap> lista = em.createQuery(criteria).getResultList();
		em.close();
		List<CapJSON> jsons = new LinkedList<>();
		for (Cap cap : lista) {
			CapJSON json = serializza(cap);
			jsons.add(json);
		}
        return jsons;
	}

	@Override
	public CapJSON findByCap(String cap) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cap> criteria = cb.createQuery(Cap.class);
        Root<Cap> member = criteria.from(Cap.class);
        criteria.select(member).where(cb.equal(member.get("id").get("cap"), cap));
		List<Cap> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		CapJSON trovato = lista.isEmpty() ? null : serializza(lista.get(0));
		return trovato;
	}

	@Override
	public CapJSON findByCapAndTown(String cap, String town) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cap> criteria = cb.createQuery(Cap.class);
        Root<Cap> member = criteria.from(Cap.class);
        Predicate condizioneCap = cb.equal(member.get("id").get("cap"), cap);
        Predicate condizioneLocalita = cb.equal(member.get("id").get("localita"), town);
        criteria.select(member).where(cb.and(condizioneCap, condizioneLocalita));
		List<Cap> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		CapJSON trovato = lista.isEmpty() ? null : serializza(lista.get(0));
		return trovato;
	}
	
	@Override
	public Cap deserializza(CapJSON json) {
		Cap cap = new Cap();
		CapPK pk = new CapPK();
		pk.setCap(json.getCap());
		pk.setLocalita(json.getLocalita());
		cap.setId(pk);
		cap.setProvincia(json.getProvincia());
		cap.setRegione(json.getRegione());
		cap.setBrtDisagiate(json.isBrtDisagiate());
		cap.setBrtIsole(json.isBrtIsole());
		cap.setBrtZtl(json.isBrtZtl());
		cap.setTntOreDieci(json.getTntOreDieci());
		cap.setTntOreDodici(json.getTntOreDodici());
		return cap;
	}

	@Override
	public CapJSON serializza(Cap cap) {
		CapJSON json = new CapJSON();
		json.setCap(cap.getId().getCap());
		json.setLocalita(cap.getId().getLocalita());
		json.setProvincia(cap.getProvincia());
		json.setRegione(cap.getRegione());
		json.setBrtDisagiate(cap.getBrtDisagiate());
		json.setBrtIsole(cap.getBrtIsole());
		json.setBrtZtl(cap.getBrtZtl());
		json.setTntOreDieci(cap.getTntOreDieci());
		json.setTntOreDodici(cap.getTntOreDodici());
		json.setDataUltimaModifica(cap.getDataUltimaModifica());
		return json;
	}
	
	

}
