package it.ltc.services.clienti.data.prodotto;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.services.custom.dao.FactoryDao;

/**
 * https://stackoverflow.com/questions/46543172/spring-use-different-bean-as-per-request-value
 * Anche se non è possibile applicarla a causa delle diverse persistence unit non sembra male.
 * In realtà sarebbe possibile se nella configurazione dei bean ne mettessimo uno per persistence unit utilizzata però non mi sembra comodo.
 * @author Damiano
 *
 */
@Component
public class ProdottoDAOFactory extends FactoryDao<ProdottoDAO> {
	
	private static final Logger logger = Logger.getLogger("ProdottoDAOFactory");
	
	public static final String PU_COLTORTI = "legacy-coltorti";
	public static final String PU_LEGACY_TEST = "legacy-test";

	@Override
	protected ProdottoDAO findDao(Commessa commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		logger.info("Nuova istanza dao per: " + commessa + ", PU: " + persistenceUnitName);
		ProdottoDAO dao;
		if (commessa.isLegacy()) {
			switch (persistenceUnitName) {
				case PU_COLTORTI : case PU_LEGACY_TEST : dao = new ProdottoColtortiDAOImpl(persistenceUnitName); break;
				default : dao = new ProdottoLegacyDAOImpl(persistenceUnitName);
			}
		} else {
			dao = new ProdottoDAOImpl(persistenceUnitName);
		}
		return dao;
	}

}
