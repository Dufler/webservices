package it.ltc.services.sede.validation.ordine;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.interno.ordine.DeliveryJSON;

@Component
public class DeliveryValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = DeliveryJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		DeliveryJSON delivery = (DeliveryJSON) arg0;
		
		String corriere = delivery.getCorriere();
		if (corriere == null || corriere.isEmpty())
			errors.reject("corriere", "Bisogna indicare un corriere.");
		
		Set<Integer> idSpedizioni = delivery.getSpedizioni();
		if (idSpedizioni == null || idSpedizioni.isEmpty())
			errors.reject("spedizioni", "Bisogna indicare almeno una spedizione.");

	}

}
