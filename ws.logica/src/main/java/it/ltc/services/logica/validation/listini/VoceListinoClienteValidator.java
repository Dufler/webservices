package it.ltc.services.logica.validation.listini;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.ListinoCommessaVoce;

@Component
public class VoceListinoClienteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = ListinoCommessaVoce.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
