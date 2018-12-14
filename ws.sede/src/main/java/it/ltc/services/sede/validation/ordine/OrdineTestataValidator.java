package it.ltc.services.sede.validation.ordine;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.interno.OrdineTestata;

@Component
public class OrdineTestataValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = OrdineTestata.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub

	}

}
