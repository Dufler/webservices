package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgEvento;

@Repository
public class EventoDAOImpl extends CRUDDao<CdgEvento> implements EventoDAO {

	public EventoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgEvento.class);
	}

	@Override
	public List<CdgEvento> trovaTutti() {
		List<CdgEvento> entities = findAll();
		return entities;
	}

	@Override
	public CdgEvento trova(int id) {
		CdgEvento entity = findByID(id);
		return entity;
	}

	@Override
	public CdgEvento inserisci(CdgEvento evento) {
		CdgEvento entity = insert(evento);
		return entity;
	}

	@Override
	public CdgEvento aggiorna(CdgEvento evento) {
		CdgEvento entity = update(evento, evento.getId());
		return entity;
	}

	@Override
	public CdgEvento elimina(CdgEvento evento) {
		CdgEvento entity = delete(evento.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgEvento oldEntity, CdgEvento entity) {
		oldEntity.setCategoriaMerceologica(entity.getCategoriaMerceologica());
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setFase(entity.getFase());
		oldEntity.setNome(entity.getNome());
	}

}
