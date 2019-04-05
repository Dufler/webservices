package it.ltc.services.sede.validation.ordine;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;

@Component
public class DatiSpedizioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = DatiSpedizione.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		DatiSpedizione dati = (DatiSpedizione) arg0;
		
		if (dati.getColli() < 1)
			errors.reject("colli", "Il numero dei colli non è valido. (" + dati.getColli() + ")");
		
		//TODO

	}

}
