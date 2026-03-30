package com.felski.soft.domain.entity;

import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", nullable = false, length = 20)
    private StatusPedido statusPedido = StatusPedido.ABERTO;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(name = "percentual_desconto", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualDesconto = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);

    @NotNull
    @Column(name = "valor_total_desconto", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotalDesconto = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);

    @NotNull
    @Column(name = "valor_total_pedido", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotalPedido = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Version
    private Integer version;

    public Pedido() {
    }

    public void adicionarItem(ProdutoServico produto, Integer quantidade) {
        validarEstadoAberto();
        ItemPedido novoItem = new ItemPedido(this, produto, quantidade);
        this.itens.add(novoItem);
        recalcularTotais();
    }

    public void removerItem(ItemPedido item) {
        validarEstadoAberto();
        if (itens.remove(item)) {
            recalcularTotais();
        }
    }

    public void aplicarDesconto(BigDecimal novoPercentual) {
        validarEstadoAberto();
        if (novoPercentual == null || novoPercentual.compareTo(BigDecimal.ZERO) < 0
                || novoPercentual.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException("Percentual de desconto deve estar entre 0 e 100.");
        }
        this.percentualDesconto = novoPercentual.setScale(2, RoundingMode.HALF_EVEN);
        recalcularTotais();
    }

    public void fechar() {
        validarEstadoAberto();
        if (itens.isEmpty()) {
            throw new BusinessException("Não é possível fechar um pedido sem itens.");
        }
        this.statusPedido = StatusPedido.FECHADO;
    }

    private void recalcularTotais() {
        BigDecimal baseCalculoDesconto = itens.stream()
                .filter(item -> item.getProdutoServico().getTipoItem() == TipoItem.PRODUTO)
                .map(ItemPedido::getValorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalServicos = itens.stream()
                .filter(item -> item.getProdutoServico().getTipoItem() == TipoItem.SERVICO)
                .map(ItemPedido::getValorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotalDesconto = baseCalculoDesconto.multiply(percentualDesconto)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);

        this.valorTotalPedido = baseCalculoDesconto.subtract(valorTotalDesconto)
                .add(totalServicos)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private void validarEstadoAberto() {
        if (this.statusPedido != StatusPedido.ABERTO) {
            throw new BusinessException("Operação não permitida: O pedido já está FECHADO.");
        }
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public UUID getId() {
        return id;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public BigDecimal getValorTotalDesconto() {
        return valorTotalDesconto;
    }

    public BigDecimal getValorTotalPedido() {
        return valorTotalPedido;
    }

    public Integer getVersion() {
        return version;
    }
}
