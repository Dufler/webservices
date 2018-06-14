package it.ltc.services.clienti.validation;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.DocumentoJSON;

@Component
public class DocumentoValidator implements Validator {
	
	private enum Tipo {CARICO, RESO, ORDINE, CAMPIONARIO, LAVORAZIONE, ALTRO}

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = DocumentoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		DocumentoJSON documento = (DocumentoJSON) target;
		
		String riferimento = documento.getRiferimento();
		if (riferimento == null || riferimento.isEmpty())
			errors.reject("documento.riferimento.necessario", "E' necessario indicare un riferimento per il documento.");
		else if (riferimento.length() > 40)
			errors.reject("documento.riferimento.lughezza", "La lunghezza del riferimento per il documento è troppo lunga. (MAX 40 caratteri)");
		
		Date data = documento.getData();
		if (data == null)
			errors.reject("documento.data.necessaria", "E' necessario indicare una data per il documento.");
		
		String tipo = documento.getTipo();
		if (tipo == null || tipo.isEmpty())
			errors.reject("documento.tipo.necessario", "E' necessario indicare un tipo di documento.");
		else try {
			Tipo.valueOf(tipo);
		} catch (Exception e) {
			errors.reject("documento.tipo.valido", "E' necessario indicare un tipo di documento valido. I possibili valori sono: " + Tipo.values());
		}
		
	}

}
