package it.ltc.services.logica.validation.cdg;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.CdgCostiRicaviGenericiFase;

@Component
public class CdgCostiRicaviGenericiFaseValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = CdgCostiRicaviGenericiFase.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub

	}

}
