package com.cnuip.gaea.service.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LogController {

    @Autowired
    BuildProperties buildProperties;

    @GetMapping("/log")
    public String log(Model model) {
        model.addAttribute("plugin", buildProperties.getArtifact());
        model.addAttribute("version", buildProperties.getVersion());
        return "/ws2.jsp";
    }
}
