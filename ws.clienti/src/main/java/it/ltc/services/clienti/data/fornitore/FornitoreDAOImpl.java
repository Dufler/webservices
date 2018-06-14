package it.ltc.services.clienti.data.fornitore;

import java.util.List;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.commessa.Fornitore;
import it.ltc.model.shared.dao.IFornitoreDao;
import it.ltc.model.shared.json.cliente.FornitoreJSON;

public class FornitoreDAOImpl extends Dao implements IFornitoreDao {
	
	private static final Logger logger = Logger.getLogger("FornitoreDAOImpl");

	public FornitoreDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		logger.info("Istanziato DAO Fornitori");
	}

	@Override
	public FornitoreJSON trovaDaID(int idFornitore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FornitoreJSON> trovaTutti() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FornitoreJSON inserisci(FornitoreJSON fornitore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FornitoreJSON aggiorna(FornitoreJSON fornitore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FornitoreJSON elimina(FornitoreJSON fornitore) {
		// TODO Auto-generated method stub
		return null;
	}

	public Fornitore deserializza(FornitoreJSON json) {
		// TODO Auto-generated method stub
		return null;
	}

	public FornitoreJSON serializza(Fornitore prodotto) {
		// TODO Auto-generated method stub
		return null;
	}

}
