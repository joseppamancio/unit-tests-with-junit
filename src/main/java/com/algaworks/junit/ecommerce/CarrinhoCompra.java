package com.algaworks.junit.ecommerce;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CarrinhoCompra {

	private final Cliente cliente;
	private final List<ItemCarrinhoCompra> itens;

	public CarrinhoCompra(Cliente cliente) {
		this(cliente, new ArrayList<>());
	}

	public CarrinhoCompra(Cliente cliente, List<ItemCarrinhoCompra> itens) {
		Objects.requireNonNull(cliente);
		Objects.requireNonNull(itens);
		this.cliente = cliente;
		this.itens = new ArrayList<>(itens); //Cria lista caso passem uma imutável
	}

	public List<ItemCarrinhoCompra> getItens() {
		return new ArrayList<>(itens);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void adicionarProduto(Produto produto, int quantidade) {
		//quantidade não pode ser menor que 1
		Objects.requireNonNull(produto);
		validaQuantidade(quantidade);

		//deve incrementar a quantidade caso o produto já exista
		encontrarItemPeloProduto(produto)
				.ifPresentOrElse(item -> item.adicionarQuantidade(quantidade), ()-> adicionarNovoItem(produto, quantidade));
	}

	public void removerProduto(Produto produto) {
		Objects.requireNonNull(produto);

		//caso o produto não exista, deve retornar uma exception
		ItemCarrinhoCompra item = encontrarItemPeloProduto(produto).orElseThrow(RuntimeException::new);
		this.itens.remove(item);
	}

	public void aumentarQuantidadeProduto(Produto produto) {
		Objects.requireNonNull(produto);
		ItemCarrinhoCompra item = encontrarItemPeloProduto(produto).orElseThrow(RuntimeException::new);
		item.adicionarQuantidade(1);
	}

    public void diminuirQuantidadeProduto(Produto produto) {
		Objects.requireNonNull(produto);
		ItemCarrinhoCompra item = encontrarItemPeloProduto(produto).orElseThrow(RuntimeException::new);
		if (item.getQuantidade() == 1) {
			itens.remove(item);
		} else {
			item.subtrairQuantidade(1);
		}
	}

    public BigDecimal getValorTotal() {
		//implementar soma do valor total de todos itens
		return this.itens.stream()
				.map(ItemCarrinhoCompra::getValorTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	public int getQuantidadeTotalDeProdutos() {
		//retorna quantidade total de itens no carrinho
		//Exemplo: em um carrinho com 2 itens, com a quantidade 2 e 3 para cada ítem respetivamente, deve retornar 5
		return this.itens.stream()
				.map(ItemCarrinhoCompra::getQuantidade)
				.reduce(0, Integer::sum);
	}

	public void esvaziar() {
		this.itens.clear();
	}

	private void validaQuantidade(int quantidade) {
		if (quantidade <= 0) {
			throw new IllegalArgumentException();
		}
	}

	private void adicionarNovoItem(Produto produto, int quantidade) {
		this.itens.add(new ItemCarrinhoCompra(produto, quantidade));
	}

	private Optional<ItemCarrinhoCompra> encontrarItemPeloProduto(Produto produto) {
		return this.itens
				.stream()
				.filter(item -> item.getProduto().equals(produto))
				.findFirst();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CarrinhoCompra that = (CarrinhoCompra) o;
		return Objects.equals(itens, that.itens) && Objects.equals(cliente, that.cliente);
	}

	@Override
	public int hashCode() {
		return Objects.hash(itens, cliente);
	}
}