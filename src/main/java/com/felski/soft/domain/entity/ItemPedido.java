package com.felski.soft.domain.entity;

import com.felski.soft.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_servico_id", nullable = false)
    private ProdutoServico produtoServico;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoUnitario;

    @Version
    private Integer version;

    public ItemPedido(Pedido pedido, ProdutoServico produtoServico, Integer quantidade) {
        if (!produtoServico.getAtivo()) {
            throw new BusinessException("Produto/Serviço desativado: " + produtoServico.getNome());
        }
        if (quantidade == null || quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero.");
        }
        this.pedido = pedido;
        this.produtoServico = produtoServico;
        this.quantidade = quantidade;
        this.precoUnitario = produtoServico.getPreco();
    }

    public BigDecimal getValorTotalItem() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public UUID getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public ProdutoServico getProdutoServico() {
        return produtoServico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public Integer getVersion() {
        return version;
    }
}
