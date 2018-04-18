package it.ltc.services.clienti.validation;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.clienti.model.prodotto.IngressoJSON;

@Component
public class IngressoValidator implements Validator {
	
	private enum Tipo { CARICO, RESO, CAMPIONARIO, BOUTIQUE } //TODO - Fare si che le tipologie risiedano nel DB del cliente.

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = IngressoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		IngressoJSON ingresso = (IngressoJSON) target;
		//Controllo sui campi necessari
		if (ingresso.getFornitore() == null || ingresso.getFornitore().isEmpty())
			errors.reject("fornitore.necessario", "E' necessario indicare un fornitore.");
		if (ingresso.getPezziStimati() < 1)
			errors.reject("pezzistimati.validi", "E' necessario inserire un carico con almeno un pezzo in arrivo.");
		if (ingresso.getRiferimentoCliente() == null || ingresso.getRiferimentoCliente().isEmpty())
			errors.reject("riferimento.necessario", "E' necessario inserire un riferimento chiave per il carico.");
		else if (ingresso.getRiferimentoCliente().length() > 30)
			errors.reject("riferimento.lunghezza", "La lunghezza del riferimento chiave è troppo lunga. (MAX 30 caratteri)");
		String tipo = ingresso.getTipo();
		if (tipo == null || tipo.isEmpty())
			errors.reject("tipo.necessario", "E' necessario inserire una tipologia di carico.");
		else try {
			Tipo.valueOf(tipo);
		} catch (Exception e) {
			errors.reject("tipo.valido", "E' necessario inserire una tipologia di carico valida. I possibili valori sono: " + Tipo.values());
		}
		//Controllo sui facoltativi
		Date dataArrivo = ingresso.getDataArrivo();
		if (dataArrivo != null && dataArrivo.before(new Date()))
			errors.reject("dataarrivo.valida", "La data di arrivo precede quella di immissione del carico.");
	}

}
