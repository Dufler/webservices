package it.ltc.services.sede.data.cdg;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.sede.CdgEventoRiepilogo;
import it.ltc.database.model.sede.CdgEventoRiepilogoPK;
import it.ltc.database.model.sede.json.CdgEventoRiepilogoJSON;
import it.ltc.services.sede.model.cdg.FiltroEventoRiepilogo;

@Repository
public class EventoRiepilogoDAOImpl extends CRUDDao<CdgEventoRiepilogo> implements EventoRiepilogoDAO {

	public EventoRiepilogoDAOImpl() {
		super(LOCAL_SEDE_PERSISTENCE_UNIT_NAME, CdgEventoRiepilogo.class);
	}

	@Override
	public List<CdgEventoRiepilogoJSON> trovaTutti() {
		List<CdgEventoRiepilogo> entities = findAll();
		List<CdgEventoRiepilogoJSON> jsons = new LinkedList<CdgEventoRiepilogoJSON>();
		for (CdgEventoRiepilogo entity : entities) {
			CdgEventoRiepilogoJSON json = serializza(entity);
			jsons.add(json);
		}
		return jsons;
	}

	@Override
	public CdgEventoRiepilogoJSON trova(int evento, int commessa, Date data) {
		CdgEventoRiepilogoPK key = new CdgEventoRiepilogoPK();
		key.setEvento(evento);
		key.setCommessa(commessa);
		key.setGiorno(data);
		CdgEventoRiepilogo entity = findByID(key);
		CdgEventoRiepilogoJSON json = serializza(entity);
		return json;
	}

	@Override
	public CdgEventoRiepilogoJSON inserisci(CdgEventoRiepilogoJSON evento) {
		CdgEventoRiepilogo entity = insert(deserializza(evento));
		CdgEventoRiepilogoJSON json = serializza(entity);
		return json;
	}

	@Override
	public CdgEventoRiepilogoJSON aggiorna(CdgEventoRiepilogoJSON evento) {
		CdgEventoRiepilogo entity = deserializza(evento);
		entity = update(entity, entity.getId());
		CdgEventoRiepilogoJSON json = serializza(entity);
		return json;
	}

	@Override
	public CdgEventoRiepilogoJSON elimina(CdgEventoRiepilogoJSON evento) {
		CdgEventoRiepilogo entity = deserializza(evento);
		entity = delete(entity.getId());
		CdgEventoRiepilogoJSON json = serializza(entity);
		return json;
	}

	@Override
	protected void updateValues(CdgEventoRiepilogo oldEntity, CdgEventoRiepilogo entity) {
		oldEntity.setDurataTotale(entity.getDurataTotale());
		oldEntity.setOperatore(entity.getOperatore());
		oldEntity.setPezzi(entity.getPezzi());
	}
	
	protected CdgEventoRiepilogo deserializza(CdgEventoRiepilogoJSON json) {
		CdgEventoRiepilogo entity;
		if (json != null) {
			entity = new CdgEventoRiepilogo();
			CdgEventoRiepilogoPK key = new CdgEventoRiepilogoPK();
			key.setCommessa(json.getCommessa());
			key.setEvento(json.getEvento());
			key.setGiorno(json.getGiorno());
			entity.setId(key);
			entity.setDurataTotale(json.getDurataTotale());
			entity.setOperatore(json.getOperatore());
			entity.setPezzi(json.getPezzi());
		} else {
			entity = null;
		}
		return entity;
	}
	
	protected CdgEventoRiepilogoJSON serializza(CdgEventoRiepilogo entity) {
		CdgEventoRiepilogoJSON json;
		if (entity != null) {
			json = new CdgEventoRiepilogoJSON();
			json.setCommessa(entity.getId().getCommessa());
			json.setEvento(entity.getId().getEvento());
			json.setGiorno(entity.getId().getGiorno());
			json.setDurataTotale(entity.getDurataTotale());
			json.setOperatore(entity.getOperatore());
			json.setPezzi(entity.getPezzi());
		} else {
			json = null;
		}
		return json;
	}

	@Override
	public List<CdgEventoRiepilogoJSON> trovaPerCommessaEData(FiltroEventoRiepilogo filtro) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgEventoRiepilogo> criteria = cb.createQuery(CdgEventoRiepilogo.class);
        Root<CdgEventoRiepilogo> member = criteria.from(CdgEventoRiepilogo.class);
        Predicate condizioneCommessa = cb.equal(member.get("id").get("commessa"), filtro.getCommessa());
        Predicate condizioneData = cb.between(member.get("id").get("giorno"), filtro.getInizio(), filtro.getFine());
        criteria.select(member).where(cb.and(condizioneCommessa, condizioneData));
		List<CdgEventoRiepilogo> lista = em.createQuery(criteria).getResultList();
		em.close();
		List<CdgEventoRiepilogoJSON> jsons = new LinkedList<CdgEventoRiepilogoJSON>();
		for (CdgEventoRiepilogo entity : lista) {
			CdgEventoRiepilogoJSON json = serializza(entity);
			jsons.add(json);
		}
		return jsons;
	}

}
