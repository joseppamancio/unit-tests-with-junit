package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PessoaTest {

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "DEV")
    void assercaoAgrupada(){
//        boolean isDevelopment = "DEV".equals(System.getenv("ENVIRONMENT"));
//        Assumptions.assumeTrue(isDevelopment, () -> "Abortando teste: Não deve ser executado em Prod");
        Pessoa pessoa = new Pessoa("Alex", "Silva");
        assertAll("Asserção de pessoa",
                () -> assertEquals("Alex", pessoa.getNome()),
                () -> assertEquals("Silva", pessoa.getSobrenome()));
    }

}