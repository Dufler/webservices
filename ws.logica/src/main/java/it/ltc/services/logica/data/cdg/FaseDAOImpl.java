package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgFase;

@Repository
public class FaseDAOImpl extends CRUDDao<CdgFase> implements FaseDAO {

	public FaseDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgFase.class);
	}

	@Override
	public List<CdgFase> trovaTutte() {
		List<CdgFase> entities = findAll();
		return entities;
	}

	@Override
	public CdgFase trova(int id) {
		CdgFase entity = findByID(id);
		return entity;
	}

	@Override
	public CdgFase inserisci(CdgFase fase) {
		CdgFase entity = insert(fase);
		return entity;
	}

	@Override
	public CdgFase aggiorna(CdgFase fase) {
		CdgFase entity = update(fase, fase.getId());
		return entity;
	}

	@Override
	public CdgFase elimina(CdgFase fase) {
		CdgFase entity = delete(fase.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgFase oldEntity, CdgFase entity) {
		oldEntity.setNome(entity.getNome());
		oldEntity.setDescrizione(entity.getDescrizione());
	}

}
