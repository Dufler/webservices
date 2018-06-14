package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.ProvinciaDao;
import it.ltc.database.model.centrale.Provincia;

@Controller
@RequestMapping("/provincia")
public class ProvinceController {
	
	private final ProvinciaDao dao;
    
    public ProvinceController() {
    	dao = new ProvinciaDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Provincia> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Provincia lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaSigla(id);
    }

}
