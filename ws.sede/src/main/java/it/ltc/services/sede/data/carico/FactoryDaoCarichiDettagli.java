package it.ltc.services.sede.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.model.shared.dao.ICaricoDettaglioDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoCarichiDettagli extends FactoryDao<ICaricoDettaglioDao> {

	@Override
	protected ICaricoDettaglioDao findDao(Commessa commessa) {
		ICaricoDettaglioDao dao = new CaricoDettagliLegacyDAOImpl(commessa.getNomeRisorsa());
		return dao;
	}

}
