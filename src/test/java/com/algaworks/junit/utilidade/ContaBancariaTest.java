package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

    @Nested
    class Saque {
        @Test
        void saque() {
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("70.00"));
            contaBancaria.saque(new BigDecimal("50.01"));
            assertEquals(new BigDecimal("19.99"), contaBancaria.saldo());
        }

        @Test
        void saqueComValorZeroFalha(){
            ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
            assertThrows(IllegalArgumentException.class, () -> contaBancaria.saque(BigDecimal.ZERO));
        }

        @Test
        void saqueComValorNegativo(){
            ContaBancaria contaBancaria = new ContaBancaria(BigDecimal.TEN);
            assertThrows(IllegalArgumentException.class, () -> contaBancaria.saque(new BigDecimal("-10.00")));
        }

        @Test
        void saqueComValorMaiorFalha() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            assertThrows(RuntimeException.class, ()-> conta.saque(new BigDecimal("20.0")));
        }

        @Test
        void saqueComValorIgualNaoFalha() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            conta.saque(new BigDecimal("10.00"));
            assertEquals(new BigDecimal("0.00"), conta.saldo());
        }

        @Test
        void saqueComValorNullFalha() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            assertThrows(IllegalArgumentException.class, ()-> conta.saque(null));
        }
    }

    @Nested
    class Deposito{
        @Test
        void deposito() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            conta.deposito(BigDecimal.TEN);
            assertEquals(new BigDecimal("20"), conta.saldo());
        }

        @Test
        void depositoComValorZeradoFalha() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            assertThrows(IllegalArgumentException.class, ()-> conta.deposito(BigDecimal.ZERO));
        }

        @Test
        void depositoComValorNegativoFalha() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            assertThrows(IllegalArgumentException.class, ()-> conta.deposito(new BigDecimal("-10.0")));
        }

        @Test
        void depositoComValorNullFalha() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            assertThrows(IllegalArgumentException.class, ()-> conta.deposito(null));
        }

        @Test
        void saqueAposDeposito() {
            ContaBancaria conta = new ContaBancaria(BigDecimal.TEN);
            conta.deposito(BigDecimal.TEN);
            conta.saque(new BigDecimal("5"));
            assertEquals(new BigDecimal("15"), conta.saldo());
        }
    }

    @Nested
    class Saldo{
        @Test
        void saldo() {
            ContaBancaria conta = new ContaBancaria(new BigDecimal("29.90"));
            assertEquals(new BigDecimal("29.90"), conta.saldo());
        }

        @Test
        void criarContaComSaldoNullFalha() {
            assertThrows(IllegalArgumentException.class, ()-> new ContaBancaria(null));
        }
    }
}