package it.ltc.services.clienti.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class CaricoDAOFactory extends FactoryDao<CaricoDAO<?,?>> {

	@Override
	protected CaricoDAO<?,?> findDao(Utente user, Commessa commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		CaricoDAO<?,?> dao = commessa.isLegacy() ? new CaricoLegacyDAOImpl(persistenceUnitName) : new CaricoDAOImpl(persistenceUnitName);
		return dao;
	}

}
