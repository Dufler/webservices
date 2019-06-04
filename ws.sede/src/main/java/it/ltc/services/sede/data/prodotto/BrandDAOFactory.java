package it.ltc.services.sede.data.prodotto;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IBrandDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class BrandDAOFactory extends FactoryDao<IBrandDao> {
	
	private static final Logger logger = Logger.getLogger(BrandDAOFactory.class);

	@Override
	protected IBrandDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		logger.debug("Nuova istanza dao per: " + commessa + ", PU: " + persistenceUnitName);
		//Fissato alla versione legacy, per ora.
		IBrandDao dao = new BrandDAOImpl(persistenceUnitName);
		dao.setUtente(user.getUsername());
		return dao;
	}

}
