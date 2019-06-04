package it.ltc.services.sede.controller.common;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
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

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IBrandDao;
import it.ltc.model.shared.json.cliente.Brand;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.prodotto.BrandDAOFactory;
import it.ltc.services.sede.validation.prodotto.BrandValidator;


@Controller
@RequestMapping("/brand")
public class BrandController extends RestController {
	
	private static final Logger logger = Logger.getLogger(BrandController.class);
	
	@Autowired
	private BrandDAOFactory factory;
	
	@Autowired
	private BrandValidator validatorBrand;
	
	@InitBinder("brand")
	protected void initBrandBinder(WebDataBinder binder) {
	    binder.setValidator(validatorBrand);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Brand> inserisci(@Valid @RequestBody Brand brand, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di inserimento brand: " + brand + "dall'utente: " + user.getUsername());
		IBrandDao dao = factory.getDao(user, risorsaCommessa);
		Brand entity = dao.inserisci(brand);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<Brand> response = new ResponseEntity<Brand>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Brand> modifica(@Valid @RequestBody Brand brand, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di modifica brand: " + brand + "dall'utente: " + user.getUsername());
		IBrandDao dao = factory.getDao(user, risorsaCommessa);
		Brand entity = dao.aggiorna(brand);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Brand> response = new ResponseEntity<Brand>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Brand> dismetti(@RequestBody Brand brand, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione brand: " + brand + "dall'utente: " + user.getUsername());
		IBrandDao dao = factory.getDao(user, risorsaCommessa);
		Brand entity = dao.elimina(brand);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Brand> response = new ResponseEntity<Brand>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Brand>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco brand dall'utente: " + user.getUsername());		
		IBrandDao dao = factory.getDao(user, risorsaCommessa, false);
		List<Brand> brands = dao.trovaTutte();
		HttpStatus status = brands.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Brand>> response = new ResponseEntity<List<Brand>>(brands, status);
		return response;
	}

}
