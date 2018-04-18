package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessaVocePercentuale;

@Repository
public class VociListinoClientePercentualeDAOImpl extends CRUDDao<ListinoCommessaVocePercentuale> implements VociListinoClientePercentualeDAO {

	public VociListinoClientePercentualeDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessaVocePercentuale.class);
	}

	@Override
	public List<ListinoCommessaVocePercentuale> trovaTutte() {
		List<ListinoCommessaVocePercentuale> list = findAll();
		return list;
	}

	@Override
	public ListinoCommessaVocePercentuale trova(int id) {
		ListinoCommessaVocePercentuale entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCommessaVocePercentuale inserisci(ListinoCommessaVocePercentuale voce) {
		ListinoCommessaVocePercentuale entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCommessaVocePercentuale aggiorna(ListinoCommessaVocePercentuale voce) {
		ListinoCommessaVocePercentuale entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCommessaVocePercentuale elimina(ListinoCommessaVocePercentuale voce) {
		ListinoCommessaVocePercentuale entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessaVocePercentuale oldEntity, ListinoCommessaVocePercentuale entity) {
		oldEntity.setValore(entity.getValore());
		oldEntity.setValoreMassimo(entity.getValoreMassimo());
		oldEntity.setValoreMinimo(entity.getValoreMinimo());
	}

}
