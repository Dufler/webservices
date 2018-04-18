package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.SedeDao;
import it.ltc.database.model.centrale.Sede;

@Controller
@RequestMapping("/sede")
public class SedeController {
	
	private final SedeDao dao;
    
    public SedeController() {
    	dao = SedeDao.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Sede> listAllMembers() {
        return dao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Sede lookupMemberById(@PathVariable("id") int id) {
        return dao.findByID(id);
    }

}
