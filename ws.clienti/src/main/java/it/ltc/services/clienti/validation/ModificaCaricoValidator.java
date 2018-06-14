package it.ltc.services.clienti.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.ModificaCaricoJSON;

@Component
public class ModificaCaricoValidator implements Validator {

	
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = ModificaCaricoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ModificaCaricoJSON modifiche = (ModificaCaricoJSON) target;
		
		int idCarico = modifiche.getId();
		if (idCarico < 1)
			errors.reject("carico.id", "E' necessario inserire un ID per il carico valido.");
	}

}
