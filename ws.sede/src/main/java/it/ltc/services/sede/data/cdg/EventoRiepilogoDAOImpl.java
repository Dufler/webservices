package it.ltc.services.sede.data.cdg;

import java.util.Date;
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
import it.ltc.services.sede.model.cdg.FiltroEventoRiepilogo;

@Repository
public class EventoRiepilogoDAOImpl extends CRUDDao<CdgEventoRiepilogo> implements EventoRiepilogoDAO {

	public EventoRiepilogoDAOImpl() {
		super(LOCAL_SEDE_PERSISTENCE_UNIT_NAME, CdgEventoRiepilogo.class);
	}

	@Override
	public List<CdgEventoRiepilogo> trovaTutti() {
		List<CdgEventoRiepilogo> entities = findAll();
		return entities;
	}

	@Override
	public CdgEventoRiepilogo trova(int evento, int commessa, Date data) {
		CdgEventoRiepilogoPK key = new CdgEventoRiepilogoPK();
		key.setEvento(evento);
		key.setCommessa(commessa);
		key.setGiorno(data);
		CdgEventoRiepilogo entity = findByID(key);
		return entity;
	}

	@Override
	public CdgEventoRiepilogo inserisci(CdgEventoRiepilogo evento) {
		CdgEventoRiepilogo entity = insert(evento);
		return entity;
	}

	@Override
	public CdgEventoRiepilogo aggiorna(CdgEventoRiepilogo evento) {
		CdgEventoRiepilogo entity = update(evento, evento.getPK());
		return entity;
	}

	@Override
	public CdgEventoRiepilogo elimina(CdgEventoRiepilogo evento) {
		CdgEventoRiepilogo entity = delete(evento.getPK());
		return entity;
	}

	@Override
	protected void updateValues(CdgEventoRiepilogo oldEntity, CdgEventoRiepilogo entity) {
		oldEntity.setDurataTotale(entity.getDurataTotale());
		oldEntity.setOperatore(entity.getOperatore());
		oldEntity.setPezzi(entity.getPezzi());
	}

	@Override
	public List<CdgEventoRiepilogo> trovaPerCommessaEData(FiltroEventoRiepilogo filtro) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgEventoRiepilogo> criteria = cb.createQuery(CdgEventoRiepilogo.class);
        Root<CdgEventoRiepilogo> member = criteria.from(CdgEventoRiepilogo.class);
        Predicate condizioneCommessa = cb.equal(member.get("commessa"), filtro.getCommessa());
        Predicate condizioneData = cb.between(member.get("giorno"), filtro.getInizio(), filtro.getFine());
        criteria.select(member).where(cb.and(condizioneCommessa, condizioneData));
		List<CdgEventoRiepilogo> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

}
