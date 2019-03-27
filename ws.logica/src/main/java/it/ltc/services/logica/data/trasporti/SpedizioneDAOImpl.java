package it.ltc.services.logica.data.trasporti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.common.CorriereDao;
import it.ltc.database.dao.common.SpedizioneServizioDao;
import it.ltc.database.dao.common.TrackingStatoDao;
import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.dao.costanti.NazioneDao;
import it.ltc.database.model.centrale.Corriere;
import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.database.model.centrale.Spedizione;
import it.ltc.database.model.centrale.SpedizioneContrassegno;
import it.ltc.database.model.centrale.SpedizioneLight;
import it.ltc.database.model.centrale.SpedizioneLight.Archiviazione;
import it.ltc.database.model.centrale.SpedizioneServizio;
import it.ltc.database.model.centrale.Tracking;
import it.ltc.database.model.centrale.TrackingStato;
import it.ltc.database.model.centrale.enumcondivise.Fatturazione;
import it.ltc.database.model.costanti.Nazione;
import it.ltc.services.logica.model.trasporti.ContrassegnoJSON;
import it.ltc.services.logica.model.trasporti.CriteriRicercaSpedizioniLight;
import it.ltc.services.logica.model.trasporti.IndirizzoJSON;
import it.ltc.services.logica.model.trasporti.SpedizioneCompletaJSON;
import it.ltc.services.logica.model.trasporti.TrackingJSON;

@Repository
public class SpedizioneDAOImpl extends CRUDDao<Spedizione> implements SpedizioneDAO {
	
	private static final Logger logger = Logger.getLogger("SpedizioneDAOImpl");
	
	
	@Autowired
	private NazioneDao daoNazioni;
	
	private final SpedizioneServizioDao daoServizi;
	private final TrackingStatoDao daoStatoTracking;
	private final CorriereDao daoCorrieri;
	
	private final HashMap<String, TrackingStato> mappaCodificaStati;

	public SpedizioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Spedizione.class);
		
		//daoNazioni = new NazioneDao();
		daoServizi = new SpedizioneServizioDao();
		daoStatoTracking = new TrackingStatoDao();
		daoCorrieri = new CorriereDao();
		
		mappaCodificaStati = new HashMap<>();
	}

	@Override
	public List<Spedizione> trovaTutte() {
        List<Spedizione> spedizioni = findAll();
        return spedizioni;
	}

	@Override
	public List<Spedizione> trovaDaUltimaModifica(CriteriUltimaModifica criteri) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Spedizione> criteria = cb.createQuery(Spedizione.class);
        Root<Spedizione> member = criteria.from(Spedizione.class);
        criteria.select(member).where(cb.greaterThan(member.get("dataUltimaModifica"), criteri.getDataUltimaModifica()));
        List<Spedizione> spedizioni = em.createQuery(criteria).getResultList();
        em.close();
        return spedizioni;
	}

	@Override
	public List<Spedizione> trovaSpedizioniFatturabili(int idCommessa, Date start, Date end) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Spedizione> criteria = cb.createQuery(Spedizione.class);
        Root<Spedizione> member = criteria.from(Spedizione.class);
        Predicate condizioneData = cb.between(member.get("dataPartenza"), start, end);
        Predicate condizioneCommessa = cb.equal(member.get("idCommessa"), idCommessa);
        Predicate condizioneFatturabile = cb.equal(member.get("fatturazione"), Fatturazione.FATTURABILE);
        criteria.select(member).where(cb.and(condizioneCommessa, condizioneData, condizioneFatturabile));
        List<Spedizione> spedizioni = em.createQuery(criteria).getResultList();
        em.close();
        return spedizioni;
	}

	@Override
	public Spedizione inserisci(Spedizione spedizione) {
		Spedizione entity = insert(spedizione);
		return entity;
	}

	@Override
	public Spedizione aggiorna(Spedizione spedizione) {
		Spedizione entity = update(spedizione, spedizione.getId());
		return entity;
	}

	@Override
	public List<SpedizioneLight> trovaSpedizioni(CriteriRicercaSpedizioniLight criteri) {
		//Recupero i criteri definiti
		int idCommessa = criteri.getIdCommessa();
		String riferimento = criteri.getRiferimento();
		String destinatario = criteri.getDestinatario();
		Date start = criteri.getInizio();
		Date end = criteri.getFine();
		String stato = criteri.getStato();
		Archiviazione archiviazione = getArchiviazione(criteri);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SpedizioneLight> criteria = cb.createQuery(SpedizioneLight.class);
        Root<SpedizioneLight> member = criteria.from(SpedizioneLight.class);
        List<Predicate> condizioni = new ArrayList<>();
        //La commessa è necessaria
        Predicate condizioneCommessa = cb.equal(member.get("idCommessa"), idCommessa);
        condizioni.add(condizioneCommessa);
        //Il resto è opzionale
        if (riferimento != null && !riferimento.isEmpty()) {
        	Predicate condizioneRiferimento = cb.like(member.get("riferimentoCliente"), "%" + riferimento + "%");
        	condizioni.add(condizioneRiferimento);
        }
        if (start != null) {
        	Predicate condizioneDataInizio = cb.greaterThanOrEqualTo(member.get("dataPartenza"), start);
        	condizioni.add(condizioneDataInizio);
        }
        if (end != null) {
        	Predicate condizioneDataFine = cb.lessThanOrEqualTo(member.get("dataPartenza"), end);
        	condizioni.add(condizioneDataFine);
        }
        if (destinatario != null && !destinatario.isEmpty()) {
        	Predicate condizioneDestinatario = cb.like(member.get("ragioneSocialeDestinatario"), "%" + destinatario + "%");
        	condizioni.add(condizioneDestinatario);
        }
        if (stato != null && !stato.isEmpty()) {
        	Predicate condizioneStato = cb.equal(member.get("stato"), stato);
        	condizioni.add(condizioneStato);
        }
        if (archiviazione != null) {
        	Predicate condizioneArchiviazione = cb.equal(member.get("archiviazione"), archiviazione);
        	condizioni.add(condizioneArchiviazione);
        }
        //Condizioni particolari non definite dall'utente
        if (start == null && end == null && archiviazione == Archiviazione.NO) {
        	GregorianCalendar today = new GregorianCalendar();
        	int dayOfTheYear = today.get(Calendar.DAY_OF_YEAR);
        	today.set(Calendar.DAY_OF_YEAR, dayOfTheYear - 90);
        	Date limit = today.getTime();
        	logger.info("Non sono state specificate date: imposto come data inizio :'" + limit);
        	Predicate condizioneDataInizio = cb.greaterThanOrEqualTo(member.get("dataPartenza"), limit);
        	condizioni.add(condizioneDataInizio);
        }
        criteria.select(member).where(cb.and(condizioni.toArray(new Predicate[condizioni.size()]))).orderBy(cb.desc(member.get("dataPartenza")));
        List<SpedizioneLight> spedizioni = em.createQuery(criteria).setMaxResults(200).getResultList();
        em.close();
        for (SpedizioneLight spedizione : spedizioni) {
        	spedizione.setStato(getStato(spedizione.getStato()));
        }
        return spedizioni;
	}

	private Archiviazione getArchiviazione(CriteriRicercaSpedizioniLight criteri) {
		Archiviazione archiviazione;
		try {
			archiviazione = Archiviazione.valueOf(criteri.getArchiviazione());
		} catch (Exception e) {
			logger.warn("E' stato impostato un valore errato per la condizione di archiviazione, verra' ignorato (" + criteri.getArchiviazione() + ")");
			archiviazione = null;
		}
		return archiviazione;
	}

	@Override
	public Spedizione trovaDaID(int id) {
		Spedizione entity = findByID(id);
		return entity;
	}

	@Override
	public SpedizioneCompletaJSON trovaDettagli(int id) {
		SpedizioneCompletaJSON json;
		Spedizione spedizione = trovaDaID(id);
		if (spedizione != null) {
			json = new SpedizioneCompletaJSON();
			//Copio le info sulla spedizione
			json.setId(spedizione.getId());
			json.setColli(spedizione.getColli());
			json.setCorriere(getCorriere(spedizione));
			json.setData(spedizione.getDataPartenza());
			json.setFatturata(getFatturazione(spedizione));
			json.setGiacenza(spedizione.getGiacenza());
			json.setLetteraDiVettura(spedizione.getLetteraDiVettura());
			json.setNote(spedizione.getNote());
			json.setPeso(spedizione.getPeso());
			json.setPezzi(spedizione.getPezzi());
			json.setRiferimento(spedizione.getRiferimentoCliente());
			json.setRitardo(spedizione.getInRitardo());
			json.setServizio(getServizio(spedizione));
			json.setStato(getStato(spedizione.getStato()));
			json.setVolume(spedizione.getVolume());
			//Cerco il mittente e lo copio
			IndirizzoJSON mittente = getIndirizzo(spedizione.getIndirizzoPartenza());
			json.setMittente(mittente);
			//Cerco il destinatario e lo copio
			IndirizzoJSON destinatario = getIndirizzo(spedizione.getIndirizzoDestinazione());
			json.setDestinatario(destinatario);
			//Se è in contrassegno lo cerco e lo copio
			if (spedizione.getContrassegno()) {
				ContrassegnoJSON contrassegno = getContrassegno(spedizione);
				json.setContrassegno(contrassegno);
			}
			//Cerco il tracking e lo copio
			List<TrackingJSON> tracking = getTracking(spedizione.getId());
	        json.setTracking(tracking);
		} else {
			json = null;
		}
		return json;
	}
	
	private ContrassegnoJSON getContrassegno(Spedizione spedizione) {
		EntityManager em = getManager();
		SpedizioneContrassegno contrassegno = em.find(SpedizioneContrassegno.class, spedizione.getId());
		em.close();
		ContrassegnoJSON json;
		if (contrassegno != null) {
			json = new ContrassegnoJSON();
			json.setTipo(contrassegno.getTipo());
			json.setValore(contrassegno.getValore());
		} else {
			json = null;
		}
		return json;
	}

	private IndirizzoJSON getIndirizzo(int id) {
		EntityManager em = getManager();
		Indirizzo indirizzo = em.find(Indirizzo.class, id);
		em.close();
		IndirizzoJSON json;
		if (indirizzo != null) {
			json = new IndirizzoJSON();
			json.setCap(indirizzo.getCap());
			json.setEmail(indirizzo.getEmail());
			json.setIndirizzo(indirizzo.getIndirizzo());
			json.setLocalita(indirizzo.getLocalita());
			json.setNazione(getNazione(indirizzo.getNazione()));
			json.setProvincia(indirizzo.getProvincia());
			json.setRagioneSociale(indirizzo.getRagioneSociale());
			json.setTelefono(indirizzo.getTelefono());
		} else {
			json = null;
		}
		return json;
	}
	
	private String getNazione(String iso) {
		String testo;
		if ("ITA".equals(iso)) {
			testo = "Italia";
		} else {
			Nazione nazione = daoNazioni.trovaDaCodiceISO3(iso);
			testo = nazione != null ? nazione.getNome() : "N/A";
		}
		return testo;
	}
	
	@Override
	public List<TrackingJSON> getTracking(int id) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tracking> criteria = cb.createQuery(Tracking.class);
        Root<Tracking> member = criteria.from(Tracking.class);
        criteria.select(member).where(cb.equal(member.get("id").get("idSpedizione"), id));
        List<Tracking> list = em.createQuery(criteria).getResultList();
        em.close();
        List<TrackingJSON> tracking = new LinkedList<>();
        for (Tracking t : list) {
        	TrackingJSON tJson = new TrackingJSON();
        	tJson.setData(t.getId().getData());
        	tJson.setStato(getStato(t.getId().getStato()));
        	tJson.setDescrizione(t.getDescrizione());
        	tracking.add(tJson);
        }
        tracking.sort(null);
        return tracking;
	}
	
	private String getCorriere(Spedizione spedizione) {
		Corriere corriere = daoCorrieri.trovaDaID(spedizione.getIdCorriere());
		String testo = corriere != null ? corriere.getNome() : "N/A";
		return testo;
	}
	
	private boolean getFatturazione(Spedizione spedizione) {
		boolean fatturazione = spedizione.getFatturazione() == Fatturazione.FATTURATA;
		return fatturazione;
	}
	
	private String getStato(String codifica) {
		if (!mappaCodificaStati.containsKey(codifica)) {
			TrackingStato stato = daoStatoTracking.trovaDaCodice(codifica);
			mappaCodificaStati.put(codifica, stato);
		}
		TrackingStato stato = mappaCodificaStati.get(codifica);
		String testo = stato != null ? stato.getNome() : "N/A";
		return testo;
	}
	
	private String getServizio(Spedizione spedizione) {
		SpedizioneServizio servizio = daoServizi.trovaDaCodice(spedizione.getServizio());
		String testo = servizio != null ? servizio.getNome() : "N/A";
		return testo;
	}

	@Override
	public boolean archiviaPerTracking(int id) {
		boolean archiviazione;
		EntityManager em = getManager();
		SpedizioneLight spedizione = em.find(SpedizioneLight.class, id);
		if (spedizione != null) {
			spedizione.setArchiviazione(Archiviazione.SI);
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(spedizione);
				t.commit();
				archiviazione = true;
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
				archiviazione = false;
			} finally {
				em.close();
			}
		} else {
			archiviazione = false;
			em.close();
		}
		return archiviazione;
	}

	@Override
	public boolean eliminaPerTracking(int id) {
		boolean eliminazione;
		EntityManager em = getManager();
		SpedizioneLight spedizione = em.find(SpedizioneLight.class, id);
		if (spedizione != null) {
			spedizione.setArchiviazione(Archiviazione.ELIMINATA);
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(spedizione);
				t.commit();
				eliminazione = true;
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
				eliminazione = false;
			} finally {
				em.close();
			}
		} else {
			eliminazione = false;
			em.close();
		}
		return eliminazione;
	}

	@Override
	protected void updateValues(Spedizione entity, Spedizione spedizione) {
		entity.setAssicurazione(spedizione.getAssicurazione());
		entity.setCodiceCliente(spedizione.getCodiceCliente());
		entity.setColli(spedizione.getColli());
		entity.setContrassegno(spedizione.getContrassegno());
		entity.setCosto(spedizione.getCosto());
		entity.setDataPartenza(spedizione.getDataPartenza());
		entity.setDatiCompleti(spedizione.getDatiCompleti());
		entity.setFatturazione(spedizione.getFatturazione());
		entity.setGiacenza(spedizione.getGiacenza());
		entity.setIdCommessa(spedizione.getIdCommessa());
		entity.setIdCorriere(spedizione.getIdCorriere());
		entity.setIdDocumento(spedizione.getIdDocumento());
		entity.setIndirizzoDestinazione(spedizione.getIndirizzoDestinazione());
		entity.setIndirizzoPartenza(spedizione.getIndirizzoPartenza());
		entity.setInRitardo(spedizione.getInRitardo());
		entity.setLetteraDiVettura(spedizione.getLetteraDiVettura());
		entity.setNote(spedizione.getNote());
		entity.setParticolarita(spedizione.getParticolarita());
		entity.setPeso(spedizione.getPeso());
		entity.setPezzi(spedizione.getPezzi());
		entity.setRagioneSocialeDestinatario(spedizione.getRagioneSocialeDestinatario());
		entity.setRicavo(spedizione.getRicavo());
		entity.setRiferimentoCliente(spedizione.getRiferimentoCliente());
		entity.setRiferimentoMittente(spedizione.getRiferimentoMittente());
		entity.setServizio(spedizione.getServizio());
		entity.setStato(spedizione.getStato());
		entity.setTipo(spedizione.getTipo());
		entity.setValoreMerceDichiarato(spedizione.getValoreMerceDichiarato());
		entity.setVolume(spedizione.getVolume());	
	}

}
