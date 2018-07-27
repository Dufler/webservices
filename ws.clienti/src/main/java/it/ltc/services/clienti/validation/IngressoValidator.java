package it.ltc.services.clienti.validation;

import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.IngressoJSON;

@Component
public class IngressoValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("IngressoValidator");

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = IngressoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		IngressoJSON ingresso = (IngressoJSON) target;
		logger.info("Avvio la validazione per la testata del carico: " + ingresso);
		
		//Controllo sui campi necessari
		String fornitore = ingresso.getFornitore(); 
		if (fornitore == null || fornitore.isEmpty())
			errors.reject("fornitore.necessario", "E' necessario indicare un fornitore.");
		
		if (ingresso.getPezziStimati() < 1)
			errors.reject("pezzistimati.validi", "E' necessario inserire un carico con almeno un pezzo in arrivo.");
		
		String riferimento = ingresso.getRiferimentoCliente();
		if (riferimento == null || riferimento.isEmpty())
			errors.reject("riferimento.necessario", "E' necessario inserire un riferimento chiave per il carico.");
		else if (ingresso.getRiferimentoCliente().length() > 30)
			errors.reject("riferimento.lunghezza", "La lunghezza del riferimento chiave è troppo lunga. (MAX 30 caratteri)");
		
		String tipo = ingresso.getTipo();
		if (tipo == null || tipo.isEmpty())
			errors.reject("tipo.necessario", "E' necessario inserire una tipologia di carico.");
		
		//Controllo sui facoltativi
		Date dataArrivo = ingresso.getDataArrivo();
		if (dataArrivo != null && dataArrivo.before(new Date()))
			errors.reject("dataarrivo.valida", "La data di arrivo precede quella di immissione del carico.");
		
		String note = ingresso.getNote();
		if (note != null && note.length() > 250)
			errors.reject("note.lunghezza", "Le note sono troppo lunghe. (MAX 250 caratteri)");
	}

}
