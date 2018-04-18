package it.ltc.services.clienti.data.indirizzo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.services.clienti.model.prodotto.IndirizzoJSON;

// @Repository("ProdottoLegacyDAO")
public class IndirizziLegacyDAOImpl extends Dao implements IndirizziLegacyDAO {

	private static final Logger logger = Logger.getLogger("IndirizziLegacyDAOImpl");

	public IndirizziLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public MittentiOrdine trovaMittente(int idMittente) {
		EntityManager em = getManager();
		MittentiOrdine mittente = em.find(MittentiOrdine.class, idMittente);
		em.close();
		return mittente;
	}

	@Override
	public MittentiOrdine ottieniMittente(IndirizzoJSON mittente) {
		// Eseguo una ricerca per vedere se è già presente.
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MittentiOrdine> criteria = cb.createQuery(MittentiOrdine.class);
		Root<MittentiOrdine> member = criteria.from(MittentiOrdine.class);
		Predicate condizioneRagioneSociale = cb.equal(member.get("ragioneSociale"), mittente.getRagioneSociale());
		Predicate condizioneCap = cb.equal(member.get("cap"), mittente.getCap());
		Predicate condizioneIndirizzo = cb.equal(member.get("indirizzo"), mittente.getIndirizzo());
		Predicate condizioneLocalita = cb.equal(member.get("localita"), mittente.getLocalita());
		Predicate condizioneNazione = cb.equal(member.get("nazione"), mittente.getNazione());
		criteria.select(member).where(cb.and(condizioneRagioneSociale, condizioneCap, condizioneIndirizzo, condizioneLocalita, condizioneNazione));
		List<MittentiOrdine> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		// Se ho trovato corrispondenza lo restituisco, altrimenti lo inserisco
		MittentiOrdine entity;
		if (list.isEmpty()) {
			entity = new MittentiOrdine();
			entity.setCap(mittente.getCap());
			entity.setEmail(mittente.getEmail());
			entity.setIndirizzo(mittente.getIndirizzo());
			entity.setLocalita(mittente.getLocalita());
			entity.setNazione(mittente.getNazione());
			entity.setProvincia(mittente.getProvincia());
			entity.setRagioneSociale(mittente.getRagioneSociale());
			entity.setTelefono(mittente.getTelefono());
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.persist(entity);
				t.commit();
				logger.info("Inserito nuovo mittente: " + entity);
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			entity = list.get(0);
			em.close();
		}
		return entity;
	}

	@Override
	public Destinatari trovaDestinatario(int idDestinatario) {
		EntityManager em = getManager();
		Destinatari destinatario = em.find(Destinatari.class, idDestinatario);
		em.close();
		return destinatario;
	}

	@Override
	public Destinatari ottieniDestinatario(IndirizzoJSON destinatario) {
		// Eseguo un controllo sulla ragione sociale
		String ragioneSociale1 = destinatario.getRagioneSociale();
		String ragioneSociale2 = "";
		// Se è più lunga di 70 vado a tagliare la parte eccedente.
		if (ragioneSociale1.length() > 70) {
			ragioneSociale1 = ragioneSociale1.substring(0, 70);
		}
		// Se è più lunga di 35 la divido in 2
		if (ragioneSociale1.length() > 35) {
			ragioneSociale2 = ragioneSociale1.substring(35);
			ragioneSociale1 = ragioneSociale1.substring(0, 35);
		}
		// Eseguo una ricerca per vedere se è già presente.
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Destinatari> criteria = cb.createQuery(Destinatari.class);
		Root<Destinatari> member = criteria.from(Destinatari.class);
		Predicate condizioneRagioneSociale = cb.equal(member.get("ragSoc1"), ragioneSociale1);
		Predicate condizioneCap = cb.equal(member.get("cap"), destinatario.getCap());
		Predicate condizioneIndirizzo = cb.equal(member.get("indirizzo"), destinatario.getIndirizzo());
		Predicate condizioneLocalita = cb.equal(member.get("localita"), destinatario.getLocalita());
		Predicate condizioneNazione = cb.equal(member.get("codIso"), destinatario.getNazione());
		criteria.select(member).where(cb.and(condizioneRagioneSociale, condizioneCap, condizioneIndirizzo, condizioneLocalita, condizioneNazione));
		List<Destinatari> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		// Se ho trovato corrispondenza lo restituisco, altrimenti lo inserisco
		Destinatari entity;
		if (list.isEmpty()) {
			entity = new Destinatari();
			entity.setCap(destinatario.getCap());
			entity.setCodIso(destinatario.getNazione());
			entity.setCodNaz(destinatario.getNazione());
			entity.setEmail(destinatario.getEmail());
			entity.setIndirizzo(destinatario.getIndirizzo());
			entity.setLocalita(destinatario.getLocalita());
			entity.setNazione(destinatario.getNazione());
			entity.setProvincia(destinatario.getProvincia());
			entity.setRagSoc1(ragioneSociale1);
			entity.setRagSoc2(ragioneSociale2);
			entity.setTel(destinatario.getTelefono());
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.persist(entity);
				t.commit();
				logger.info("Inserito nuovo destinatario: " + entity);
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			entity = list.get(0);
			em.close();
		}
		return entity;
	}

}
