package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriereVocePercentuale;

@Repository
public class VociListinoCorrierePercentualeDAOImpl extends CRUDDao<ListinoCorriereVocePercentuale> implements VociListinoCorrierePercentualeDAO {

	public VociListinoCorrierePercentualeDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriereVocePercentuale.class);
	}

	@Override
	public List<ListinoCorriereVocePercentuale> trovaTutte() {
		List<ListinoCorriereVocePercentuale> list = findAll();
		return list;
	}

	@Override
	public ListinoCorriereVocePercentuale trova(int id) {
		ListinoCorriereVocePercentuale entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCorriereVocePercentuale inserisci(ListinoCorriereVocePercentuale voce) {
		ListinoCorriereVocePercentuale entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCorriereVocePercentuale aggiorna(ListinoCorriereVocePercentuale voce) {
		ListinoCorriereVocePercentuale entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCorriereVocePercentuale elimina(ListinoCorriereVocePercentuale voce) {
		ListinoCorriereVocePercentuale entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriereVocePercentuale oldEntity, ListinoCorriereVocePercentuale entity) {
		oldEntity.setValore(entity.getValore());
		oldEntity.setValoreMassimo(entity.getValoreMassimo());
		oldEntity.setValoreMinimo(entity.getValoreMinimo());
	}

}
