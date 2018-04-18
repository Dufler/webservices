package it.ltc.services.logica.validation.listini;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.ListinoCorriereVoce;

@Component
public class VoceListinoCorriereValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = ListinoCorriereVoce.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
