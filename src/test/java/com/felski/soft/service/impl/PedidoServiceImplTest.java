package com.felski.soft.service.impl;

import com.felski.soft.domain.entity.ItemPedido;
import com.felski.soft.domain.entity.Pedido;
import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.AplicarDescontoRequest;
import com.felski.soft.dto.request.ItemPedidoRequest;
import com.felski.soft.dto.response.PedidoResponse;
import com.felski.soft.exception.BusinessException;
import com.felski.soft.exception.ResourceNotFoundException;
import com.felski.soft.repository.ItemPedidoRepository;
import com.felski.soft.repository.PedidoRepository;
import com.felski.soft.repository.ProdutoServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoServicoRepository produtoServicoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private PedidoServiceImpl service;

    private Pedido pedido;
    private UUID pedidoId;
    private ProdutoServico produto;
    private UUID produtoId;

    @BeforeEach
    void setUp() {
        pedidoId = UUID.randomUUID();
        pedido = new Pedido();
        ReflectionTestUtils.setField(pedido, "id", pedidoId);

        produtoId = UUID.randomUUID();
        produto = new ProdutoServico("Notebook", new BigDecimal("3500.00"), TipoItem.PRODUTO);
    }

    @Test
    @DisplayName("Deve listar todos os pedidos")
    void deveListarTodos() {
        Page<Pedido> page = new PageImpl<>(List.of(pedido));
        when(pedidoRepository.findAll(any(com.querydsl.core.types.Predicate.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        Page<PedidoResponse> response = service.listarTodos(StatusPedido.ABERTO, PageRequest.of(0, 10));

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).statusPedido()).isEqualTo(StatusPedido.ABERTO);
    }

    @Test
    @DisplayName("Deve buscar pedido por ID")
    void deveBuscarPorId() {
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.of(pedido));

        PedidoResponse response = service.buscarPorId(pedidoId);

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao buscar pedido inexistente")
    void deveFalharAoBuscarPorIdInexistente() {
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(pedidoId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void deveCriarPedido() {
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponse response = service.criar();

        assertThat(response).isNotNull();
        assertThat(response.statusPedido()).isEqualTo(StatusPedido.ABERTO);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve adicionar item com sucesso")
    void deveAdicionarItem() {
        ItemPedidoRequest request = new ItemPedidoRequest(produtoId, 2);
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.of(pedido));
        when(produtoServicoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse response = service.adicionarItem(pedidoId, request);

        assertThat(response.valorTotalPedido()).isEqualByComparingTo("7000.00");
    }

    @Test
    @DisplayName("Deve remover item com sucesso")
    void deveRemoverItem() {
        pedido.adicionarItem(produto, 1);
        ItemPedido item = pedido.getItens().get(0);
        UUID itemId = UUID.randomUUID();
        
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.of(pedido));
        when(itemPedidoRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse response = service.removerItem(pedidoId, itemId);

        assertThat(response.valorTotalPedido()).isEqualByComparingTo("0.00");
        assertThat(pedido.getItens()).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar erro ao remover item que não pertence ao pedido")
    void deveFalharAoRemoverItemDeOutroPedido() {
        Pedido outroPedido = new Pedido();
        ReflectionTestUtils.setField(outroPedido, "id", UUID.randomUUID());
        outroPedido.adicionarItem(produto, 1);
        ItemPedido item = outroPedido.getItens().get(0);
        UUID itemId = UUID.randomUUID();
        
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.of(pedido));
        when(itemPedidoRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> service.removerItem(pedidoId, itemId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("não pertence");
    }

    @Test
    @DisplayName("Deve aplicar desconto com sucesso")
    void deveAplicarDesconto() {
        pedido.adicionarItem(produto, 1);
        AplicarDescontoRequest request = new AplicarDescontoRequest(new BigDecimal("10.00"));
        
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse response = service.aplicarDesconto(pedidoId, request);

        assertThat(response.valorTotalPedido()).isEqualByComparingTo("3150.00");
    }

    @Test
    @DisplayName("Deve fechar pedido com sucesso")
    void deveFecharPedido() {
        pedido.adicionarItem(produto, 1);
        
        when(pedidoRepository.findByIdWithItens(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse response = service.fechar(pedidoId);

        assertThat(response.statusPedido()).isEqualTo(StatusPedido.FECHADO);
    }
}
