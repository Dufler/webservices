package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.costanti.ValutaDao;
import it.ltc.database.model.costanti.Valuta;

@Controller
@RequestMapping("/valuta")
public class ValutaController {
	
	private final ValutaDao dao;
    
    public ValutaController() {
    	dao = new ValutaDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Valuta> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Valuta lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaCodice(id);
    }

}
