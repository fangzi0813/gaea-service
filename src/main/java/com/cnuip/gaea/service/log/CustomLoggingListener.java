package com.cnuip.gaea.service.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

import java.nio.charset.Charset;

public class CustomLoggingListener implements GenericApplicationListener {

    private static final String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} "
            + "${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingListener.class);

    private boolean addedCustomAppender;
    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        // this is the earliest 'event type' which is capable of exposing the application context
        return ApplicationPreparedEvent.class.isAssignableFrom(resolvableType.getRawClass());
    }

    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (!addedCustomAppender) {
            //Add log appender for Monitoring
            CustomLogbackAppender customLogbackAppender = new CustomLogbackAppender();
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            final ch.qos.logback.classic.Logger root = context.getLogger("ROOT");

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(context);
            encoder.setCharset(Charset.forName("UTF-8"));
            encoder.setPattern(OptionHelper.substVars(pattern, context));
            encoder.start();

            customLogbackAppender.setEncoder(encoder);
            customLogbackAppender.setContext(context);
            customLogbackAppender.setName("WebSocket Logger");
            customLogbackAppender.start();
            root.addAppender(customLogbackAppender);
            logger.info("Added custom log appender");
            addedCustomAppender = true;
        }
    }

    @Override
    public int getOrder() {
        // this must be higher than LoggingApplicationListener.DEFAULT_ORDER
        return Ordered.HIGHEST_PRECEDENCE + 21;
    }
}
