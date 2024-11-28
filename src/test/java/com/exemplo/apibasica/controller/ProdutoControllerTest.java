package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.ProdutoDTO;
import com.exemplo.apibasica.model.Produto;
import com.exemplo.apibasica.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void deveListarTodosProdutos() {
        List<Produto> produtos = List.of(
                new Produto("Produto1", 10.0),
                new Produto("Produto2", 20.0)
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<Produto> paginaDeProdutos = new PageImpl<>(produtos, pageable, produtos.size());

        when(produtoRepository.findAll(pageable)).thenReturn(paginaDeProdutos);

        ResponseEntity<Map<String, Object>> response = produtoController.listarProdutos(pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));

        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);

        @SuppressWarnings("unchecked")
        List<ProdutoDTO> produtosRetornados = (List<ProdutoDTO>) responseBody.get("data");
        assertNotNull(produtosRetornados);
        assertEquals(2, produtosRetornados.size());
        assertEquals("Produto1", produtosRetornados.get(0).getNome());
        assertEquals("Produto2", produtosRetornados.get(1).getNome());

        @SuppressWarnings("unchecked")
        Map<String, Object> pageInfo = (Map<String, Object>) responseBody.get("page");
        assertNotNull(pageInfo);
        assertEquals(0, pageInfo.get("currentPage"));
        assertEquals(2L, pageInfo.get("totalItems"));
        assertEquals(1, pageInfo.get("totalPages"));
        assertEquals(10, pageInfo.get("pageSize"));
    }

    @Test
    void deveAdicionarProduto() {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("NovoProduto");
        produtoDTO.setPreco(30.0);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("NovoProduto");
        produtoSalvo.setPreco(30.0);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ResponseEntity<Map<String, Object>> response = produtoController.adicionarProduto(produtoDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        assertEquals(1L, data.get("id"));
        assertEquals("NovoProduto", data.get("nome"));
        assertEquals("Produto adicionado com sucesso!", response.getBody().get("message"));

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveAtualizarProduto() {
        Long produtoId = 1L;

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("ProdutoExistente");
        produto.setPreco(15.0);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoAtualizado");
        produtoDTO.setPreco(20.0);

        Produto produtoSalvo = new Produto(produtoDTO.getNome(), produtoDTO.getPreco());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ResponseEntity<Map<String, Object>> response = produtoController.atualizarProduto(produtoId, produtoDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        assertEquals(produtoId, data.get("id"));
        assertEquals("ProdutoAtualizado", data.get("nome"));
        assertEquals(20.0, data.get("preco"));
        assertEquals("Produto atualizado com sucesso!", response.getBody().get("message"));

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveDarErroAoAtualizarProduto() {
        Long produtoId = 1L;

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoAtualizado");
        produtoDTO.setPreco(20.0);

        ResponseEntity<Map<String, Object>> response = produtoController.atualizarProduto(produtoId, produtoDTO);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Produto não encontrado com o ID: " + produtoId, response.getBody().get("message"));
    }

    @Test
    void deveRemoverProduto() {
        Long produtoId = 1L;

        when(produtoRepository.existsById(produtoId)).thenReturn(true);

        ResponseEntity<Map<String, Object>> response = produtoController.removerProduto(produtoId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Produto com o ID " + produtoId + " removido com sucesso!", response.getBody().get("message"));
        verify(produtoRepository, times(1)).deleteById(produtoId);
    }

    @Test
    void deveDarErroAoRemoverProduto() {
        Long produtoId = 1L;

        when(produtoRepository.existsById(produtoId)).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = produtoController.removerProduto(produtoId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Produto não encontrado com o ID: " + produtoId, response.getBody().get("message"));
    }

}
