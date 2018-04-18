package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.CategoriaMerceologicaDao;
import it.ltc.database.model.centrale.CategoriaMerceologica;

@Controller
@RequestMapping("/categoriamerceologica")
public class CategoriaMerceologicaController {
	
	private final CategoriaMerceologicaDao dao;
    
    public CategoriaMerceologicaController() {
    	dao = CategoriaMerceologicaDao.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<CategoriaMerceologica> trovaTutte() {
        return dao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody CategoriaMerceologica trovaDaId(@PathVariable("id") String id) {
        return dao.findByNome(id);
    }

}
