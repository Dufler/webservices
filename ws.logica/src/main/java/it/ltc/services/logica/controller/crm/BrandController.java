package it.ltc.services.logica.controller.crm;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.Brand;
import it.ltc.services.logica.data.crm.BrandDAO;
import it.ltc.services.logica.model.crm.FiltroTesto;
import it.ltc.services.logica.validation.crm.BrandValidator;
import it.ltc.services.logica.validation.crm.FiltroTestoValidator;

@Controller
@RequestMapping("/crm/brand")
public class BrandController {
	
	private static final Logger logger = Logger.getLogger("BrandController");
	
	@Autowired
	private BrandDAO dao;
	
	@Autowired
	private BrandValidator validatorBrand;
	
	@Autowired
	private FiltroTestoValidator validatorFiltro;
	
	@InitBinder("brand")
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validatorBrand);
	}
	
	@InitBinder("filtroTesto")
	protected void initFiltroBinder(WebDataBinder binder) {
	    binder.setValidator(validatorFiltro);
	}
	
	public BrandController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Brand>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i brand.");
		List<Brand> entities = dao.trovaTutti();
		ResponseEntity<List<Brand>> response = new ResponseEntity<List<Brand>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/azienda/{id}")
	public ResponseEntity<List<Brand>> trovaDaBrand(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutti i brand afferenti alla data azienda.");
		List<Brand> entities = dao.trovaDaAzienda(id);
		ResponseEntity<List<Brand>> response = new ResponseEntity<List<Brand>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<Brand>> trovaDaNome(@Valid @RequestBody FiltroTesto filtro, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i brand con un dato nome.");
		List<Brand> entities = dao.cercaDaNome(filtro.getTesto());
		ResponseEntity<List<Brand>> response = new ResponseEntity<List<Brand>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Brand> inserisci(@Valid @RequestBody Brand brand, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo brand.");
		Brand entity = dao.inserisci(brand);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Brand> response = new ResponseEntity<Brand>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Brand> modifica(@Valid @RequestBody Brand brand, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un brand.");
		Brand entity = dao.aggiorna(brand);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Brand> response = new ResponseEntity<Brand>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Brand> elimina(@RequestBody Brand brand, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un brand.");
		Brand entity = dao.elimina(brand);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Brand> response = new ResponseEntity<Brand>(entity, status);
		return response;
	}

}
