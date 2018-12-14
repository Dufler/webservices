package it.ltc.services.logica.validation.trasporti;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.common.CodiceClienteCorriereDao;
import it.ltc.database.dao.common.CommessaDao;
import it.ltc.database.dao.common.CorriereDao;
import it.ltc.database.model.centrale.JoinCommessaCorriere;
import it.ltc.database.model.centrale.Spedizione;

@Component
public class SpedizioneValidator implements Validator {
	
	private final CodiceClienteCorriereDao daoCodice;
	private final CommessaDao daoCommesse;
	private final CorriereDao daoCorrieri;
	
	public SpedizioneValidator() {
		daoCodice = new CodiceClienteCorriereDao();
		daoCommesse = new CommessaDao();
		daoCorrieri = new CorriereDao();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = Spedizione.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Spedizione spedizione = (Spedizione) target;
		
		if (spedizione.getColli() < 1)
			errors.reject("colli", "Il numero di colli indicato non e' valido (" + spedizione.getColli() + ")");
		
		if (spedizione.getIndirizzoDestinazione() < 1)
			errors.reject("destinazione", "Bisogna indicare un indirizzo di destinazione esistente.");
		
		if (spedizione.getIndirizzoPartenza() < 1)
			errors.reject("destinazione", "Bisogna indicare un indirizzo di partenza esistente.");
		
		int commessa = spedizione.getIdCommessa();
		if (daoCommesse.trovaDaID(commessa) == null)
			errors.reject("commessa", "La commessa indicata non esiste.");
		
		int corriere = spedizione.getIdCorriere();
		if (daoCorrieri.trovaDaID(corriere) == null)
			errors.reject("corriere", "Il corriere indicato non esiste.");
		
		String codiceCliente = spedizione.getCodiceCliente();
		if (codiceCliente == null || codiceCliente.isEmpty()) {
			errors.reject("codiceCliente", "Bisogna indicare un codice cliente relativo al corriere per la spedizione.");
		} else {
			JoinCommessaCorriere entity = daoCodice.trovaDaCodice(codiceCliente);
			if (entity == null)
				errors.reject("codiceCliente", "Il codice cliente indicato non esiste. (" + codiceCliente + ")");
			//I seguenti controlli sono stati eliminati per semplificare la vita di Sonia che doveva cambiare la commessa in caso di errore.
//			else if (entity.getCommessa() != commessa)
//				errors.reject("codiceCliente", "Il codice cliente indicato non appartiene alla commessa specificata. (" + codiceCliente + ")");
//			else if (entity.getCorriere() != corriere)
//				errors.reject("codiceCliente", "Il codice cliente indicato non appartiene al corriere specificato. (" + codiceCliente + ")");
		}
		
		
	}

}
