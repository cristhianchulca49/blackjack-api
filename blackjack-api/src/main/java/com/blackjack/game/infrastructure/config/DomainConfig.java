package com.blackjack.game.infrastructure.config;

import com.blackjack.game.domain.service.BlackjackDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public BlackjackDomainService blackjackDomainService() {
        return new BlackjackDomainService();
    }
}