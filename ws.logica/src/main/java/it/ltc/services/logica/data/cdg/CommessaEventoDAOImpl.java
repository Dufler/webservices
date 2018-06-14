package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCommessaEvento;
import it.ltc.database.model.centrale.CdgCommessaEventoPK;

@Repository
public class CommessaEventoDAOImpl extends CRUDDao<CdgCommessaEvento> implements CommessaEventoDAO {

	public CommessaEventoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCommessaEvento.class);
	}

	@Override
	public List<CdgCommessaEvento> trovaTutte() {
		List<CdgCommessaEvento> entities = findAll();
		return entities;
	}

	@Override
	public CdgCommessaEvento trova(int commessa, int evento) {
		CdgCommessaEventoPK key = new CdgCommessaEventoPK();
		key.setCommessa(commessa);
		key.setEvento(evento);
		CdgCommessaEvento entity = findByID(key);
		return entity;
	}

	@Override
	public CdgCommessaEvento inserisci(CdgCommessaEvento commessaEvento) {
		CdgCommessaEvento entity = insert(commessaEvento);
		return entity;
	}

	@Override
	public CdgCommessaEvento aggiorna(CdgCommessaEvento commessaEvento) {
		CdgCommessaEvento entity = update(commessaEvento, commessaEvento.getPK());
		return entity;
	}

	@Override
	public CdgCommessaEvento elimina(CdgCommessaEvento commessaEvento) {
		CdgCommessaEvento entity = delete(commessaEvento.getPK());
		return entity;
	}

	@Override
	protected void updateValues(CdgCommessaEvento oldEntity, CdgCommessaEvento entity) {
		oldEntity.setDurata(entity.getDurata());
	}

}
