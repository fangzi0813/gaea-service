package com.cnuip.gaea.service.log;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {

    private BuildProperties buildProperties;

    public LogController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/log")
    public String log(Model model) {
        model.addAttribute("plugin", buildProperties.getArtifact());
        model.addAttribute("version", buildProperties.getVersion());
        return "ws2";
    }
}
