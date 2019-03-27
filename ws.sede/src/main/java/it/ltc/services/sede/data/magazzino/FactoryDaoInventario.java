package it.ltc.services.sede.data.magazzino;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoInventario extends FactoryDao<InventarioDao> {

	@Override
	protected InventarioDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		InventarioLegacyColtortiDao dao = new InventarioLegacyColtortiDao(commessa.getNomeRisorsa());
		dao.setUtente(user.getUsername());
		return dao;
	}

}
