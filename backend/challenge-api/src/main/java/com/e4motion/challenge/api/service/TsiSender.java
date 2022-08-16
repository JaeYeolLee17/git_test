package com.e4motion.challenge.api.service;

import com.e4motion.challenge.common.domain.TsiFilterBy;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface TsiSender {

    SseEmitter subscribe(TsiFilterBy filterBy, String filterValue);

    void unsubscribe(String emitterId);

}
