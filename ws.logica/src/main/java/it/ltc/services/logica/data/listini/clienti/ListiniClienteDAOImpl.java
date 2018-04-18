package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessa;

@Repository
public class ListiniClienteDAOImpl extends CRUDDao<ListinoCommessa> implements ListiniClienteDAO {

	public ListiniClienteDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessa.class);
	}

	@Override
	public List<ListinoCommessa> trovaTutti() {
		List<ListinoCommessa> list = findAll();
		return list;
	}

	@Override
	public ListinoCommessa trova(int id) {
		ListinoCommessa entity = findByID(id);
		return entity;
	}

	@Override
	public ListinoCommessa inserisci(ListinoCommessa listino) {
		ListinoCommessa entity = insert(listino);
		return entity;
	}

	@Override
	public ListinoCommessa aggiorna(ListinoCommessa listino) {
		ListinoCommessa entity = update(listino, listino.getId());
		return entity;
	}

	@Override
	public ListinoCommessa elimina(ListinoCommessa listino) {
		ListinoCommessa entity = delete(listino.getId());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessa oldEntity, ListinoCommessa entity) {
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setIdCommessa(entity.getIdCommessa());
		oldEntity.setNome(entity.getNome());
		oldEntity.setTipo(entity.getTipo());
	}

}
