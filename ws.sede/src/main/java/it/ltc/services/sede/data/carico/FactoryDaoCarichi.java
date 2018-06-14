package it.ltc.services.sede.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.model.shared.dao.ICaricoDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoCarichi extends FactoryDao<ICaricoDao> {

	@Override
	protected ICaricoDao findDao(Commessa commessa) {
		ICaricoDao dao = new CarichiLegacyDAOImpl(commessa.getNomeRisorsa());
		return dao;
	}

}
