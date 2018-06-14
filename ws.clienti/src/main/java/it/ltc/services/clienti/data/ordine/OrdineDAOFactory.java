package it.ltc.services.clienti.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class OrdineDAOFactory extends FactoryDao<OrdineDAO<?, ?>> {
	
	public static final String PU_COLTORTI = "legacy-coltorti";
	public static final String PU_LEGACY_TEST = "legacy-test";

	@Override
	protected OrdineDAO<?, ?> findDao(Commessa commessa) {
		OrdineDAO<?,?> dao;
		String persistenceUnitName = commessa.getNomeRisorsa();
		if (commessa.isLegacy()) {
			switch (persistenceUnitName) {
				case PU_COLTORTI : /*case PU_LEGACY_TEST :*/ dao = new OrdineColtortiDAOImpl(persistenceUnitName); break;
				default : dao = new OrdineLegacyDAOImpl(persistenceUnitName);
			}
		} else {
			dao = new OrdineDAOImpl(persistenceUnitName);
		}
		return dao;
	}

}
