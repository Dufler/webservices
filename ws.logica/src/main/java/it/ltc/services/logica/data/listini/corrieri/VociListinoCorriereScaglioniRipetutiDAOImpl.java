package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioniRipetuti;

@Repository
public class VociListinoCorriereScaglioniRipetutiDAOImpl extends CRUDDao<ListinoCorriereVoceScaglioniRipetuti> implements VociListinoCorriereScaglioniRipetutiDAO {

	public VociListinoCorriereScaglioniRipetutiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriereVoceScaglioniRipetuti.class);
	}

	@Override
	public List<ListinoCorriereVoceScaglioniRipetuti> trovaTutte() {
		List<ListinoCorriereVoceScaglioniRipetuti> list = findAll();
		return list;
	}

	@Override
	public ListinoCorriereVoceScaglioniRipetuti trova(int id) {
		ListinoCorriereVoceScaglioniRipetuti entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCorriereVoceScaglioniRipetuti inserisci(ListinoCorriereVoceScaglioniRipetuti voce) {
		ListinoCorriereVoceScaglioniRipetuti entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCorriereVoceScaglioniRipetuti aggiorna(ListinoCorriereVoceScaglioniRipetuti voce) {
		ListinoCorriereVoceScaglioniRipetuti entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCorriereVoceScaglioniRipetuti elimina(ListinoCorriereVoceScaglioniRipetuti voce) {
		ListinoCorriereVoceScaglioniRipetuti entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriereVoceScaglioniRipetuti oldEntity,	ListinoCorriereVoceScaglioniRipetuti entity) {
		oldEntity.setIntervallo(entity.getIntervallo());
		oldEntity.setMassimo(entity.getMassimo());
		oldEntity.setMinimo(entity.getMinimo());
		oldEntity.setValore(entity.getValore());		
	}

}
