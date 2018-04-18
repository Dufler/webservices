package it.ltc.services.clienti.data.fornitore;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.services.clienti.model.prodotto.FornitoreJSON;
import it.ltc.services.clienti.model.prodotto.IndirizzoJSON;
import it.ltc.services.custom.exception.CustomException;

public class FornitoreLegacyDAOImpl extends CRUDDao<Fornitori> implements FornitoreDAO<Fornitori> {
	
	private static final Logger logger = Logger.getLogger("FornitoreLegacyDAOImpl");

	public FornitoreLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit, Fornitori.class);
	}
	
	@Override
	public FornitoreJSON trovaDaID(int idFornitore) {
		Fornitori fornitore = findByID(idFornitore);
		FornitoreJSON json = fornitore != null ? serializza(fornitore) : null;
		return json;
	}
	
	@Override
	public List<FornitoreJSON> trovaTutti() {
        List<Fornitori> list = findAll();
        List<FornitoreJSON> fornitori = new LinkedList<>();
        for (Fornitori fornitore : list)
        	fornitori.add(serializza(fornitore));
		return fornitori;
	}
	
	@Override
	public FornitoreJSON inserisci(FornitoreJSON json) {
		checkRiferimentoUnicity(json);
		Fornitori fornitore = deserializza(json);
		Fornitori entity = insert(fornitore);
		FornitoreJSON inserito = entity != null ? serializza(entity) : null;
		return inserito;
	}
	
	@Override
	public FornitoreJSON aggiorna(FornitoreJSON json) {
		String riferimento = json != null ? json.getRiferimentoCliente() : ""; 
		Fornitori entity = findByCodice(riferimento);
		FornitoreJSON aggiornato;
		if (entity != null) {
			Fornitori fornitore = deserializza(json);
			entity = update(fornitore, entity.getIdFornitore());
			aggiornato = entity != null ? serializza(entity) : null;
		} else throw new CustomException("Non esiste nessun fornitore con il codice di riferimento indicato.");
		return aggiornato;
	}
	
	@Override
	public FornitoreJSON elimina(FornitoreJSON json) {
		String riferimento = json != null ? json.getRiferimentoCliente() : ""; 
		Fornitori entity = findByCodice(riferimento);
		FornitoreJSON eliminato;
		if (entity != null) {
			entity = delete(entity.getIdFornitore());
			eliminato = entity != null ? serializza(entity) : null;
		} else throw new CustomException("Non esiste nessun fornitore con il codice di riferimento indicato.");
		return eliminato;
	}

//	@Override
//	public FornitoreJSON trovaDaID(int idFornitore) {
//		EntityManager em = getManager();
//		Fornitori fornitore = em.find(Fornitori.class, idFornitore);
//		em.close();
//		FornitoreJSON json = fornitore != null ? serializza(fornitore) : null;
//		return json;
//	}

//	@Override
//	public List<FornitoreJSON> trovaTutti() {
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Fornitori> criteria = cb.createQuery(Fornitori.class);
//        Root<Fornitori> member = criteria.from(Fornitori.class);
//        criteria.select(member);
//        List<Fornitori> list = em.createQuery(criteria).getResultList();
//        em.close();
//        List<FornitoreJSON> fornitori = new LinkedList<>();
//        for (Fornitori fornitore : list)
//        	fornitori.add(serializza(fornitore));
//		return fornitori;
//	}

//	@Override
//	public boolean inserisci(FornitoreJSON json) {
//		boolean insert;
//		if (json != null) {
//			checkRiferimentoUnicity(json);
//			Fornitori fornitore = deserializza(json);
//			EntityManager em = getManager();
//			EntityTransaction t = em.getTransaction();
//			try {
//				t.begin();
//				em.persist(fornitore);
//				t.commit();
//				insert = true;
//				logger.info("Inserito nuovo fornitore.");
//			} catch(Exception e) {
//				logger.error(e);
//				insert = false;
//				t.rollback();
//			} finally {
//				em.close();
//			}
//		} else {
//			insert = false;
//			throw new CustomException("Nessun fornitore da inserire.");
//		}
//		return insert;
//	}
	
	private void checkRiferimentoUnicity(FornitoreJSON json) throws CustomException {
		if (json == null || json.getRiferimentoCliente() == null || json.getRiferimentoCliente().isEmpty())
			throw new CustomException("Nessun fornitore da inserire.");
		Fornitori fornitore = findByCodice(json.getRiferimentoCliente());
		if (fornitore != null)
			throw new CustomException("E' gia' presente un fornitore con lo stesso codice.");
	}
	
	public Fornitori findByCodice(String codiceFornitore) {
		logger.info("Ricerca tramite codice fornitore: '" + codiceFornitore + "'");
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fornitori> criteria = cb.createQuery(Fornitori.class);
        Root<Fornitori> member = criteria.from(Fornitori.class);
        criteria.select(member).where(cb.equal(member.get("codiceFornitore"), codiceFornitore));
        List<Fornitori> list = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        Fornitori fornitore = list.isEmpty() ? null : list.get(0);
        logger.info("Fornitore trovato: " + fornitore);
		return fornitore;
	}

//	@Override
//	public boolean aggiorna(FornitoreJSON json) {
//		boolean update;
//		String riferimento = json != null ? json.getRiferimentoCliente() : ""; 
//		Fornitori fornitore = findByCodice(riferimento);
//		if (fornitore != null) {
//			EntityManager em = getManager();
//			fornitore = em.find(Fornitori.class, fornitore.getIdFornitore());
//			//Aggiorno i campi
//			fornitore.setRagSoc(json.getNome());
//			IndirizzoJSON indirizzo = json.getIndirizzo();
//			if (indirizzo != null) {
//				fornitore.setCap(indirizzo.getCap());
//				fornitore.setCitta(indirizzo.getLocalita());
//				fornitore.setCodnaz(indirizzo.getNazione());
//				fornitore.setProv(indirizzo.getProvincia());
//				fornitore.setIndirizzo(indirizzo.getIndirizzo());
//				//Opzionali, se non indicati lascio quello che ho gia'.
//				String email = indirizzo.getEmail();
//				if (email != null && !email.isEmpty())
//					fornitore.setEMail(email);
//				String telefono = indirizzo.getTelefono();
//				if (telefono != null && !telefono.isEmpty()) {
//					fornitore.setTel(telefono);
//					fornitore.setFax(telefono);
//				}
//			}
//			EntityTransaction t = em.getTransaction();
//			try {
//				t.begin();
//				em.merge(fornitore);
//				t.commit();
//				update = true;
//			} catch(Exception e) {
//				logger.error(e);
//				update = false;
//				t.rollback();
//			} finally {
//				em.close();
//			}
//			
//		} else {
//			update = false;
//			throw new CustomException("Non esiste nessun fornitore con il codice di riferimento indicato.");
//		}
//		return update;
//	}

//	@Override
//	public boolean elimina(FornitoreJSON json) {
//		boolean delete;
//		String riferimento = json != null ? json.getRiferimentoCliente() : ""; 
//		Fornitori fornitore = findByCodice(riferimento);
//		if (fornitore != null) {
//			EntityManager em = getManager();
//			fornitore = em.find(Fornitori.class, fornitore.getIdFornitore());
//			EntityTransaction t = em.getTransaction();
//			try {
//				t.begin();
//				em.remove(fornitore);
//				t.commit();
//				delete = true;
//			} catch(Exception e) {
//				logger.error(e);
//				delete = false;
//				t.rollback();
//			} finally {
//				em.close();
//			}
//		} else {
//			delete = false;
//			throw new CustomException("Non esiste nessun fornitore con il codice di riferimento indicato.");
//		}
//		return delete;
//	}

	@Override
	public Fornitori deserializza(FornitoreJSON json) {
		Fornitori fornitore;
		if (json != null) {
			fornitore = new Fornitori();
			fornitore.setIdFornitore(json.getId());
			fornitore.setRagSoc(json.getNome());
			fornitore.setCodiceFornitore(json.getRiferimentoCliente());
			//Non esiste il corrispondente delle note nei sistemi legacy - json.getNote();
			IndirizzoJSON indirizzo = json.getIndirizzo();
			if (indirizzo != null) {
				fornitore.setCap(indirizzo.getCap());
				fornitore.setCitta(indirizzo.getLocalita());
				fornitore.setCodnaz(indirizzo.getNazione());
				fornitore.setEMail(indirizzo.getEmail());
				fornitore.setFax(indirizzo.getTelefono());
				fornitore.setIndirizzo(indirizzo.getIndirizzo());
				fornitore.setNaz(indirizzo.getNazione());
				fornitore.setProv(indirizzo.getProvincia());
				fornitore.setTel(indirizzo.getTelefono());
			}
			logger.info("Fornitore deserializzato: " + fornitore);
		} else {
			fornitore = null;
		}
		return fornitore;
	}

	@Override
	public FornitoreJSON serializza(Fornitori fornitore) {
		FornitoreJSON json = new FornitoreJSON();
		if (fornitore != null) {
			json.setId(fornitore.getIdFornitore());
			json.setNome(fornitore.getRagSoc());
			json.setRiferimentoCliente(fornitore.getCodiceFornitore());
			IndirizzoJSON indirizzo = new IndirizzoJSON();
			indirizzo.setCap(fornitore.getCap());
			indirizzo.setEmail(fornitore.getEMail());
			indirizzo.setIndirizzo(fornitore.getIndirizzo());
			indirizzo.setLocalita(fornitore.getCitta());
			indirizzo.setNazione(fornitore.getCodnaz());
			indirizzo.setProvincia(fornitore.getProv());
			indirizzo.setRagioneSociale(fornitore.getRagSoc());
			indirizzo.setTelefono(fornitore.getTel());
			json.setIndirizzo(indirizzo);
		}
		return json;
	}

	@Override
	protected void updateValues(Fornitori oldEntity, Fornitori entity) {
		oldEntity.setCap(entity.getCap());
		oldEntity.setCitta(entity.getCitta());
		oldEntity.setCodnaz(entity.getCodnaz());
		oldEntity.setProv(entity.getProv());
		oldEntity.setIndirizzo(entity.getIndirizzo());
		//Opzionali, se non indicati lascio quello che ho gia'.
		String email = entity.getEMail();
		if (email != null && !email.isEmpty())
			oldEntity.setEMail(email);
		String telefono = entity.getTel();
		if (telefono != null && !telefono.isEmpty()) {
			oldEntity.setTel(telefono);
			oldEntity.setFax(telefono);
		}
	}

}
