package it.ltc.services.sede.data.prodotto;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class SaldiMovimentiDAOFactory extends FactoryDao<SaldiMovimentiDAO> {
	
	private static final Logger logger = Logger.getLogger("ProdottoDAOFactory");
	
	@Override
	protected SaldiMovimentiDAO findDao(UtenteUtenti user, CommessaUtenti commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		logger.info("Nuova istanza dao per: " + commessa + ", PU: " + persistenceUnitName);
		SaldiMovimentiDAO dao;
		if (commessa.isLegacy()) {
			dao = new SaldiMovimentiLegacyDAOImpl(persistenceUnitName);
		} else {
			dao = new SaldiMovimentiDAOImpl(persistenceUnitName);
		}
		dao.setUtente(user.getUsername());
		return dao;
	}

}
