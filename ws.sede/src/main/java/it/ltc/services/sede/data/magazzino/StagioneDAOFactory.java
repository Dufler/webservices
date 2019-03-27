package it.ltc.services.sede.data.magazzino;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IStagioneDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class StagioneDAOFactory extends FactoryDao<IStagioneDao> {
	
	private static final Logger logger = Logger.getLogger(StagioneDAOFactory.class);

	@Override
	protected IStagioneDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		IStagioneDao dao;
		if (commessa.isLegacy()) {
			dao = new StagioneLegacyDaoImpl(persistenceUnitName);
		} else {
			logger.error("Dao non pronto per " + persistenceUnitName);
			dao = null;
		}
		return dao;
	}

}
