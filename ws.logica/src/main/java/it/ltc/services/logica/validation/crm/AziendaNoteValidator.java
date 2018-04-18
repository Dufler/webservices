package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.AziendaNote;

@Component
public class AziendaNoteValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = AziendaNote.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		AziendaNote note = (AziendaNote) obj;
		
		String autore = note.getAutore();
		if (autore == null || autore.isEmpty())
			errors.reject("autore", "E' necessario indicare l'autore della nota.");
		
		int azienda = note.getAzienda();
		if (azienda < 1)
			errors.reject("azienda", "E' necessario indicare un'azienda a cui attribuire la nota.");
		
		String nota = note.getNote();
		if (nota == null || nota.isEmpty())
			errors.reject("nota", "Bisogna compilare il campo nota.");
		else if (nota.length() > 250)
			errors.reject("nota", "Le note sono troppo lunghe. (MAX 250 Caratteri)");
		
	}

}
