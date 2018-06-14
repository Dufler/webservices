package it.ltc.services.sede.validation.operatore;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.operatore.EventoJSON;

@Component
public class EventoJSONValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = EventoJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		EventoJSON evento = (EventoJSON) obj;
		
		String client = evento.getClient();
		if (client == null || client.isEmpty())
			errors.reject("client", "Il nome del client è necessario.");
		
		int idEvento = evento.getEvento();
		if (idEvento < 1)
			errors.reject("evento", "L'evento o ID di costo è necessario.");
		
		int pezzi = evento.getPezzi();
		if (pezzi < 0)
			errors.reject("pezzi", "Il numero dei pezzi non può essere inferiore a 0.");
		
		String riferimento = evento.getRiferimento();
		if (riferimento == null || riferimento.isEmpty())
			errors.reject("riferimento", "E' necessario indicare un riferimento al documento di lavoro per l'evento.");

	}

}
