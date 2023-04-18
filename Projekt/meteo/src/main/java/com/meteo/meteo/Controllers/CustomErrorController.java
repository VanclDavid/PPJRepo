package com.meteo.meteo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.meteo.meteo.Utils.LogUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @Autowired
    private LogUtil logger;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, ModelMap modelMap) {
        Object codeStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (codeStatus != null) {
            modelMap.addAttribute("errorCode", Integer.valueOf(codeStatus.toString()));

            Object messageStatus = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
            Object uriStatus = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

            logger.logWarning(String.format("HTML ERROR: %s, URL: %s, descr: %s",
                    codeStatus.toString(),
                    uriStatus.toString(),
                    messageStatus.toString()));
        }

        return "error";
    }
}