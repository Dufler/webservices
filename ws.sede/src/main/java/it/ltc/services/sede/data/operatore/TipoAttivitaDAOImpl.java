package it.ltc.services.sede.data.operatore;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.ReadOnlyDao;
import it.ltc.database.model.sede.AttivitaTipo;

@Repository
public class TipoAttivitaDAOImpl extends ReadOnlyDao<AttivitaTipo> implements TipoAttivitaDAO {

	public TipoAttivitaDAOImpl() {
		super(LOCAL_SEDE_PERSISTENCE_UNIT_NAME, AttivitaTipo.class);
	}

	@Override
	public AttivitaTipo trovaDaID(int id) {
		AttivitaTipo entity = findByID(id);
		return entity;
	}

	@Override
	public List<AttivitaTipo> trovaTutti() {
		List<AttivitaTipo> entities = findAll();
		return entities;
	}

}
