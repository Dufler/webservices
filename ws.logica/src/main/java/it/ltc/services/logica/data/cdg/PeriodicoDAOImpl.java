package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgPeriodico;

@Repository
public class PeriodicoDAOImpl extends CRUDDao<CdgPeriodico> implements PeriodicoDAO {

	public PeriodicoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgPeriodico.class);
	}

	@Override
	public List<CdgPeriodico> trovaTutte() {
		List<CdgPeriodico> entities = findAll();
		return entities;
	}

	@Override
	public CdgPeriodico trova(int id) {
		CdgPeriodico entity = findByID(id);
		return entity;
	}

	@Override
	public CdgPeriodico inserisci(CdgPeriodico periodico) {
		CdgPeriodico entity = insert(periodico);
		return entity;
	}

	@Override
	public CdgPeriodico aggiorna(CdgPeriodico periodico) {
		CdgPeriodico entity = update(periodico, periodico.getId());
		return entity;
	}

	@Override
	public CdgPeriodico elimina(CdgPeriodico periodico) {
		CdgPeriodico entity = delete(periodico.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgPeriodico oldEntity, CdgPeriodico entity) {
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setNome(entity.getNome());
		oldEntity.setPeriodo(entity.getPeriodo());
		oldEntity.setTipo(entity.getTipo());
		oldEntity.setValore(entity.getValore());
	}

}
