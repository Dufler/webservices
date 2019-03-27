package it.ltc.services.sede.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.utente.CommessaUtentiDao;
import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/commessa")
public class CommessaController extends RestController {
	
	@Autowired
    private CommessaUtentiDao dao;
    
    public CommessaController() {}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody CommessaUtenti trovaDaID(@PathVariable("id") Integer id) {
        return dao.trovaDaID(id);
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CommessaUtenti>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		List<CommessaUtenti> entities = dao.trovaTutti();
		ResponseEntity<List<CommessaUtenti>> response = new ResponseEntity<List<CommessaUtenti>>(entities, HttpStatus.OK);
		return response;
	}

}
