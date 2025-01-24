package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes no utilitário de saudação")
class SaudacaoUtilTest {

    @Test
    @DisplayName("Dado uma hora valida deve saudar com bom dia")
    public void dadoUmHorarioMatutino_QuandoSaudar_EntaoDeveRetornarBomDia() {
        int horaValida = 9;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Bom dia", saudacao, "Saudação Incorreta!");
    }

    @Test
    @DisplayName("Dado uma hora valida deve saudar com boa tarde")
    public void dadoUmHorarioVespertino_QuandoSaudar_EntaoDeveRetornarBoaTarde() {
        int horaValida = 14;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Boa tarde", saudacao, "Saudação Incorreta!");
    }

    @Test
    @DisplayName("Dado uma hora valida deve saudar com boa noite")
    public void dado_um_horario_noturno_quando_saudar_entao_deve_retornar_boa_noite() {
        int horaValida = 22;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Boa noite", saudacao, "Saudação Incorreta!");
    }

    @Test
    @DisplayName("Dado uma hora invalida Quando saudar Entao deve lancar uma excecao")
    public void dado_uma_hora_invalida_quando_saudar_entao_deve_lancar_exception() {
        int horaInvalida = -10;
        Executable chamadaMetodo = () -> SaudacaoUtil.saudar(horaInvalida);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, chamadaMetodo);
        assertEquals("Hora inválida", exception.getMessage());
    }

    @Test
    public void dadoUmaHoraValida_QuandoSaudar_EntaoNaoDeveLancarException() {
        int horaValida = 0;
        Executable chamadaMetodo = () -> SaudacaoUtil.saudar(horaValida);
        assertDoesNotThrow(chamadaMetodo);
    }
}