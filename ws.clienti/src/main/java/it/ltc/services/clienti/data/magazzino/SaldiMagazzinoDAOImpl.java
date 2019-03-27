package it.ltc.services.clienti.data.magazzino;

import it.ltc.database.dao.shared.magazzino.GiacenzeDao;

/**
 * Implementazione vanilla per la restituzione delle giacenze di magazzino.
 * @author Damiano
 *
 */
public class SaldiMagazzinoDAOImpl extends GiacenzeDao implements SaldiMagazzinoDAO {

	public SaldiMagazzinoDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

}
