package it.ltc.services.logica.data.cdg;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgCostoAssociazione;

@Repository
public class CostoAssociazioneDAOImpl extends CRUDDao<CdgCostoAssociazione> implements CostoAssociazioneDAO {

	public CostoAssociazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgCostoAssociazione.class);
	}

	@Override
	public List<CdgCostoAssociazione> trovaTutte() {
		List<CdgCostoAssociazione> entities = findAll();
		return entities;
	}

	@Override
	public CdgCostoAssociazione trova(int id) {
		CdgCostoAssociazione entity = findByID(id);
		return entity;
	}

	@Override
	public CdgCostoAssociazione inserisci(CdgCostoAssociazione costo) {
		CdgCostoAssociazione entity = insert(costo);
		return entity;
	}

	@Override
	public CdgCostoAssociazione aggiorna(CdgCostoAssociazione costo) {
		CdgCostoAssociazione entity = update(costo, costo.getId());
		return entity;
	}

	@Override
	public CdgCostoAssociazione elimina(CdgCostoAssociazione costo) {
		CdgCostoAssociazione entity = delete(costo.getId());
		return entity;
	}

	@Override
	protected void updateValues(CdgCostoAssociazione oldEntity, CdgCostoAssociazione entity) {
		oldEntity.setCosto(entity.getCosto());
		oldEntity.setNome(entity.getNome());
		oldEntity.setSede(entity.getSede());
	}

}
