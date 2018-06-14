package it.ltc.services.sede.data.operatore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.legacyltc.EventiDoc;
import it.ltc.database.model.legacyltc.GestionaliConf;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.model.operatore.EventoJSON;

@Repository
public class EventiDAOLegacyImpl extends CRUDDao<EventiDoc> implements EventiDAO<EventiDoc> {
	
	private static final Logger logger = Logger.getLogger("EventiDAOLegacyImpl");

	public EventiDAOLegacyImpl() {
		super("legacy-eventi", EventiDoc.class);
	}
	
	private GestionaliConf trovaInfoEventoDaID(int idEvento) {
		List<GestionaliConf> lista;
		EntityManager em = getManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<GestionaliConf> criteria = cb.createQuery(GestionaliConf.class);
	        Root<GestionaliConf> member = criteria.from(GestionaliConf.class);
	        criteria.select(member).where(cb.equal(member.get("idCosto"), idEvento));
			lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		} catch (Exception e) {
			logger.error(e);
			lista = null;
		} finally {
			em.close();
		}
		GestionaliConf entity = lista.isEmpty() ? null : lista.get(0);
        return entity;
	}
	
	private void completaInfoEvento(EventiDoc evento) {
		//Recupero le info addizionali e le inserisco.
		GestionaliConf info = trovaInfoEventoDaID(evento.getIdCosti());
		if (info != null) {
			evento.setIdDoc(info.getIdDoc());
			evento.setIdMittente(info.getIdMittente());
			evento.setIdSettore(info.getIdSettore());
			evento.setIdtipoAttività(info.getIdAttivita());
		} else {
			throw new CustomException("Non sono state inserite le informazioni legate all'ID di costo " + evento.getIdCosti());
		}
	}

	@Override
	public EventoJSON trovaEventoAperto(String operatore) {
		List<EventiDoc> lista;
		EntityManager em = getManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<EventiDoc> criteria = cb.createQuery(c);
	        Root<EventiDoc> member = criteria.from(c);
	        Predicate condizioneOperatore = cb.equal(member.get("codOpe"), operatore);
	        Predicate condizioneStato = cb.greaterThanOrEqualTo(member.get("statoEvtDoc"), "INI");
	        Predicate condizioneIngressoUscita = cb.equal(member.get("ingressoUscita"), "NO");
	        criteria.select(member).where(cb.and(condizioneOperatore, condizioneStato, condizioneIngressoUscita));
			lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		} catch (Exception e) {
			logger.error(e);
			lista = new LinkedList<>();
		} finally {
			em.close();
		}
		EventiDoc entity = lista.isEmpty() ? entity = null : lista.get(0);
		EventoJSON evento = entity != null ? serializza(entity) : null;
		return evento;
	}

	@Override
	public List<EventoJSON> trovaEventi(String operatore, Date inizio, Date fine) {
		List<EventiDoc> lista;
		EntityManager em = getManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<EventiDoc> criteria = cb.createQuery(c);
	        Root<EventiDoc> member = criteria.from(c);
	        Predicate condizioneOperatore = cb.equal(member.get("codOpe"), operatore);
	        Predicate condizioneDataInizio = cb.greaterThanOrEqualTo(member.get("dataInizio"), inizio);
	        Predicate condizioneDataFine = cb.lessThan(member.get("dataFine"), fine);
	        criteria.select(member).where(cb.and(condizioneOperatore, condizioneDataInizio, condizioneDataFine));
			lista = em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.error(e);
			lista = new LinkedList<>();
		} finally {
			em.close();
		}
		List<EventoJSON> eventi = new LinkedList<EventoJSON>();
		for (EventiDoc entity : lista) {
			EventoJSON evento = serializza(entity);
			eventi.add(evento);
		}
        return eventi;
	}

	@Override
	public EventoJSON inserisci(EventoJSON evento) {
		// Quando si tenta un inserimento controllo che non ci siano eventi aperti.
		EventoJSON aperto = trovaEventoAperto(evento.getOperatore());
		EventoJSON inserito;
		//Se ne ho trovati o se ci sono errori restituisco null, altrimeti lo inserisco.
		if (aperto == null) {
			EventiDoc entity = deserializza(evento);
			completaInfoEvento(entity);
			entity = insert(entity);
			inserito = serializza(entity);
		} else {
			inserito = null;
		}
		return inserito;
	}

	@Override
	public EventoJSON aggiorna(EventoJSON evento) {
		EventiDoc entity = deserializza(evento);
		completaInfoEvento(entity);
		entity = update(entity, entity.getIdEvtDoc());
		evento = serializza(entity);
		return evento;
	}

	@Override
	public EventoJSON elimina(EventoJSON evento) {
		EventiDoc entity = delete(evento.getId());
		evento = serializza(entity);
		return null;
	}

	@Override
	protected void updateValues(EventiDoc oldEntity, EventiDoc entity) {
		oldEntity.setCodOpe(entity.getCodOpe());
		oldEntity.setComputerEVT(entity.getComputerEVT());
		if (entity.getDataFine() != null)
			oldEntity.setDataFine(entity.getDataFine());
		if (entity.getDataInizio() != null)
			oldEntity.setDataInizio(entity.getDataInizio());
		oldEntity.setIdCosti(entity.getIdCosti());
		oldEntity.setIdDoc(entity.getIdDoc());
		oldEntity.setIdEvtDoc(entity.getIdEvtDoc());
		oldEntity.setIdMittente(entity.getIdMittente());
		oldEntity.setIdSettore(entity.getIdSettore());
		oldEntity.setIdtipoAttività(entity.getIdtipoAttività());
	}

	@Override
	public EventiDoc deserializza(EventoJSON json) {
		EventiDoc entity;
		if (json != null) {
			entity = new EventiDoc();
			entity.setCodOpe(Integer.parseInt(json.getOperatore()));
			entity.setComputerEVT(json.getClient());
			entity.setDataFine(json.getDataFine() != null ? new Timestamp(json.getDataFine().getTime()) : null);
			entity.setDataFine(json.getDataInizio() != null ? new Timestamp(json.getDataInizio().getTime()) : null);
			entity.setIdCosti(json.getEvento());
			entity.setIdEvtDoc(json.getId());
			entity.setNote(json.getNote());
			entity.setNrDocClienteS(json.getRiferimento());
			entity.setQuantità(json.getPezzi());
			entity.setStatoEvtDoc(json.getStato());
		} else {
			entity = null;
		}
		return null;
	}

	@Override
	public EventoJSON serializza(EventiDoc evento) {
		EventoJSON json;
		if (evento != null) {
			json = new EventoJSON();
			json.setClient(evento.getComputerEVT());
			json.setDataFine(evento.getDataFine());
			json.setDataInizio(evento.getDataInizio());
			json.setEvento(evento.getIdCosti());
			json.setId(evento.getIdEvtDoc());
			json.setNote(evento.getNote());
			json.setOperatore(Integer.toString(evento.getCodOpe()));
			json.setPezzi(evento.getQuantità());
			json.setRiferimento(evento.getNrDocClienteS());
			json.setStato(evento.getStatoEvtDoc());
		} else {
			json = null;
		}
		return json;
	}

}
