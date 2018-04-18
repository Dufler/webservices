package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CoordinateBancarie;

@Repository
public class CoordinateBancarieDAOImpl extends CRUDDao<CoordinateBancarie> implements CoordinateBancarieDAO {

	public CoordinateBancarieDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CoordinateBancarie.class);
	}

	@Override
	public List<CoordinateBancarie> trovaTutti() {
		List<CoordinateBancarie> entities = findAll();
		return entities;
	}

	@Override
	public CoordinateBancarie trova(int id) {
		CoordinateBancarie entity = findByID(id);
		return entity;
	}

	@Override
	public CoordinateBancarie inserisci(CoordinateBancarie coordinate) {
		CoordinateBancarie entity = insert(coordinate);
		return entity;
	}

	@Override
	public CoordinateBancarie aggiorna(CoordinateBancarie coordinate) {
		CoordinateBancarie entity = update(coordinate, coordinate.getId());
		return entity;
	}

	@Override
	public CoordinateBancarie elimina(CoordinateBancarie coordinate) {
		CoordinateBancarie entity = delete(coordinate.getId());
		return entity;
	}

	@Override
	protected void updateValues(CoordinateBancarie oldEntity, CoordinateBancarie entity) {
		oldEntity.setNome(entity.getNome());
		oldEntity.setCoordinate(entity.getCoordinate());
		oldEntity.setEnte(entity.getEnte());
	}

}
