package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.ContrassegnoJSON;

@Component
public class ContrassegnoValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("ContrassegnoValidator");
	
	public enum ValutaContrassegno { EUR, USD }
	
	public enum TipoContrassegno { BB, BM, CB, CM, NA, OM, SC, TM, TO }
	
	//private final TipoContrassegnoDao daoTipoContrassegno;
	
//	public ContrassegnoValidator() {
//		daoTipoContrassegno = new TipoContrassegnoDao();
//	}

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = ContrassegnoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ContrassegnoJSON contrassegno = (ContrassegnoJSON) target;
		logger.info("Avvio la validazione per il contrassegno: " + contrassegno);
		String tipo = contrassegno.getTipo();
		if (tipo == null || tipo.isEmpty()) {
			errors.reject("contrassegno.tipo.necessario", "E' necessario indicare la tipologia di contrassegno.");
		} else {
//			SpedizioneContrassegnoTipo tipoContrassegno = daoTipoContrassegno.trovaDaCodice(tipo);
//			if (tipoContrassegno == null)
//				errors.reject("contrassegno.tipo.valido", "La tipologia di contrassegno indicata non e' valida. (" + tipo + ")");
			try {
				TipoContrassegno.valueOf(tipo);
			} catch (Exception e) {
				errors.reject("contrassegno.tipo.valido", "La tipologia di contrassegno indicata non e' valida. (" + tipo + ") I possibili valori sono: " + TipoContrassegno.values());
			}
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
