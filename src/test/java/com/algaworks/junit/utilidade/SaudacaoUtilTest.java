package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;

class SaudacaoUtilTest {

    @Test
    public void saudarBomDia() {
        String saudacao = SaudacaoUtil.saudar(9);
        assertEquals("Bom dia", saudacao, "Saudação Incorreta!");
    }

    @Test
    public void saudarBoaTarde() {
        String saudacao = SaudacaoUtil.saudar(14);
        assertEquals("Boa tarde", saudacao, "Saudação Incorreta!");
    }

    @Test
    public void saudarBoaNoite() {
        String saudacao = SaudacaoUtil.saudar(22);
        assertEquals("Boa noite", saudacao, "Saudação Incorreta!");
    }

    @Test
    public void deveLancarExcecao() {
        Executable executable = () -> SaudacaoUtil.saudar(-10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Hora inválida", exception.getMessage());
    }

    @Test
    public void naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> SaudacaoUtil.saudar(0));
    }
}