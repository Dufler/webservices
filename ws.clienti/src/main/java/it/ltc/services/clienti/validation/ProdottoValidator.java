package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.ProdottoJSON;

/**
 * Il validatore esegue i seguenti controlli:
 * - presenza di un valore nei campi necessari
 * - controllo sul made in: se inserito deve essere codificato secondo la ISO3
 * - controllo formale sulla stagione: 4 caratteri (i primi 2 sono la stagione, gli altri 2 l'anno, la stagione può essere AU, IN, PR, ES, AI, PE, CO)
 * - controllo sul peso, h, l, z: se il valore viene inserito deve essere maggiore di 0
 * - controllo sul valore: se viene inserito deve essere maggiore di 0 euro.
 * @author Damiano
 *
 */
@Component
public class ProdottoValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("ProdottoValidator");
	
	public static final String regexStagione = "^(AU|IN|PR|ES|AI|PE|CO)\\d{2}"; //Qua volendo possiamo andare ad aggiungere una codifica per mese nel caso in cui servisse. (es. Gennaio -> GE etc.)

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = ProdottoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProdottoJSON prodotto = (ProdottoJSON) target;
		logger.info("Avvio la validazione per il prodotto: " + prodotto);
		//Controllo sulla presenza dei campi obbligatori
		
		String chiaveCliente = prodotto.getChiaveCliente();
		if (chiaveCliente == null || chiaveCliente.isEmpty())
			errors.reject("chiaveCliente.necessaria", "La codifica del prodotto è obbligatoria.");
		else if (chiaveCliente.length() > 50)
			errors.reject("chiaveCliente.lunghezza", "La codifica del prodotto è troppo lunga. (MAX 50 caratteri)");
		
		String codiceModello = prodotto.getCodiceModello();
		if (codiceModello == null || codiceModello.isEmpty())
			errors.reject("codiceModello.necessario", "La codifica del modello del prodotto è obbligatoria.");
		else if (codiceModello.length() > 50)
			errors.reject("codiceModello.lunghezza", "La codifica del modello è troppo lunga. (MAX 50 caratteri)");
		
		String barcode = prodotto.getBarcode();
		if (barcode == null || barcode.isEmpty())
			errors.reject("barcode.necessario", "Il barcode del prodotto è obbligatorio.");
		else if (barcode.length() > 50)
			errors.reject("barcode.lunghezza", "Il barcode del prodotto è troppo lungo. (MAX 50 caratteri)");
		
		String taglia = prodotto.getTaglia();
		if (taglia == null || taglia.isEmpty())
			errors.reject("taglia.necessaria", "La taglia del prodotto è obbligatoria.");
		else if (taglia.length() > 10)
			errors.reject("taglia.lunghezza", "La taglia del prodotto è troppo lunga. (MAX 20 caratteri)");
		
		String descrizione = prodotto.getDescrizione();
		if (descrizione == null || descrizione.isEmpty())
			errors.reject("descrizione.necessaria", "La descrizione del prodotto è obbligatoria.");
		else if (descrizione.length() > 100)
			errors.reject("taglia.lunghezza", "La descrizione del prodotto è troppo lunga. (MAX 100 caratteri)");
		
		String brand = prodotto.getBrand();
		if (brand == null || brand.isEmpty())
			errors.reject("brand.necessario", "Il brand del prodotto è obbligatorio.");
		else if (brand.length() > 50)
			errors.reject("brand.lunghezza", "Il brand del prodotto è troppo lungo. (MAX 50 caratteri)");
		
		String categoria = prodotto.getCategoria();
		if (categoria == null || categoria.isEmpty())
			errors.reject("categoria.necessaria", "La categoria merceologica del prodotto è obbligatoria.");
		
		//Facoltativi
		
		String descrizioneAggiuntiva = prodotto.getDescrizioneAggiuntiva();
		if (descrizioneAggiuntiva != null && descrizioneAggiuntiva.length() > 100)
			errors.reject("note.lunghezza", "La descrizione aggiuntiva del prodotto è troppo lunga. (MAX 100 caratteri)");
		
		String note = prodotto.getNote();
		if (note != null && note.length() > 250)
			errors.reject("note.lunghezza", "Le note del prodotto sono troppo lunghe. (MAX 250 caratteri)");
		
		//Stagione
//		String stagione = prodotto.getStagione();
//		if (stagione != null && !stagione.isEmpty() && !stagione.matches(regexStagione))
//			errors.reject("stagione.formato", "Il valore indicato per la stagione è errato. I possibili valori sono: AU (Autunno), IN (Inverno), PR (Primavera), ES (Estate), AI (Autunno-Inverno), PE (Primavera-Estate), CO (Continuativo) seguiti da 2 cifre indicanti l'anno. Es: AI17.");
		String stagione = prodotto.getStagione();
		if (stagione != null && stagione.length() > 30)
			errors.reject("stagione.lunghezza", "La stagione è troppo lunga. (MAX 30 caratteri)");
		
		//Peso, H, L e Z: se il valore è presente deve essere maggiore di 0.
		Integer peso = prodotto.getPeso();
		if (peso != null && peso < 0)
			errors.reject("peso.errato", "Il peso deve essere maggiore di 0 grammi.");
		Integer h = prodotto.getH();
		if (h != null && h < 0)
			errors.reject( "h.errato", "L'altezza deve essere maggiore di 0 millimetri.");
		Integer l = prodotto.getL();
		if (l != null && l < 0)
			errors.reject("l.errato", "La lunghezza deve essere maggiore di 0 millimetri.");
		Integer z = prodotto.getZ();
		if (z != null && z < 0)
			errors.reject("z.errato", "La profondita' deve essere maggiore di 0 millimetri.");
		//Valore: se il valore è presente deve essere maggiore di 0 euro.
		Double valore = prodotto.getValore();
		if (valore != null && valore < 0)
			errors.reject("valore.errato", "Il valore deve essere maggiore di 0 euro.");
		
		String colore = prodotto.getColore();
		if (colore != null && colore.length() > 50)
			errors.reject("colore.lunghezza", "Il colore del prodotto è troppo lungo. (MAX 50 caratteri)");
		
		String composizione = prodotto.getComposizione();
		if (composizione != null && composizione.length() > 100)
			errors.reject("composizione.lunghezza", "La composizione del prodotto è troppo lunga. (MAX 100 caratteri)");
	}

}
