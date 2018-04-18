package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.clienti.model.prodotto.IndirizzoJSON;
import it.ltc.services.clienti.model.prodotto.UscitaJSON;

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
	
//	@Autowired
//	private ContrassegnoValidator validatoreContrassegno;
//	
//	@Autowired
//	private DocumentoValidator validatoreDocumento;

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
		String tipo = ordine.getTipo();
		if (tipo == null || tipo.isEmpty())
			errors.reject("tipo.necessario", "E' necessario indicare un tipo di ordine.");
		else {
			//TODO - Controllo che il tipo di ordine sia valido
		}
		
		String riferimentoOrdine = ordine.getRiferimentoOrdine();
		if (riferimentoOrdine == null || riferimentoOrdine.isEmpty())
			errors.reject("riferimento.necessario", "E' necessario indicare un riferimento per l'ordine.");
		else if (riferimentoOrdine.length() > 20)
			errors.reject("riferimento.lunghezza", "Il riferimento per l'ordine e' troppo lungo. (MAX 20 Caratteri)");
		
//		String corriere = ordine.getCorriere();
//		if (corriere == null || corriere.isEmpty())
//			errors.reject("corriere.necessario", "E' necessario indicare il corriere con cui partira' la merce.");
//		else {
//			//Controlla che sia un corriere valido.
//		}
//		
//		String servizioCorriere = ordine.getServizioCorriere();
//		if (servizioCorriere == null || servizioCorriere.isEmpty())
//			errors.reject("servizio.necessario", "E' necessario indicare il livello di servizio del corriere con cui partira' la merce.");
//		else {
//			//Controlla che il livello di servizio sia valido
//		}
		
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
//		ContrassegnoJSON contrassegno = ordine.getContrassegno();
//		if (contrassegno != null)
//			validatoreContrassegno.validate(contrassegno, errors);
//		
//		Double valoreDoganale = ordine.getValoreDoganale();
//		if (valoreDoganale != null && valoreDoganale <= 0)
//			errors.reject("valoredoganale.valido", "Il valore doganale indicato non e' valido. (" + valoreDoganale + ")");
//
//		DocumentoJSON documentoFiscale = ordine.getDocumentoFiscale();
//		if (documentoFiscale != null)
//			validatoreDocumento.validate(documentoFiscale, errors);
		
		Integer priorita = ordine.getPriorita();
		if (priorita != null && (priorita < 1 || priorita > 10))
			errors.reject("priorita.valido", "Il valore indicato per la priorita' non e' valido. (" + priorita + ", min 1 - MAX 10)");
	}

}
