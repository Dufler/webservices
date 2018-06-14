package it.ltc.services.sede.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoCarico extends FactoryDao<CaricoDAO> {

	@Override
	protected CaricoDAO findDao(Commessa commessa) {
		CaricoDAO dao = commessa.isLegacy() ? new CaricoLegacyDAOImpl(commessa.getNomeRisorsa()) : new CaricoDAOImpl(commessa.getNomeRisorsa());
		return dao;
	}

}
