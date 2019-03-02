package com.cnuip.gaea.service.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.logging.LogFile;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import java.nio.charset.Charset;
import java.util.Properties;

public class CustomLoggingListener implements GenericApplicationListener {

    private static final String PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} "
            + "${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";

    private static final String FILE_LOG_PATTERN = "%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} "
            + "${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingListener.class);

    private boolean addedCustomAppender;

    private boolean addedRollingFileAppender;

    private String artifact;

    public CustomLoggingListener(Properties p) {
        this.artifact = p.getProperty("build.artifact");
    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        // this is the earliest 'event type' which is capable of exposing the application context
        return ApplicationPreparedEvent.class.isAssignableFrom(resolvableType.getRawClass());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ch.qos.logback.classic.Logger root = context.getLogger("ROOT");
        LogFile logFile = LogFile.get(((ApplicationPreparedEvent) applicationEvent).getApplicationContext().getEnvironment());
        if (!addedCustomAppender) {
            //Add log appender for Monitoring
            CustomLogbackAppender customLogbackAppender = new CustomLogbackAppender();

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(context);
            encoder.setCharset(Charset.forName("UTF-8"));
            encoder.setPattern(OptionHelper.substVars(PATTERN, context));
            encoder.start();

            customLogbackAppender.setEncoder(encoder);
            customLogbackAppender.setContext(context);
            customLogbackAppender.setName("WebSocket Logger");
            customLogbackAppender.start();
            root.addAppender(customLogbackAppender);
            logger.info("Added custom log appender");
            addedCustomAppender = true;
        }
        if (!addedRollingFileAppender) {
            root.detachAppender("FILE");
            String logFileStr = "./logs/" + artifact + ".log";
            if (logFile != null) {
                logFileStr = logFile.toString() + artifact + ".log";
            }
            RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
            appender.setContext(context);

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setPattern(OptionHelper.substVars(FILE_LOG_PATTERN, context));
            encoder.setContext(context);
            encoder.start();

            SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
            rollingPolicy.setFileNamePattern(logFileStr + ".%d{yyyy-MM-dd}.%i.gz");
            rollingPolicy.setMaxFileSize(FileSize.valueOf("500MB"));
            rollingPolicy.setMaxHistory(30);
            rollingPolicy.setParent(appender);
            rollingPolicy.setContext(context);
            rollingPolicy.start();

            appender.setRollingPolicy(rollingPolicy);
            appender.setEncoder(encoder);
            appender.setName("RollingFile Logger");
            appender.setFile(logFileStr);
            appender.start();
            root.addAppender(appender);

            logger.info("Added rolling file log appender");
            addedRollingFileAppender = true;
        }
    }

    @Override
    public int getOrder() {
        // this must be higher than LoggingApplicationListener.DEFAULT_ORDER
        return Ordered.HIGHEST_PRECEDENCE + 21;
    }
}
