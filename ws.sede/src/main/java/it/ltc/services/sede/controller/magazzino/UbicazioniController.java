package it.ltc.services.sede.controller.magazzino;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/ubicazioni")
public class UbicazioniController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("UbicazioniController");
	
	

}
