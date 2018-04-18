package it.ltc.services.clienti.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.common.TipoContrassegnoDao;
import it.ltc.database.model.centrale.SpedizioneContrassegnoTipo;
import it.ltc.services.clienti.model.prodotto.ContrassegnoJSON;

@Component
public class ContrassegnoValidator implements Validator {
	
	public enum ValutaContrassegno { EUR, USD }
	
	//public enum TipoContrassegno { BB, BM, CB, CM, NA, OM, SC, TM, TO }

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = ContrassegnoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ContrassegnoJSON contrassegno = (ContrassegnoJSON) target;
		
		String tipo = contrassegno.getTipo();
		if (tipo == null || tipo.isEmpty()) {
			errors.reject("contrassegno.tipo.necessario", "E' necessario indicare la tipologia di contrassegno.");
		} else {
			SpedizioneContrassegnoTipo tipoContrassegno = TipoContrassegnoDao.getInstance().findByCodice(tipo);
			if (tipoContrassegno == null)
				errors.reject("contrassegno.tipo.valido", "La tipologia di contrassegno indicata non e' valida. (" + tipo + ")");
		}
		
		Double valore = contrassegno.getValore();
		if (valore == null || valore <= 0) {
			errors.reject("contrassegno.valore.valido", "E' necessario indicare un valore valido per il contrassegno.");
		}
		
		String valuta = contrassegno.getValuta();
		if (valuta == null || valuta.isEmpty()) {
			contrassegno.setValuta("EUR");
		} else try {
			ValutaContrassegno.valueOf(valuta);
		} catch (Exception e) {
			errors.reject("contrassegno.valuta.valida", "E' necessario indicare un valore valido per la valuta del contrassegno. I possibili valori sono: " + ValutaContrassegno.values());
		}
		
	}

}
