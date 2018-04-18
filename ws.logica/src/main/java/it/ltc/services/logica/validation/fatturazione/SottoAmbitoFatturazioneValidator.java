package it.ltc.services.logica.validation.fatturazione;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.FatturaSottoAmbito;

@Component
public class SottoAmbitoFatturazioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = FatturaSottoAmbito.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
