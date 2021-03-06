package it.ltc.services.sede.data.prodotto;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.dao.shared.prodotti.ProdottoColtortiDAOImpl;
import it.ltc.database.dao.shared.prodotti.ProdottoDAOImpl;
import it.ltc.database.dao.shared.prodotti.ProdottoLegacyDAOImpl;
import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IProdottoDao;
import it.ltc.services.custom.dao.FactoryDao;

/**
 * Factory per i dao che gestiscono i prodotti.
 * @author Damiano
 *
 */
@Component
public class ProdottoDAOFactory extends FactoryDao<IProdottoDao> {
	
	private static final Logger logger = Logger.getLogger(ProdottoDAOFactory.class);
	
	public static final String PU_COLTORTI = "legacy-coltorti";
	public static final String PU_LEGACY_TEST = "legacy-test";

	@Override
	protected IProdottoDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		logger.debug("Nuova istanza dao per: " + commessa + ", PU: " + persistenceUnitName);
		IProdottoDao dao;
		if (commessa.isLegacy()) {
			switch (persistenceUnitName) {
				case PU_COLTORTI : dao = new ProdottoColtortiDAOImpl(persistenceUnitName, commessa.getId()); break;
				default : dao = new ProdottoLegacyDAOImpl(persistenceUnitName, commessa.getId());
			}
		} else {
			dao = new ProdottoDAOImpl(persistenceUnitName);
		}
		dao.setUtente(user.getUsername());
		return dao;
	}

}
