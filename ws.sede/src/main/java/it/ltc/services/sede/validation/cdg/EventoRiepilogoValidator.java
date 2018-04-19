package it.ltc.services.sede.validation.cdg;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.sede.json.CdgEventoRiepilogoJSON;

@Component
public class EventoRiepilogoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CdgEventoRiepilogoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}

}
