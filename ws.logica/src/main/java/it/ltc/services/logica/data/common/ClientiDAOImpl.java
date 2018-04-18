package it.ltc.services.logica.data.common;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.Cliente;

@Repository
public class ClientiDAOImpl extends CRUDDao<Cliente> implements ClientiDAO {

	public ClientiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Cliente.class);
	}

	@Override
	public List<Cliente> trovaTutti() {
		List<Cliente> entities = findAll();
		return entities;
	}

	@Override
	public Cliente trova(int id) {
		Cliente entity = findByID(id);
		return entity;
	}

	@Override
	public Cliente inserisci(Cliente cliente) {
		Cliente entity = insert(cliente);
		return entity;
	}

	@Override
	public Cliente aggiorna(Cliente cliente) {
		Cliente entity = update(cliente, cliente.getId());
		return entity;
	}

	@Override
	public Cliente elimina(Cliente cliente) {
		Cliente entity = delete(cliente.getId());
		return entity;
	}

	@Override
	protected void updateValues(Cliente oldEntity, Cliente entity) {
		oldEntity.setIndirizzo(entity.getIndirizzo());
		oldEntity.setPartitaIva(entity.getPartitaIva());
		oldEntity.setRagioneSociale(entity.getRagioneSociale());
		oldEntity.setCodice(entity.getCodice());
	}

}
