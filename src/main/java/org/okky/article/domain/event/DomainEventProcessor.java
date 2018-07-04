package org.okky.article.domain.event;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.okky.article.domain.service.ArticleProxy;
import org.okky.share.event.ArticleScrapped;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
class DomainEventProcessor {
    ArticleProxy proxy;

    @EventListener
    void when(ArticleScrapped event) {
        proxy.sendEvent(event);
    }
}

@Aspect
@Component
@Slf4j
class LogAspect {
    @After("execution(void org.okky.article.domain.event.DomainEventProcessor.when(..))")
    void logging(JoinPoint jp) {
        String event = jp.getArgs()[0].getClass().getSimpleName();

        logger.info("Event: {}", event);
    }
}