package it.ltc.services.logica.data.common;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.Operatore;

@Repository
public class OperatoriDAOImpl extends CRUDDao<Operatore> implements OperatoriDAO {

	public OperatoriDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Operatore.class);
	}

	@Override
	public List<Operatore> trovaTutti() {
		List<Operatore> list = findAll();
		return list;
	}

	@Override
	public Operatore trova(String username) {
		Operatore entity = findByID(username);
		return entity;
	}

	@Override
	public Operatore inserisci(Operatore operatore) {
		Operatore entity = insert(operatore);
		return entity;
	}

	@Override
	public Operatore aggiorna(Operatore operatore) {
		Operatore entity = update(operatore, operatore.getUsername());
		return entity;
	}

	@Override
	public Operatore elimina(Operatore operatore) {
		Operatore entity = delete(operatore.getUsername());
		return entity;
	}

	@Override
	protected void updateValues(Operatore oldEntity, Operatore entity) {
		oldEntity.setAssociazione(entity.getAssociazione());		
	}

}
