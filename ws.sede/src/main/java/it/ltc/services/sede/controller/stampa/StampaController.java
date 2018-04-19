package it.ltc.services.sede.controller.stampa;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.services.sede.model.stampa.RichiestaDiStampa;
import it.ltc.services.sede.validation.stampa.RichiestaDiStampaValidator;

@Controller
@RequestMapping("/stampa")
public class StampaController extends ServizioDiStampa {
	
	@Autowired
	private RichiestaDiStampaValidator validatorRichiesta;
	
	@InitBinder("richiestaDiStampa")
	protected void initRichiestaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorRichiesta);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/plain", consumes="text/plain", value="/cerca")
	public ResponseEntity<String> trovaPerCommessaEData(@Valid @RequestBody RichiestaDiStampa richiesta, @RequestHeader("authorization") String authenticationString) {
		String risposta = null;
		boolean stampato = false;
		int tentativi = 0;
		while (!stampato && tentativi < 3) {
			tentativi += 1;
			risposta = inviaStampa(richiesta.getIp(), richiesta.getContent());
			stampato = (risposta == null);
		}
		HttpStatus status = stampato ? HttpStatus.OK : HttpStatus.GATEWAY_TIMEOUT;
		ResponseEntity<String> response = new ResponseEntity<String>(risposta, status);
		return response;
	}

}
