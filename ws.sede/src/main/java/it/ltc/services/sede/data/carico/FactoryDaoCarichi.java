package it.ltc.services.sede.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.ICaricoDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoCarichi extends FactoryDao<ICaricoDao> {

	@Override
	protected ICaricoDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		CarichiLegacyDAOImpl daoLegacy = new CarichiLegacyDAOImpl(commessa.getNomeRisorsa());
		daoLegacy.setUtente(user.getUsername());
		//FIXME - Qui viene inserita in maniera fissa la versione legacy.
		ICaricoDao dao = daoLegacy;
		return dao;
	}

}
