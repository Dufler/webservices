package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.Contatto;

@Component
public class ContattoValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Contatto.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Contatto contatto = (Contatto) obj;
		
		String nome = contatto.getNome();
		if (nome == null || nome.isEmpty())
			errors.reject("nome", "Il nome è obbligatorio.");
		else if (nome.length() > 100)
			errors.reject("nome", "Il nome è troppo lungo. (MAX 100 Caratteri)");
		
		String cognome = contatto.getCognome();
		if (cognome == null || cognome.isEmpty())
			errors.reject("cognome", "Il cognome è obbligatorio.");
		else if (cognome.length() > 100)
			errors.reject("cognome", "Il cognome è troppo lungo. (MAX 100 Caratteri)");
		
//		String email = contatto.getEmail();
//		if (email != null && !email.isEmpty()) {
//			if (email.length() > 250)
//				errors.reject("email", "L'email è troppo lunga. (MAX 250 caratteri)");
//			if (!email.matches(REGEX_EMAIL))
//				errors.reject("email", "L'email non è corretta.");
//		}
		
//		String telefono = contatto.getTelefono();
//		if (telefono != null && !telefono.isEmpty()) {
//			if (telefono.length() > 100)
//				errors.reject("telefono", "Il numero di telefono è troppo lungo. (MAX 100 caratteri)");
//			if (!telefono.matches(REGEX_TELEFONO))
//				errors.reject("telefono", "Il numero di telefono non è corretto.");
//		}
		
		String ruolo = contatto.getRuolo();
		if (ruolo != null && ruolo.length() > 100)
				errors.reject("ruolo", "Il ruolo è troppo lungo. (MAX 100 caratteri)");
		
//		String titolo = contatto.getTitolo();
//		if (titolo != null && titolo.length() > 100)
//				errors.reject("titolo", "Il titolo è troppo lungo. (MAX 100 caratteri)");
			
	}

}
