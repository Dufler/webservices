package it.ltc.services.logica.validation.common;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.Commessa;

@Component
public class CommessaValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Commessa.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub

	}

}
