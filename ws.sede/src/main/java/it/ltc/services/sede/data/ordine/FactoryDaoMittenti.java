package it.ltc.services.sede.data.ordine;

import org.springframework.stereotype.Component;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IMittenteDao;
import it.ltc.services.custom.dao.FactoryDao;

@Component
public class FactoryDaoMittenti extends FactoryDao<IMittenteDao> {

	@Override
	protected IMittenteDao findDao(Utente user, Commessa commessa) {
		//Lo metto fisso, per ora.
		MittenteLegacyDAOImpl dao = new MittenteLegacyDAOImpl(commessa.getNomeRisorsa(), user.getUsername());
		return dao;
	}

}
