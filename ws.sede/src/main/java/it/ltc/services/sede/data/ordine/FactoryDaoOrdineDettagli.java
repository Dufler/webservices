package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IOrdineDettaglioDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoOrdineDettagli extends FactoryDao<IOrdineDettaglioDao> {

	@Override
	protected IOrdineDettaglioDao findDao(Utente user, Commessa commessa) {
		OrdineDettaglioLegacyDAOImpl daoLegacy = new OrdineDettaglioLegacyDAOImpl(commessa.getNomeRisorsa());
		daoLegacy.setUtente(user.getUsername());
		//Per ora ho solo quella legacy
		IOrdineDettaglioDao dao = daoLegacy;
		return dao;
	}

}
