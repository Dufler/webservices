package it.ltc.services.sede.validation.operatore;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.sede.Attivita;

@Component
public class AttivitaValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Attivita.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Attivita attivita = (Attivita) obj;
		
		//Campi necessari
		String utente = attivita.getUtente();
		if (utente == null || utente.isEmpty())
			errors.reject("utente", "E' necessario specificare un utente a cui assegnare l'attivita'.");

		int commessa = attivita.getCommessa();
		if (commessa < 1)
			errors.reject("commessa", "E' necessario specificare una commessa per l'attivita'.");
		
		int tipo = attivita.getTipo();
		if (tipo < 0) //FIXME: Validare il tipo di attività
			errors.reject("tipo", "E' necessario specificare un tipo per l'attivita'.");
		
		//Campi facoltativi
		Date inizio = attivita.getDataInizio();
		Date fine = attivita.getDataFine();
		if (inizio != null && fine != null && fine.before(inizio))
			errors.reject("date", "La data di fine specificata precede quella di inizio.");
		
		String riferimento = attivita.getRiferimento();
		if (riferimento != null && riferimento.length() > 250)
			errors.reject("riferimento", "Il riferimento indicato è troppo lungo. (MAX 250 caratteri)");
		
		String note = attivita.getNote();
		if (note != null && note.length() > 250)
			errors.reject("note", "Le note indicate sono troppo lunghe. (MAX 250 caratteri)");
	}

}
