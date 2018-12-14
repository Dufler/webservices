package it.ltc.services.logica.controller.crm;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.services.custom.controller.RestController;
import it.ltc.services.logica.data.crm.RicercaCrmDAO;
import it.ltc.services.logica.model.crm.FiltroTesto;
import it.ltc.services.logica.model.crm.RisultatoRicercaCrm;

@Controller
@RequestMapping("/crm/cerca")
public class RicercaCrmController extends RestController {
	
	@Autowired
	private RicercaCrmDAO dao;
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RisultatoRicercaCrm> trovaDaTesto(@Valid @RequestBody FiltroTesto filtro, @RequestHeader("authorization") String authenticationString) {
		RisultatoRicercaCrm results = dao.trovaParolaChiave(filtro.getTesto());
		ResponseEntity<RisultatoRicercaCrm> response = new ResponseEntity<RisultatoRicercaCrm>(results, HttpStatus.OK);
		return response;
	}

}
