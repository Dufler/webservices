package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.ISpedizioneDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoSpedizioni extends FactoryDao<ISpedizioneDao> {

	@Override
	protected ISpedizioneDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		ISpedizioneDao dao;
		SpedizioneLegacyDao daoLegacy = new SpedizioneLegacyDao(commessa.getNomeRisorsa());
		daoLegacy.setUtente(user.getUsername());
		//FIXME - Qui viene inserita in maniera fissa la versione legacy.
		dao = daoLegacy;
		return dao;
	}

}
