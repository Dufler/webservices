package it.ltc.services.sede.data.magazzino;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IMagazzinoDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class MagazzinoDAOFactory extends FactoryDao<IMagazzinoDao> {
	
private static final Logger logger = Logger.getLogger("ProdottoDAOFactory");
	
	public static final String PU_COLTORTI = "legacy-coltorti";
	public static final String PU_LEGACY_TEST = "legacy-test";

	@Override
	protected IMagazzinoDao findDao(Utente user, Commessa commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		logger.info("Nuova istanza dao per: " + commessa + ", PU: " + persistenceUnitName);
		IMagazzinoDao dao;
		if (commessa.isLegacy()) {
			dao = new MagazzinoLegacyDaoImpl(persistenceUnitName);
		} else {
			dao = null; //FIXME - Va implementato per il nuovo.
		}
		return dao;
	}

}
