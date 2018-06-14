package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCostiRicaviGenerici;

@Repository
public class CostoRicavoGenericoDAOImpl extends CRUDDao<CdgCostiRicaviGenerici> implements CostoRicavoGenericoDAO {

	public CostoRicavoGenericoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCostiRicaviGenerici.class);
	}

	@Override
	public List<CdgCostiRicaviGenerici> trovaTutti() {
		List<CdgCostiRicaviGenerici> entities = findAll();
		return entities;
	}

	@Override
	public CdgCostiRicaviGenerici trova(int id) {
		CdgCostiRicaviGenerici entity = findByID(id);
		return entity;
	}

	@Override
	public CdgCostiRicaviGenerici inserisci(CdgCostiRicaviGenerici bilancio) {
		CdgCostiRicaviGenerici entity = insert(bilancio);
		return entity;
	}

	@Override
	public CdgCostiRicaviGenerici aggiorna(CdgCostiRicaviGenerici bilancio) {
		CdgCostiRicaviGenerici entity = update(bilancio, bilancio.getId());
		return entity;
	}

	@Override
	public CdgCostiRicaviGenerici elimina(CdgCostiRicaviGenerici bilancio) {
		CdgCostiRicaviGenerici entity = delete(bilancio.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgCostiRicaviGenerici oldEntity, CdgCostiRicaviGenerici entity) {
		oldEntity.setDriver(entity.getDriver());
		oldEntity.setNome(entity.getNome());
	}

}
