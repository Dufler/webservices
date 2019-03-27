package it.ltc.services.clienti.data.magazzino;

import org.springframework.stereotype.Component;

import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.dao.FactoryDao;
import it.ltc.services.custom.exception.CustomException;

@Component
public class SaldiMagazzinoFactory extends FactoryDao<SaldiMagazzinoDAO> {
	
	public static final String PU_CIESSE = "legacy-ciesse";
	public static final String PU_WOMSH = "legacy-womsh";

	@Override
	protected SaldiMagazzinoDAO findDao(UtenteUtenti user, CommessaUtenti commessa) {
		SaldiMagazzinoDAO dao;
		String persistenceUnitName = commessa.getNomeRisorsa();
		if (commessa.isLegacy()) {
			switch (persistenceUnitName) {
				case PU_CIESSE : dao = new SaldiMagazzinoDAOCiesseImpl(persistenceUnitName); break;
				case PU_WOMSH : dao = new SaldiMagazzinoDAOWomshImpl(persistenceUnitName); break;
				default : dao = new SaldiMagazzinoDAOImpl(persistenceUnitName); 
			}
		} else {
			throw new CustomException("Il dao giacenze per i sistemi non-lgeacy non è ancora disponibile.");
		}
		return dao;
	}

}
