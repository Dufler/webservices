package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.GadgetInviati;

@Component
public class GadgetInviatiValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = GadgetInviati.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		GadgetInviati inviati = (GadgetInviati) obj;
		
		int quantità = inviati.getQuantita();
		if (quantità < 1)
			errors.reject("quantita", "La quantita' di gadget inviati non può essere minore di 1.");

	}

}
