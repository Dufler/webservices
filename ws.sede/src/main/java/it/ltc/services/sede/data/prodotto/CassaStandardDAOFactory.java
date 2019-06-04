package it.ltc.services.sede.data.prodotto;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.ICassaStandardDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class CassaStandardDAOFactory extends FactoryDao<ICassaStandardDao> {
	
	private static final Logger logger = Logger.getLogger(CassaStandardDAOFactory.class);

	@Override
	protected ICassaStandardDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		ICassaStandardDao dao;
		String persistenceUnitName = commessa.getNomeRisorsa();
		logger.debug("Nuova istanza dao per: " + commessa + ", PU: " + persistenceUnitName);
		if (commessa.isLegacy()) {
			dao = new CassaStandardLegacyDAOImpl(persistenceUnitName);
			dao.setUtente(user.getUsername());
		} else {
			dao = null; //FIXME - Va fatto anche per il nuovo.
		}
		return dao;
	}

}
