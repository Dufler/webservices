package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IMittenteDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoMittenti extends FactoryDao<IMittenteDao> {

	@Override
	protected IMittenteDao findDao(UtenteUtenti user, CommessaUtenti commessa) {
		//Lo metto fisso, per ora.
		MittenteLegacyDAOImpl dao = new MittenteLegacyDAOImpl(commessa.getNomeRisorsa(), user.getUsername());
		return dao;
	}

}
