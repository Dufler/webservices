package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IDestinatarioDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoDestinatari extends FactoryDao<IDestinatarioDao> {

	@Override
	protected IDestinatarioDao findDao(Utente user, Commessa commessa) {
		//Lo metto in maniera fissa, per ora.
		DestinatarioLegacyDAOImpl dao = new DestinatarioLegacyDAOImpl(commessa.getNomeRisorsa());
		dao.setUtente(user.getUsername());
		return dao;
	}

}
