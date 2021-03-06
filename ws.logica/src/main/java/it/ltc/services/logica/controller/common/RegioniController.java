package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.costanti.RegioneDao;
import it.ltc.database.model.costanti.Regione;

@Controller
@RequestMapping("/regione")
public class RegioniController {
	
	private final RegioneDao dao;
    
    public RegioniController() {
    	dao = new RegioneDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Regione> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Regione lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaSigla(id);
    }

}
