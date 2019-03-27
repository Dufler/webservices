package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IOrdineDettaglioDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoOrdineDettagli extends FactoryDao<IOrdineDettaglioDao> {

	@Override
	protected IOrdineDettaglioDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		OrdineDettaglioLegacyDAOImpl daoLegacy = new OrdineDettaglioLegacyDAOImpl(commessa.getNomeRisorsa());
		daoLegacy.setUtente(user.getUsername());
		//Per ora ho solo quella legacy
		IOrdineDettaglioDao dao = daoLegacy;
		return dao;
	}

}
