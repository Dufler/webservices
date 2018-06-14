package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.CrmTagCategoriaMerceologica;

@Component
public class TagCategorieValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = CrmTagCategoriaMerceologica.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		CrmTagCategoriaMerceologica tagCategoria = (CrmTagCategoriaMerceologica) obj;

		int azienda = tagCategoria.getAzienda();
		if (azienda < 1)
			errors.reject("azienda", "Il tag deve essere associato ad una azienda valida.");
		
		String tag = tagCategoria.getTag();
		if (tag == null || tag.isEmpty())
			errors.reject("tag", "Il tag non può essere vuoto.");
	}

}
