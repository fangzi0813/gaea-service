package com.cnuip.gaea.service.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.cnuip.gaea.service.utils.SpringUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class CustomLogbackAppender extends AppenderBase<ILoggingEvent> {

    private PatternLayoutEncoder encoder;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        String log = encoder.getLayout().doLayout(iLoggingEvent);
        if (simpMessagingTemplate == null) {
            simpMessagingTemplate = SpringUtil.getBean(SimpMessagingTemplate.class);
        }
        if (simpMessagingTemplate != null) {
            if (null != log) {
                log  = log.replaceAll("\\n", "<br/>")
                        .replaceAll("<br/>\\s*<br/>", "<br/>")
                        .replaceAll("\\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
                        .replaceAll("\\s", "&nbsp;");
            }
            simpMessagingTemplate.convertAndSend("/topic/logListener", log);
        }
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
