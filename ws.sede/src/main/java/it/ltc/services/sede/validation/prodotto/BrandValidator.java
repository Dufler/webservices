package it.ltc.services.sede.validation.prodotto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.Brand;

@Component
public class BrandValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Brand.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		Brand brand = (Brand) arg0;

		String descrizione = brand.getDescrizione();
		if (descrizione != null && descrizione.length() > 50) {
			errors.reject("brand.descrizione", "La descrizione del brand è troppo lunga. (MAX 50 caratteri)");
		}
	}

}
