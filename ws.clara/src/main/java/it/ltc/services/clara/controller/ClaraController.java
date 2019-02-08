package it.ltc.services.clara.controller;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.services.clara.client.ClaraClient;
import it.ltc.services.clara.model.FileStep;
import it.ltc.services.clara.model.Scena;
import it.ltc.services.custom.exception.CustomException;

@Controller
@RequestMapping("/scena")
public class ClaraController /*extends RestController*/ {
	
	//public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("ClaraController"); 
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Scena> nuovaScena(@RequestBody FileStep file) {
		logger.info("Nuova scena clara");
		//logger.info(file.getContenuto());
		ClaraClient client = new ClaraClient();
		Scena scena = client.nuovaScena();
		boolean creazione = client.getStatus() == 200;
		boolean caricamento = false;
		boolean download = false;
		logger.info("Esito creazione scena: " + creazione);
		if (creazione) {
			client.caricaFileTramiteForm(scena.getId(), file.getContenuto());
			caricamento = client.getStatus() == 202;
			logger.info("Esito caricamento scena: " + caricamento);
			if (!caricamento)
				throw new CustomException("Impossibile caricare il file. Errore: " + client.getStatus() + ", " + client.getError());
			else {
				String json = client.scaricaJSON(scena.getId());
				download = client.getStatus() == 200;
				if (download) {
					//restituisco il json.
				}
			}
		}
		HttpStatus status = caricamento ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Scena> response = new ResponseEntity<Scena>(scena, status);
		return response;
	}
	
//	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
//	public ResponseEntity<Scena> nuovaScena(@RequestParam(value = "filedata", required = true) MultipartFile filedata, final HttpServletRequest request) {
//		logger.info("Nuova scena clara");
//		InputStream is = null;
//	  	if (filedata == null) {
//	    	is = request.getInputStream();
//	  	} else {
//	    	is = filedata.getInputStream();
//	  	}
//	  	byte[] bytes = IOUtils.toByteArray(is);
//	  	System.out.println("read " + bytes.length + " bytes.");
//		Scena scena = new Scena();
//		scena.setId("1");
//		HttpStatus status = scena != null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
//		ResponseEntity<Scena> response = new ResponseEntity<Scena>(scena, status);
//		return response;
//	}

}
