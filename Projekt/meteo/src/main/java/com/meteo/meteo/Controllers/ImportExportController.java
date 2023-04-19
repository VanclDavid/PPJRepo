package com.meteo.meteo.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.meteo.meteo.Models.MeasurementEntity;
import com.meteo.meteo.Models.StateEntity;
import com.meteo.meteo.Utils.DBUtils;
import com.meteo.meteo.Utils.ImportExportUtil;
import com.meteo.meteo.Utils.LogUtil;
import com.meteo.meteo.Utils.OpenWeatherApi;

@Controller
public class ImportExportController {
    @Autowired
    private LogUtil logger;

    @Autowired
    private OpenWeatherApi openWeatherApi;

    @Autowired
    private DBUtils dbUtils;

    @Autowired
    private ImportExportUtil importExportUtil;

    @GetMapping("/import-export")
    public String importExportForm(ModelMap modelMap) {
        this.presetView(modelMap);

        return "import-export";
    }

    @PostMapping("/remove-expired")
    public String deleteSubmit(@RequestParam(value = "datetime", required = false) String datetime, ModelMap modelMap) {
        this.presetView(modelMap);

        try {
            List<MeasurementEntity> data = this.dbUtils.deleteExpired(datetime);
            this.addResult(modelMap, "success",
                    String.format("Expired rows (%s) has been successfuly deleted.", data.size()));
        } catch (Exception e) {
            this.addResult(modelMap, "danger", e.getMessage());
            this.logger.logException(e);
        }
        return "import-export";
    }

    @PostMapping("/download")
    public String importSubmit(@RequestParam("town") String town, ModelMap modelMap) {
        this.presetView(modelMap);

        try {
            StateEntity stateEntity = openWeatherApi.downloadTown(town);
            openWeatherApi.downloadMeasurement(stateEntity);
            this.addResult(modelMap, "success", "Successfuly downloaded and saved to DB");
        } catch (Exception e) {
            this.addResult(modelMap, "danger", e.getMessage());
            this.logger.logException(e);
        }

        return "import-export";
    }

    @PostMapping("/import")
    public String importSubmit(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "update", required = false) Boolean update,
            ModelMap modelMap) {
        this.presetView(modelMap);

        try {
            this.importExportUtil.importCsv(file, (update != null) ? update : false);
            this.addResult(modelMap, "success", "Data successfuly imported to database.");
        } catch (Exception e) {
            this.addResult(modelMap, "danger", e.getMessage());
            this.logger.logException(e);
        }

        return "import-export";
    }

    @PostMapping("/export")
    public ResponseEntity<Resource> exportSubmit() {
        try {
            return this.importExportUtil.exportCsv();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    private void presetView(ModelMap modelMap) {
        modelMap.addAttribute("town", "");
        modelMap.addAttribute("file", null);
        modelMap.addAttribute("expiration", System.getenv("EXPIRATION_IN_DAYS"));
    }

    private void addResult(ModelMap modelMap, String status, String message) {
        modelMap.addAttribute("resultState", status);
        modelMap.addAttribute("resultMessage", message);
    }
}
