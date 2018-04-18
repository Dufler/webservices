package it.ltc.services.logica.data.trasporti;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

@Repository
public class IndirizzoDAOImpl extends CRUDDao<Indirizzo> implements IndirizziDAO {
	
	private static final Logger logger = Logger.getLogger("IndirizzoDAOImpl");
	
	public IndirizzoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Indirizzo.class);
	}

	@Override
	public Indirizzo inserisci(Indirizzo nuovoIndirizzo) {
		EntityManager em = getManager();
		Indirizzo indirizzo;
		//Controllo se ne ho già almeno un altro con le stesse caratteristiche.
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Indirizzo> criteria = cb.createQuery(Indirizzo.class);
        Root<Indirizzo> member = criteria.from(Indirizzo.class);
        Predicate likeRagioneSociale = cb.like(member.get("ragioneSociale"), "%" + nuovoIndirizzo.getRagioneSociale() + "%");
        Predicate likeIndirizzo = cb.like(member.get("indirizzo"), "%" + nuovoIndirizzo.getIndirizzo() + "%");
        Predicate likeLocalita = cb.like(member.get("localita"), "%" + nuovoIndirizzo.getLocalita() + "%");
        Predicate cap = cb.equal(member.get("cap"), nuovoIndirizzo.getCap());
        Predicate provincia = cb.equal(member.get("provincia"), nuovoIndirizzo.getProvincia());
        Predicate nazione = cb.equal(member.get("nazione"), nuovoIndirizzo.getNazione());
        criteria.select(member).where(cb.and(cap, provincia, nazione, likeRagioneSociale, likeIndirizzo, likeLocalita));
		List<Indirizzo> lista = em.createQuery(criteria).getResultList();
        if (lista.isEmpty()) {   	
        	EntityTransaction t = em.getTransaction();
        	try {
        		t.begin();
        		em.persist(nuovoIndirizzo);
        		t.commit();
        	} catch (Exception e) {
        		logger.error(e);
        		t.rollback();
        		nuovoIndirizzo = null;
        	} finally {
				em.close();
			}        	
        	indirizzo = nuovoIndirizzo;
        } else {
        	logger.info("Indirizzo gia' presente!");
        	indirizzo = lista.get(0);
        }
		return indirizzo;
	}
	
	@Override
	public Indirizzo aggiorna(Indirizzo indirizzo) {
		Indirizzo entity = update(indirizzo, indirizzo.getId());
		return entity;
	}

	@Override
	public Indirizzo elimina(Indirizzo indirizzo) {
		Indirizzo entity = delete(indirizzo.getId());
		return entity;
	}
	
	@Override
	public Indirizzo trovaDaID(int id) {
		Indirizzo indirizzo = findByID(id);
		return indirizzo;
	}

	@Override
	public List<Indirizzo> trovaTutti() {
		List<Indirizzo> lista = findAll();
        return lista;
	}
	
	@Override
	public List<Indirizzo> trovaDaUltimaModifica(CriteriUltimaModifica criteri) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Indirizzo> criteria = cb.createQuery(Indirizzo.class);
        Root<Indirizzo> member = criteria.from(Indirizzo.class);
        criteria.select(member).where(cb.greaterThan(member.get("dataUltimaModifica"), criteri.getDataUltimaModifica()));
        List<Indirizzo> spedizioni = em.createQuery(criteria).getResultList();
        em.close();
        return spedizioni;
	}

	@Override
	protected void updateValues(Indirizzo entity, Indirizzo indirizzo) {
		if (indirizzo.getCap() != null && !indirizzo.getCap().isEmpty())
			entity.setCap(indirizzo.getCap());
		entity.setConsegnaAlPiano(indirizzo.getConsegnaAlPiano());
		entity.setConsegnaAppuntamento(indirizzo.getConsegnaAppuntamento());
		entity.setConsegnaGdo(indirizzo.getConsegnaGdo());
		entity.setConsegnaPrivato(indirizzo.getConsegnaPrivato());
		if (indirizzo.getEmail() != null && !indirizzo.getEmail().isEmpty())
			entity.setEmail(indirizzo.getEmail());
		if (indirizzo.getIndirizzo() != null && !indirizzo.getIndirizzo().isEmpty())
			entity.setIndirizzo(indirizzo.getIndirizzo());
		if (indirizzo.getLocalita() != null && !indirizzo.getLocalita().isEmpty())
			entity.setLocalita(indirizzo.getLocalita());
		if (indirizzo.getNazione() != null && !indirizzo.getNazione().isEmpty())
			entity.setNazione(indirizzo.getNazione());
		if (indirizzo.getProvincia() != null && !indirizzo.getProvincia().isEmpty())
			entity.setProvincia(indirizzo.getProvincia());
		if (indirizzo.getRagioneSociale() != null && !indirizzo.getRagioneSociale().isEmpty())
			entity.setRagioneSociale(indirizzo.getRagioneSociale());
		if (indirizzo.getTelefono() != null && !indirizzo.getTelefono().isEmpty())
			entity.setTelefono(indirizzo.getTelefono());
	}

}
