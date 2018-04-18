package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.Brand;

@Component
public class BrandValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Brand.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Brand brand = (Brand) obj;

		String nome = brand.getNome();
		if (nome == null || nome.isEmpty())
			errors.reject("nome", "Il nome è necessario.");
		else if (nome.length() > 250)
			errors.reject("nome", "Il nome è troppo lungo. (MAX 250 Carateri)");
	}

}
