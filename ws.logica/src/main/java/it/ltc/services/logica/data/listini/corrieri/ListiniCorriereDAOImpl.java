package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriere;

@Repository
public class ListiniCorriereDAOImpl extends CRUDDao<ListinoCorriere> implements ListiniCorriereDAO {

	public ListiniCorriereDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriere.class);
	}

	@Override
	public List<ListinoCorriere> trovaTutti() {
		List<ListinoCorriere> list = findAll();
		return list;
	}

	@Override
	public ListinoCorriere trova(int id) {
		ListinoCorriere entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCorriere inserisci(ListinoCorriere listino) {
		ListinoCorriere entity = insert(listino);
		return entity;
	}

	@Override
	public ListinoCorriere aggiorna(ListinoCorriere listino) {
		ListinoCorriere entity = update(listino, listino.getId());
		return entity;
	}

	@Override
	public ListinoCorriere elimina(ListinoCorriere listino) {
		ListinoCorriere entity = delete(listino.getId());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriere oldEntity, ListinoCorriere entity) {
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setIdCorriere(entity.getIdCorriere());
		oldEntity.setNome(entity.getNome());
		oldEntity.setTipo(entity.getTipo());
	}

}
