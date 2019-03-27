package it.ltc.services.sede.data.carico;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.ICaricoDettaglioDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoCarichiDettagli extends FactoryDao<ICaricoDettaglioDao> {

	@Override
	protected ICaricoDettaglioDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		CaricoDettagliLegacyDAOImpl daoDettagliLegacy = new CaricoDettagliLegacyDAOImpl(commessa.getNomeRisorsa());
		daoDettagliLegacy.setUtente(user.getUsername());
		//Per ora ho solo la versione legacy
		ICaricoDettaglioDao dao = daoDettagliLegacy;
		return dao;
	}

}
