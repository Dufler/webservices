package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioniRipetuti;

@Repository
public class VociListinoClienteScaglioniRipetutiDAOImpl extends CRUDDao<ListinoCommessaVoceScaglioniRipetuti> implements VociListinoClienteScaglioniRipetutiDAO {

	public VociListinoClienteScaglioniRipetutiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessaVoceScaglioniRipetuti.class);
	}

	@Override
	public List<ListinoCommessaVoceScaglioniRipetuti> trovaTutte() {
		List<ListinoCommessaVoceScaglioniRipetuti> list = findAll();
		return list;
	}

	@Override
	public ListinoCommessaVoceScaglioniRipetuti trova(int id) {
		ListinoCommessaVoceScaglioniRipetuti entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCommessaVoceScaglioniRipetuti inserisci(ListinoCommessaVoceScaglioniRipetuti voce) {
		ListinoCommessaVoceScaglioniRipetuti entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCommessaVoceScaglioniRipetuti aggiorna(ListinoCommessaVoceScaglioniRipetuti voce) {
		ListinoCommessaVoceScaglioniRipetuti entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCommessaVoceScaglioniRipetuti elimina(ListinoCommessaVoceScaglioniRipetuti voce) {
		ListinoCommessaVoceScaglioniRipetuti entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessaVoceScaglioniRipetuti oldEntity,	ListinoCommessaVoceScaglioniRipetuti entity) {
		oldEntity.setIntervallo(entity.getIntervallo());
		oldEntity.setMassimo(entity.getMassimo());
		oldEntity.setMinimo(entity.getMinimo());
		oldEntity.setValore(entity.getValore());		
	}

}
