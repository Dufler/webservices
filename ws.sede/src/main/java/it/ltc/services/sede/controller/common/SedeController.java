package it.ltc.services.sede.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.utente.SedeUtentiDao;
import it.ltc.database.model.utente.SedeUtenti;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/sede")
public class SedeController extends RestController {
	
	@Autowired
	private SedeUtentiDao dao;
    
    public SedeController() {}

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<SedeUtenti> listAllMembers() {
        return dao.trovaTutti();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody SedeUtenti lookupMemberById(@PathVariable("id") int id) {
        return dao.trovaDaID(id);
    }

}
