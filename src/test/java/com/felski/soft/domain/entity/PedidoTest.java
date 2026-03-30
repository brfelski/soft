package com.felski.soft.domain.entity;

import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class PedidoTest {

    private Pedido pedido;
    private ProdutoServico produto;
    private ProdutoServico servico;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        produto = new ProdutoServico("Notebook", new BigDecimal("3000.00"), TipoItem.PRODUTO);
        servico = new ProdutoServico("Suporte Técnico", new BigDecimal("500.00"), TipoItem.SERVICO);
    }

    @Test
    @DisplayName("Desconto deve incidir apenas sobre produtos, não sobre serviços")
    void descontoDeveIncidirApenasEmProdutos() {
        pedido.adicionarItem(produto, 1);
        pedido.adicionarItem(servico, 1);
        pedido.aplicarDesconto(new BigDecimal("10"));

        assertThat(pedido.getValorTotalDesconto()).isEqualByComparingTo("300.00");
        assertThat(pedido.getValorTotalPedido()).isEqualByComparingTo("3200.00");
    }

    @Test
    @DisplayName("Não deve adicionar item a pedido FECHADO")
    void naoDeveAdicionarItemEmPedidoFechado() {
        pedido.adicionarItem(produto, 1);
        pedido.fechar();

        assertThatThrownBy(() -> pedido.adicionarItem(servico, 1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("FECHADO");
    }

    @Test
    @DisplayName("Não deve adicionar produto inativo")
    void naoDeveAdicionarProdutoInativo() {
        produto.desativar();
 
        assertThatThrownBy(() -> pedido.adicionarItem(produto, 1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("desativado");
    }

    @Test
    @DisplayName("Não deve fechar pedido sem itens")
    void naoDeveFecharPedidoVazio() {
        assertThat(pedido.getItens()).isEmpty();
        assertThat(pedido.getStatusPedido()).isEqualTo(StatusPedido.ABERTO);
    }

    @Test
    @DisplayName("Remover item deve recalcular totais")
    void removerItemDeveRecalcularTotais() {
        pedido.adicionarItem(produto, 2);
        pedido.adicionarItem(servico, 1);
        assertThat(pedido.getValorTotalPedido()).isEqualByComparingTo("6500.00");

        pedido.removerItem(pedido.getItens().get(0));
        assertThat(pedido.getValorTotalPedido()).isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("precoUnitario no item deve ser snapshot do momento da adição")
    void precoUnitarioDeveSerSnapshotNoMomentoDeAdicao() {
        pedido.adicionarItem(produto, 1);
        BigDecimal precoNaAdicao = produto.getPreco();

        produto.atualizarPreco(new BigDecimal("9999.00"));

        assertThat(pedido.getItens().get(0).getPrecoUnitario())
                .isEqualByComparingTo(precoNaAdicao);
    }
}
