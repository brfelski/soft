package com.felski.soft.domain.entity;

import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "produto_servico")
public class ProdutoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", nullable = false, length = 20)
    private TipoItem tipoItem;

    @Version
    private Integer version;

    protected ProdutoServico() {
    }

    public ProdutoServico(String nome, BigDecimal preco, TipoItem tipoItem) {
        this.nome = nome;
        this.setPreco(preco);
        this.tipoItem = tipoItem;
        this.ativo = true;
    }

    public void atualizarPreco(BigDecimal novoPreco) {
        this.setPreco(novoPreco);
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    private void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço deve ser maior que zero.");
        }
        this.preco = preco;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public Integer getVersion() {
        return version;
    }
}
