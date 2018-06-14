package it.ltc.services.logica.validation.common;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.utente.Utente;

@Component
public class UtenteValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Utente.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Utente utente = (Utente) obj;
		
		//TODO - Verificare accuratamente il caso di insert e update.

	}

}
