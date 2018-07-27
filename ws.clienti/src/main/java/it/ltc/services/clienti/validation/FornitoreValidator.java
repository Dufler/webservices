package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.FornitoreJSON;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;

/**
 * Il validatore esegue i seguenti controlli:
 * - presenza di un valore nei campi necessari
 * @author Damiano
 *
 */
@Component
public class FornitoreValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("FornitoreValidator");
	
	@Autowired
	private IndirizzoValidator validatoreIndirizzo;
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = FornitoreJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		FornitoreJSON fornitore = (FornitoreJSON) target;
		logger.info("Avvio la validazione per il fornitore: " + fornitore);
		
		String nome = fornitore.getNome();
		if (nome == null || nome.isEmpty())
			errors.reject("nome.necessario", "E' necessario specificare un nome per il fornitore.");
		else if (nome.length() > 50)
			errors.reject("nome.lunghezza", "Il nome indicato è troppo lungo. (MAX 50 caratteri)");
		
		String riferimentoCliente = fornitore.getRiferimentoCliente();
		if (riferimentoCliente == null || riferimentoCliente.isEmpty())
			errors.reject("riferimentoCliente.necessario", "E' necessario specificare un riferimento per il fornitore.");
		else if (riferimentoCliente.length() > 20)
			errors.reject("riferimentoCliente.lunghezza", "Il riferimento indicato è troppo lungo. (MAX 20 caratteri)");
		
		String note = fornitore.getNote();
		if (note != null && note.length() > 250)
			errors.reject("note", "Le note indicate sono troppo lunghe. (MAX 250 caratteri)");
		
		IndirizzoJSON indirizzo = fornitore.getIndirizzo();
		if (indirizzo == null)
			errors.reject("indirizzo.necessario", "E' necessario specificare un indirizzo per il fornitore.");
		else {
			//eseguo la validazione sull'indirizzo specificato.				
			validatoreIndirizzo.validate(indirizzo, errors);
		}
	}

}
