package it.ltc.services.logica.data.crm;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CrmContattoRecapiti;

@Repository
public class ContattoRecapitiDAOImpl extends CRUDDao<CrmContattoRecapiti> implements ContattoRecapitiDAO {

	public ContattoRecapitiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CrmContattoRecapiti.class);
	}

	@Override
	public List<CrmContattoRecapiti> trovaTuttiDaContatto(int idContatto) {
		List<CrmContattoRecapiti> entities = findAllEqualTo("contatto", idContatto);
		return entities;
	}

	@Override
	public CrmContattoRecapiti trovaDaID(int idRecapito) {
		CrmContattoRecapiti entity = findByID(idRecapito);
		return entity;
	}

	@Override
	public CrmContattoRecapiti inserisci(CrmContattoRecapiti recapito) {
		CrmContattoRecapiti entity = insert(recapito);
		return entity;
	}

	@Override
	public CrmContattoRecapiti aggiorna(CrmContattoRecapiti recapito) {
		CrmContattoRecapiti entity = update(recapito, recapito.getId());
		return entity;
	}

	@Override
	public CrmContattoRecapiti elimina(CrmContattoRecapiti recapito) {
		CrmContattoRecapiti entity = delete(recapito.getId());
		return entity;
	}

	@Override
	protected void updateValues(CrmContattoRecapiti oldEntity, CrmContattoRecapiti entity) {
		oldEntity.setContatto(entity.getContatto());
		oldEntity.setRecapito(entity.getRecapito());
		oldEntity.setTipo(entity.getTipo());
	}

}
