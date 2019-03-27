package it.ltc.services.clienti.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class CaricoDAOFactory extends FactoryDao<CaricoDAO<?,?>> {

	@Override
	protected CaricoDAO<?,?> findDao(UtenteUtenti user, CommessaUtenti commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		CaricoDAO<?,?> dao = commessa.isLegacy() ? new CaricoLegacyDAOImpl(persistenceUnitName) : new CaricoDAOImpl(persistenceUnitName);
		return dao;
	}

}
