package it.ltc.services.logica.validation.trasporti;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.Spedizione;

@Component
public class SpedizioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = Spedizione.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Spedizione spedizione = (Spedizione) target;
		if (spedizione.getColli() < 1)
			errors.reject("colli.validi", "Il numero di colli indicato non e' valido (" + spedizione.getColli() + ")");
		//TODO
	}

}
