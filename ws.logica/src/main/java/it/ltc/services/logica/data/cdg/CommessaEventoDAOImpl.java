package it.ltc.services.logica.data.cdg;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCommessaEvento;
import it.ltc.database.model.centrale.CdgCommessaEventoPK;
import it.ltc.database.model.centrale.json.CdgCommessaEventoJSON;

@Repository
public class CommessaEventoDAOImpl extends CRUDDao<CdgCommessaEvento> implements CommessaEventoDAO {

	public CommessaEventoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCommessaEvento.class);
	}

	@Override
	public List<CdgCommessaEventoJSON> trovaTutte() {
		List<CdgCommessaEvento> entities = findAll();
		List<CdgCommessaEventoJSON> jsons = new LinkedList<>();
		for (CdgCommessaEvento entity : entities) {
			CdgCommessaEventoJSON json = serializza(entity);
			jsons.add(json);
		}
		return jsons;
	}

	@Override
	public CdgCommessaEventoJSON trova(int commessa, int evento) {
		CdgCommessaEventoPK key = new CdgCommessaEventoPK();
		key.setCommessa(commessa);
		key.setEvento(evento);
		CdgCommessaEvento entity = findByID(key);
		CdgCommessaEventoJSON json = serializza(entity);
		return json;
	}

	@Override
	public CdgCommessaEventoJSON inserisci(CdgCommessaEventoJSON json) {
		CdgCommessaEvento entity = deserializza(json);
		entity = insert(entity);
		json = serializza(entity);
		return json;
	}

	@Override
	public CdgCommessaEventoJSON aggiorna(CdgCommessaEventoJSON json) {
		CdgCommessaEvento entity = deserializza(json);
		entity = update(entity, entity.getId());
		json = serializza(entity);
		return json;
	}

	@Override
	public CdgCommessaEventoJSON elimina(CdgCommessaEventoJSON json) {
		CdgCommessaEvento entity = deserializza(json);
		entity = delete(entity.getId());
		json = serializza(entity);
		return json;
	}

	@Override
	protected void updateValues(CdgCommessaEvento oldEntity, CdgCommessaEvento entity) {
		oldEntity.setDurata(entity.getDurata());
	}
	
	protected CdgCommessaEvento deserializza(CdgCommessaEventoJSON json) {
		CdgCommessaEvento entity;
		if (json != null) {
			entity = new CdgCommessaEvento();
			CdgCommessaEventoPK key = new CdgCommessaEventoPK();
			key.setCommessa(json.getCommessa());
			key.setEvento(json.getEvento());
			entity.setId(key);
			entity.setDurata(json.getDurata());
		} else {
			entity = null;
		}
		return entity;
	}
	
	protected CdgCommessaEventoJSON serializza(CdgCommessaEvento entity) {
		CdgCommessaEventoJSON json;
		if (entity != null) {
			json = new CdgCommessaEventoJSON();
			json.setCommessa(entity.getId().getCommessa());
			json.setEvento(entity.getId().getEvento());
			json.setDurata(entity.getDurata());
		} else {
			json = null;
		}
		return json;
	}

}
