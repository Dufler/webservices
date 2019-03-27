package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IOrdineDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoOrdini extends FactoryDao<IOrdineDao> {

	@Override
	protected IOrdineDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		OrdineLegacyDAOImpl daoLegacy = new OrdineLegacyDAOImpl(commessa.getNomeRisorsa());
		daoLegacy.setUtente(user.getUsername());
		//FIXME - Qui viene inserita in maniera fissa la versione legacy.
		IOrdineDao dao = daoLegacy;
		return dao;
	}

}
