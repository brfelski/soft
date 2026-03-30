package com.felski.soft.service.impl;

import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.ProdutoServicoRequest;
import com.felski.soft.dto.response.ProdutoServicoResponse;
import com.felski.soft.exception.BusinessException;
import com.felski.soft.exception.ResourceNotFoundException;
import com.felski.soft.repository.ItemPedidoRepository;
import com.felski.soft.repository.ProdutoServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServicoServiceImplTest {

    @Mock
    private ProdutoServicoRepository produtoServicoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private ProdutoServicoServiceImpl service;

    private ProdutoServico produto;
    private UUID produtoId;

    @BeforeEach
    void setUp() {
        produtoId = UUID.randomUUID();
        produto = new ProdutoServico("Notebook", new BigDecimal("3500.00"), TipoItem.PRODUTO);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProdutoComSucesso() {
        ProdutoServicoRequest request = new ProdutoServicoRequest("Notebook", new BigDecimal("3500.00"), TipoItem.PRODUTO);
        when(produtoServicoRepository.save(any())).thenReturn(produto);

        ProdutoServicoResponse response = service.criar(request);

        assertThat(response.nome()).isEqualTo("Notebook");
        assertThat(response.preco()).isEqualByComparingTo("3500.00");
        assertThat(response.ativo()).isTrue();
        verify(produtoServicoRepository, times(1)).save(any(ProdutoServico.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(produtoServicoRepository.findById(produtoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(produtoId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(produtoId.toString());
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao deletar produto vinculado a pedido")
    void deveLancarExcecaoAoDeletarProdutoVinculado() {
        when(produtoServicoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(itemPedidoRepository.existsByProdutoServicoId(produtoId)).thenReturn(true);

        assertThatThrownBy(() -> service.deletar(produtoId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("vinculado a pedidos");

        verify(produtoServicoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve desativar produto com sucesso")
    void deveDesativarProdutoComSucesso() {
        when(produtoServicoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoServicoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProdutoServicoResponse response = service.desativar(produtoId);

        assertThat(response.ativo()).isFalse();
    }
}
