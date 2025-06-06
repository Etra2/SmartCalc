package com.kalkulator.config;

import com.kalkulator.parser.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {

    @Bean
    public Parser parser() {
        return new Parser();  // bezargumentowy konstruktor
    }
}
