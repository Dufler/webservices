package it.ltc.services.sede.data.magazzino;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoInventario extends FactoryDao<InventarioDao> {

	@Override
	protected InventarioDao findDao(Utente user, Commessa commessa) {
		InventarioLegacyColtortiDao dao = new InventarioLegacyColtortiDao(commessa.getNomeRisorsa());
		dao.setUtente(user.getUsername());
		return dao;
	}

}
