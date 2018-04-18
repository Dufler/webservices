package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriereVoceProporzionale;

@Repository
public class VociListinoCorriereProporzionaleDAOImpl extends CRUDDao<ListinoCorriereVoceProporzionale> implements VociListinoCorriereProporzionaleDAO {

	public VociListinoCorriereProporzionaleDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriereVoceProporzionale.class);
	}

	@Override
	public List<ListinoCorriereVoceProporzionale> trovaTutte() {
		List<ListinoCorriereVoceProporzionale> list = findAll();
		return list;
	}

	@Override
	public ListinoCorriereVoceProporzionale trova(int id) {
		ListinoCorriereVoceProporzionale entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCorriereVoceProporzionale inserisci(ListinoCorriereVoceProporzionale voce) {
		ListinoCorriereVoceProporzionale entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCorriereVoceProporzionale aggiorna(ListinoCorriereVoceProporzionale voce) {
		ListinoCorriereVoceProporzionale entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCorriereVoceProporzionale elimina(ListinoCorriereVoceProporzionale voce) {
		ListinoCorriereVoceProporzionale entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriereVoceProporzionale oldEntity, ListinoCorriereVoceProporzionale entity) {
		oldEntity.setMassimo(entity.getMassimo());
		oldEntity.setMinimo(entity.getMinimo());
		oldEntity.setValore(entity.getValore());		
	}

}
