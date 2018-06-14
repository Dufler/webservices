package it.ltc.services.sede.validation.carico;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.carico.ColloCaricoJSON;

@Component
public class ColloCaricoValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = ColloCaricoJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ColloCaricoJSON collo = (ColloCaricoJSON) obj;
		
		int carico = collo.getCarico();
		if (carico < 1)
			errors.reject("carico", "Va indicato un carico valido.");
		
		String magazzino = collo.getMagazzino();
		if (magazzino == null || magazzino.isEmpty())
			errors.reject("magazzino", "Va indicato un magazzino valido.");

	}

}
