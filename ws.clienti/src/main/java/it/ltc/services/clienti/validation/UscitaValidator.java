package it.ltc.services.clienti.validation;

import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.model.shared.json.cliente.UscitaJSON;

/**
 * Il validatore esegue i seguenti controlli:
 * <br>
 * <ul>
 * <li>presenza di un valore nei campi necessari</li>
 * <li>univocita' dei numeri di riga</li>
 * <li>validit&agrave; dei campi facoltativi (priorita, dataConsegna, ...)</li>
 * </ul>
 * <br>
 * Rimanda poi la validazione agli elementi interni tramite altri validatori.
 * @author Damiano
 *
 */
@Component
public class UscitaValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("OrdineValidator");
	
	@Autowired
	private IndirizzoValidator validatoreIndirizzo;

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = UscitaJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UscitaJSON ordine = (UscitaJSON) target;
		logger.info("Avvio la validazione per l'ordine: " + ordine);
		
		//Presenza dei campi necessari
		
		Date dataOrdine = ordine.getDataOrdine();
		if (dataOrdine == null)
			errors.reject("dataOrdine.necessaria", "E' necessario indicare una data per l'ordine.");
		
		//Controllo solo che sia stato inserito, la vera validazione avviene dopo controllando i tipi disponibili per il cliente.
		String tipo = ordine.getTipo();
		if (tipo == null || tipo.isEmpty())
			errors.reject("tipo.necessario", "E' necessario indicare un tipo di ordine.");
		
		String riferimentoOrdine = ordine.getRiferimentoOrdine();
		if (riferimentoOrdine == null || riferimentoOrdine.isEmpty())
			errors.reject("riferimento.necessario", "E' necessario indicare un riferimento per l'ordine.");
		else if (riferimentoOrdine.length() > 20)
			errors.reject("riferimento.lunghezza", "Il riferimento per l'ordine e' troppo lungo. (MAX 20 Caratteri)");
		
		IndirizzoJSON destinatario = ordine.getDestinatario();
		if (destinatario == null)
			errors.reject("destinatario.necessario", "E' necessario indicare il destinatario.");
		else
			validatoreIndirizzo.validate(destinatario, errors);
		
		IndirizzoJSON mittente = ordine.getMittente();
		if (mittente == null)
			errors.reject("mittente.necessario", "E' necessario indicare il mittente.");
		else
			validatoreIndirizzo.validate(mittente, errors);
		
		//Opzionali		
		Integer priorita = ordine.getPriorita();
		if (priorita != null && (priorita < 1 || priorita > 10))
			errors.reject("priorita.valido", "Il valore indicato per la priorita' non e' valido. (" + priorita + ", min 1 - MAX 10)");
		
		String note = ordine.getNote();
		if (note != null && note.length() > 250)
			errors.reject("note.lunghezza", "La lunghezza delle note è eccessiva. (MAX 250 caratteri)");
		
		Date dataConsegna = ordine.getDataConsegna();
		if (dataConsegna != null && dataConsegna.before(new Date()))
			errors.reject("dataConsegna.valida", "E' necessario indicare una data di consegna valida.");
		
	}

}
