package it.ltc.services.logica.validation.cdg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.CdgPezzo;
import it.ltc.database.model.centrale.json.CdgPezzoEventoJSON;

@Component
public class CdgPezzoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CdgPezzo.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CdgPezzo pezzo = (CdgPezzo) target;
		
		//Controllo che ricavo e costo siano presenti e maggiori di 0.
		double costo = pezzo.getCosto();
		if (costo <= 0)
			errors.reject("pezzo.costo", "Il costo non può essere minore di 0.");
		double ricavo = pezzo.getRicavo();
		if (ricavo <= 0)
			errors.reject("pezzo.ricavo", "Il ricavo non può essere minore di 0.");
		
		//Controllo che mi abbiano fornito gli spacchettamenti e che la loro somma sia 100 per costo e ricavo.
		//Inoltre verifico che lo stesso evento non sia stato passato più di una volta.
		List<CdgPezzoEventoJSON> spacchettamenti = pezzo.getSpacchettamenti();
		if (spacchettamenti == null || spacchettamenti.isEmpty()) {
			errors.reject("pezzo.spacchettamenti", "E' necessario fornire gli spacchettamenti");
		} else {
			Set<Integer> eventi = new HashSet<>();
			double ricavoPercentuale = 0;
			double costoPercentuale = 0;
			for (CdgPezzoEventoJSON spacchettamento : spacchettamenti) {
				ricavoPercentuale += spacchettamento.getRicavo();
				costoPercentuale += spacchettamento.getCosto();
				if (eventi.contains(spacchettamento.getEvento())) {
					errors.reject("pezzo.spacchettamento", "E' stato selezionato lo stesso evento più di una volta. ID: " + spacchettamento.getEvento());
				} else {
					eventi.add(spacchettamento.getEvento());
				}
			}
			if (ricavoPercentuale != 100.0)
				errors.reject("pezzo.ricavo.percentuale", "La somma dei ricavi percentuali è diversa da 100.");
			if (costoPercentuale != 100.0)
				errors.reject("pezzo.costo.percentuale", "La somma dei costi percentuali è diversa da 100.");
		}
	}

}
