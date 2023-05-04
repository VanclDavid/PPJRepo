package com.meteo.meteo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/data-view")
    public String importExportForm(ModelMap modelMap) {
        return "data-view";
    }
}
