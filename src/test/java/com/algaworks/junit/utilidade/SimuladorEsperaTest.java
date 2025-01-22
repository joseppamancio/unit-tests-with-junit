package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorEsperaTest {

    @Test
    void deveEperarENaoDarTimeout () {
//        Assertions.assertTimeout(Duration.ofSeconds(10), () -> SimuladorEspera.esperar(Duration.ofSeconds(10)));
//        Assertions.assertTimeout(Duration.ofSeconds(10), () -> SimuladorEspera.esperar(Duration.ofMillis(10)));
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> SimuladorEspera.esperar(Duration.ofMillis(10)));
    }
}