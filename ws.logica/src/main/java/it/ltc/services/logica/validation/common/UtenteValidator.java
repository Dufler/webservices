package it.ltc.services.logica.validation.common;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.utente.UtenteUtenti;

@Component
public class UtenteValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = UtenteUtenti.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		UtenteUtenti utente = (UtenteUtenti) obj;
		
		//TODO - Verificare accuratamente il caso di insert e update.

	}

}
