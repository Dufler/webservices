package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessaVoceProporzionale;

@Repository
public class VociListinoClienteProporzionaleDAOImpl extends CRUDDao<ListinoCommessaVoceProporzionale> implements VociListinoClienteProporzionaleDAO {

	public VociListinoClienteProporzionaleDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessaVoceProporzionale.class);
	}

	@Override
	public List<ListinoCommessaVoceProporzionale> trovaTutte() {
		List<ListinoCommessaVoceProporzionale> list = findAll();
		return list;
	}

	@Override
	public ListinoCommessaVoceProporzionale trova(int id) {
		ListinoCommessaVoceProporzionale entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCommessaVoceProporzionale inserisci(ListinoCommessaVoceProporzionale voce) {
		ListinoCommessaVoceProporzionale entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCommessaVoceProporzionale aggiorna(ListinoCommessaVoceProporzionale voce) {
		ListinoCommessaVoceProporzionale entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCommessaVoceProporzionale elimina(ListinoCommessaVoceProporzionale voce) {
		ListinoCommessaVoceProporzionale entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessaVoceProporzionale oldEntity, ListinoCommessaVoceProporzionale entity) {
		oldEntity.setMassimo(entity.getMassimo());
		oldEntity.setMinimo(entity.getMinimo());
		oldEntity.setValore(entity.getValore());		
	}

}
