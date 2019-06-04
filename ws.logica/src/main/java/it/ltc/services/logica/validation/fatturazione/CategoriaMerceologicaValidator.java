package it.ltc.services.logica.validation.fatturazione;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.interno.CategoriaMerceologicaJSON;

@Component
public class CategoriaMerceologicaValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = CategoriaMerceologicaJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		CategoriaMerceologicaJSON categoria = (CategoriaMerceologicaJSON) arg0;

		int commessa = categoria.getCommessa();
		if (commessa <= 0) {
			arg1.reject("commessa", "Bisogna indicare una commessa valida.");
		}
		
		String nome = categoria.getNome();
		if (nome == null || nome.isEmpty()) {
			arg1.reject("codice", "Bisogna indicare un codice valido.");
		} else if (nome.length() > 20) {
			arg1.reject("codice", "Il codice indicato è troppo lungo. (MAX 20 Caratteri)");
		}
		
		String descrizione = categoria.getDescrizione();
		if (descrizione != null && descrizione.length() > 50) {
			arg1.reject("descrizione", "La descrizione indicata è troppo lunga. (MAX 50 Caratteri)");
		}
		
	}

}
