package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessaVoceFissa;

@Repository
public class VociListinoClienteFissaDAOImpl extends CRUDDao<ListinoCommessaVoceFissa>	implements VociListinoClienteFissaDAO {


	public VociListinoClienteFissaDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessaVoceFissa.class);
	}

	@Override
	public List<ListinoCommessaVoceFissa> trovaTutte() {
		List<ListinoCommessaVoceFissa> list = findAll();
		return list;
	}
	
	@Override
	public ListinoCommessaVoceFissa trova(int id) {
		ListinoCommessaVoceFissa entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCommessaVoceFissa inserisci(ListinoCommessaVoceFissa voce) {
		ListinoCommessaVoceFissa entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCommessaVoceFissa aggiorna(ListinoCommessaVoceFissa voce) {
		ListinoCommessaVoceFissa entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCommessaVoceFissa elimina(ListinoCommessaVoceFissa voce) {
		ListinoCommessaVoceFissa entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessaVoceFissa oldEntity, ListinoCommessaVoceFissa entity) {
		oldEntity.setValore(entity.getValore());
	}

}
