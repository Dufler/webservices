package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriereVoceFissa;

@Repository
public class VociListinoCorriereFissaDAOImpl extends CRUDDao<ListinoCorriereVoceFissa>	implements VociListinoCorriereFissaDAO {


	public VociListinoCorriereFissaDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriereVoceFissa.class);
	}

	@Override
	public List<ListinoCorriereVoceFissa> trovaTutte() {
		List<ListinoCorriereVoceFissa> list = findAll();
		return list;
	}
	
	@Override
	public ListinoCorriereVoceFissa trova(int id) {
		ListinoCorriereVoceFissa entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCorriereVoceFissa inserisci(ListinoCorriereVoceFissa voce) {
		ListinoCorriereVoceFissa entity = insert(voce);
		return entity;
	}

	@Override
	public ListinoCorriereVoceFissa aggiorna(ListinoCorriereVoceFissa voce) {
		ListinoCorriereVoceFissa entity = update(voce, voce.getIdVoce());
		return entity;
	}

	@Override
	public ListinoCorriereVoceFissa elimina(ListinoCorriereVoceFissa voce) {
		ListinoCorriereVoceFissa entity = delete(voce.getIdVoce());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriereVoceFissa oldEntity, ListinoCorriereVoceFissa entity) {
		oldEntity.setValore(entity.getValore());
	}

}
