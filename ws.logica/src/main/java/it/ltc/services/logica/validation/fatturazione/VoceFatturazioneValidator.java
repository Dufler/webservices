package it.ltc.services.logica.validation.fatturazione;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.FatturaVoce;

@Component
public class VoceFatturazioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = FatturaVoce.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
