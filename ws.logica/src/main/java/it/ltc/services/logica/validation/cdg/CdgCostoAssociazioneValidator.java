package it.ltc.services.logica.validation.cdg;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.CdgCostoAssociazione;

@Component
public class CdgCostoAssociazioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CdgCostoAssociazione.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
