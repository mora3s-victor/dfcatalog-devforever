package com.devforever.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	//CONFIGURAÇÃO PARA LIBERAR O ACESSO Á QUALQUER ENDPOINT
        return (web) -> web.ignoring().requestMatchers("/**");
    }

}