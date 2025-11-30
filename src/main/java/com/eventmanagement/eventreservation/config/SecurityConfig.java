package com.eventmanagement.eventreservation.config;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 1. Autoriser explicitement TOUTES les requêtes (le "tout")
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );

        // 2. Désactiver le CSRF
        // Nécessaire car Spring Security l'active par défaut et peut interférer avec certaines requêtes.
        // C'est également un prérequis pour l'accès à la console H2.
        http.csrf(csrf -> csrf.disable());

        // 3. Désactiver la protection Iframe (X-Frame-Options)
        // ESSENTIEL pour que la console H2 (qui est affichée dans un iframe) fonctionne dans le navigateur.
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        // Note: super.configure(http) est omis ici car .anyRequest().permitAll() rend l'appel parent (qui configure le login Vaadin) inutile.
        // Si vous voulez conserver les configurations de base de Vaadin, assurez-vous de placer votre règle .anyRequest().permitAll() APRES l'appel à super.configure(http).
        // Cependant, pour simplifier et garantir que TOUT est permis, nous le laissons ainsi.

        // Si vous souhaitez conserver la configuration Vaadin par défaut (ressources statiques) 
        // tout en autorisant toutes les requêtes APRÈS elles, vous pouvez appeler super.configure(http) ici :
        // super.configure(http);
    }
}