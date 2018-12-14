package it.ltc.services.clienti.data.fornitore;

import org.springframework.stereotype.Component;

import it.ltc.database.dao.shared.fornitori.FornitoreDAOImpl;
import it.ltc.database.dao.shared.fornitori.FornitoreLegacyDAOImpl;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IFornitoreDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FornitoreDAOFactory extends FactoryDao<IFornitoreDao> {

	@Override
	protected IFornitoreDao findDao(Utente user, Commessa commessa) {
		String persistenceUnitName = commessa.getNomeRisorsa();
		IFornitoreDao dao = commessa.isLegacy() ? new FornitoreLegacyDAOImpl(persistenceUnitName) : new FornitoreDAOImpl(persistenceUnitName);
		return dao;
	}

}
