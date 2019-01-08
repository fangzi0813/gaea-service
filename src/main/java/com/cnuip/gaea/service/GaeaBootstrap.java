package com.cnuip.gaea.service;

import com.cnuip.gaea.service.log.CustomLoggingListener;
import com.github.lalyos.jfiglet.FigletFont;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
public class GaeaBootstrap {

    public GaeaBootstrap() {
    }

    public static void run(Class<?>[] primarySources, String[] args) {
        SpringApplication application = new SpringApplication(primarySources);
        Properties properties = new Properties();
        InputStream resource = GaeaBootstrap.class.getClassLoader().getResourceAsStream("META-INF/build-info.properties");
        if (null != resource) {
            try {
                properties.load(resource);
                application.setBanner((environment, sourceClass, out) -> out.println(FigletFont.convertOneLine(properties.getProperty("build.artifact"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        application.addPrimarySources(Collections.singleton(GaeaBootstrap.class));
        application.addListeners(new CustomLoggingListener(properties));
        application.run(args);
    }

    public static void run(Class<?> primarySources, String[] args) {
        run(new Class[]{primarySources}, args);
    }
}
