package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.shared.prodotti.CategoriaMerceologicaDaoImpl;
import it.ltc.model.shared.json.interno.CategoriaMerceologicaJSON;

@Controller
@RequestMapping("/categoriamerceologica")
public class CategoriaMerceologicaController {
	
	@Autowired
	private CategoriaMerceologicaDaoImpl dao;
    
    public CategoriaMerceologicaController() {}

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<CategoriaMerceologicaJSON> trovaTutte() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody CategoriaMerceologicaJSON trovaDaId(@PathVariable("id") String id) {
        return dao.trovaDaNome(id);
    }

}
