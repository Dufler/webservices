package it.ltc.services.logica.controller.common;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.shared.prodotti.CategoriaMerceologicaDaoImpl;
import it.ltc.model.shared.json.interno.CategoriaMerceologicaJSON;
import it.ltc.services.logica.validation.fatturazione.CategoriaMerceologicaValidator;

@Controller
@RequestMapping("/categoriamerceologica")
public class CategoriaMerceologicaController {
	
	private static final Logger logger = Logger.getLogger(CategoriaMerceologicaController.class);
	
	@Autowired
	private CategoriaMerceologicaDaoImpl dao;
	
	@Autowired
    private CategoriaMerceologicaValidator validator;
    
    @InitBinder("categoriaMerceologicaJSON")
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
    
    public CategoriaMerceologicaController() {}

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<CategoriaMerceologicaJSON> trovaTutte() {
        return dao.trovaTutti();
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CategoriaMerceologicaJSON> inserisci(@Valid @RequestBody CategoriaMerceologicaJSON categoria, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova entity.");
		CategoriaMerceologicaJSON entity = dao.inserisci(categoria);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CategoriaMerceologicaJSON> response = new ResponseEntity<CategoriaMerceologicaJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CategoriaMerceologicaJSON> modifica(@Valid @RequestBody CategoriaMerceologicaJSON categoria, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una entity.");
		CategoriaMerceologicaJSON entity = dao.aggiorna(categoria);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CategoriaMerceologicaJSON> response = new ResponseEntity<CategoriaMerceologicaJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CategoriaMerceologicaJSON> elimina(@Valid @RequestBody CategoriaMerceologicaJSON categoria, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una entity.");
		CategoriaMerceologicaJSON entity = dao.elimina(categoria);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CategoriaMerceologicaJSON> response = new ResponseEntity<CategoriaMerceologicaJSON>(entity, status);
		return response;
	}

}
