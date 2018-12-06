package com.cnuip.gaea.service;

import com.github.lalyos.jfiglet.FigletFont;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
public class GaeaBootstrap {

    private GaeaBootstrap() {
    }

    public static void run(Class<?>[] primarySources, String[] args) {
        SpringApplication application = new SpringApplication(primarySources);
        application.addPrimarySources(Collections.singleton(GaeaBootstrap.class));
        Properties properties = new Properties();
        InputStream resource = GaeaBootstrap.class.getClassLoader().getResourceAsStream("META-INF/build-info.properties");
        if (null != resource) {
            try {
                properties.load(resource);
                application.setBanner((environment, sourceClass, out) -> out.println(FigletFont.convertOneLine(String.valueOf(properties.get("build.artifact")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        application.run(args);
    }
}
