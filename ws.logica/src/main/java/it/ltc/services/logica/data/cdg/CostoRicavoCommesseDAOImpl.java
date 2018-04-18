package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCostiRicaviCommesse;

@Repository
public class CostoRicavoCommesseDAOImpl extends CRUDDao<CdgCostiRicaviCommesse> implements CostoRicavoCommesseDAO {

	public CostoRicavoCommesseDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCostiRicaviCommesse.class);
	}

	@Override
	public List<CdgCostiRicaviCommesse> trovaTutti() {
		List<CdgCostiRicaviCommesse> entities = findAll();
		return entities;
	}

	@Override
	public CdgCostiRicaviCommesse trova(int id) {
		CdgCostiRicaviCommesse entity = findByID(id);
		return entity;
	}

	@Override
	public CdgCostiRicaviCommesse inserisci(CdgCostiRicaviCommesse bilancio) {
		CdgCostiRicaviCommesse entity = insert(bilancio);
		return entity;
	}

	@Override
	public CdgCostiRicaviCommesse aggiorna(CdgCostiRicaviCommesse bilancio) {
		CdgCostiRicaviCommesse entity = update(bilancio, bilancio.getId());
		return entity;
	}

	@Override
	public CdgCostiRicaviCommesse elimina(CdgCostiRicaviCommesse bilancio) {
		CdgCostiRicaviCommesse entity = delete(bilancio.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgCostiRicaviCommesse oldEntity, CdgCostiRicaviCommesse entity) {
		oldEntity.setCommessa(entity.getCommessa());
		oldEntity.setDataEmissione(entity.getDataEmissione());
		oldEntity.setTipo(entity.getTipo());
		oldEntity.setValore(entity.getValore());
	}

}
