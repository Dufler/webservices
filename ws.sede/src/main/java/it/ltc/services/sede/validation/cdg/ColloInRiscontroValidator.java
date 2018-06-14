package it.ltc.services.sede.validation.cdg;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.carico.ColloInRiscontroJSON;
import it.ltc.services.sede.model.carico.ProdottoInRiscontroJSON;

@Component
public class ColloInRiscontroValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = ColloInRiscontroJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ColloInRiscontroJSON collo = (ColloInRiscontroJSON) obj;
		
		int carico = collo.getCarico();
		if (carico < 1)
			errors.reject("carico", "Va specificato un carico valido.");
		
		String magazzino = collo.getMagazzino();
		if (magazzino == null || magazzino.isEmpty())
			errors.reject("magazzino", "Va specificato un magazzino.");
		
		//Controllo che se abbia specificato seriali per un prodotto allora li abbia specificati per tutti e che siano concordi come quantità.
		boolean serialiPresenti = false;
		for (ProdottoInRiscontroJSON prodotto : collo.getProdotti()) {
			List<String> seriali = prodotto.getSeriali();
			if (seriali != null && !seriali.isEmpty()) {
				serialiPresenti = true;
				if (prodotto.getQuantita() != seriali.size())
					errors.reject("seriali", "La quantita' di seriali indicata e' diversa dalla quantita' di prodotto indicata.");
			} else if (serialiPresenti) {
				errors.reject("seriali", "Non e' possibile inserire seriali solo per alcuni prodotti nello stesso collo.");
			}
		}
	}

}
