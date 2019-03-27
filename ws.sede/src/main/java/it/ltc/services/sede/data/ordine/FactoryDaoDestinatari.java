package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IDestinatarioDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoDestinatari extends FactoryDao<IDestinatarioDao> {

	@Override
	protected IDestinatarioDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		//Lo metto in maniera fissa, per ora.
		DestinatarioLegacyDAOImpl dao = new DestinatarioLegacyDAOImpl(commessa.getNomeRisorsa());
		dao.setUtente(user.getUsername());
		return dao;
	}

}
