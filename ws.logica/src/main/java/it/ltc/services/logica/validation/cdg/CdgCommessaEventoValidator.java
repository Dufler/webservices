package it.ltc.services.logica.validation.cdg;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.CdgCommessaEvento;

@Component
public class CdgCommessaEventoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CdgCommessaEvento.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
