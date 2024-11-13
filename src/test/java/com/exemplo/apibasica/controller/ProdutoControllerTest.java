package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.ProdutoDTO;
import com.exemplo.apibasica.model.Produto;
import com.exemplo.apibasica.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void deveListarTodosProdutos() {
        List<Produto> produtos = List.of(new Produto("Produto1", 10.0), new Produto("Produto2", 20.0));
        Mockito.when(produtoRepository.findAll()).thenReturn(produtos);

        List<ProdutoDTO> resultado = produtoController.listarProdutos();

        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Produto1", resultado.get(0).getNome());
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
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produtoSalvo);

        ResponseEntity<Map<String, Object>> response = produtoController.adicionarProduto(produtoDTO);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("success", response.getBody().get("status"));

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        Assertions.assertEquals(1L, data.get("id"));
        Assertions.assertEquals("NovoProduto", data.get("nome"));
        Assertions.assertEquals("Produto adicionado com sucesso!", response.getBody().get("message"));

        Mockito.verify(produtoRepository, Mockito.times(1)).save(Mockito.any(Produto.class));
    }

    @Test
    void deveAtualizarProduto() {
        Long produtoId = 1L;

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("ProdutoExistente");
        produto.setPreco(15.0);

        Mockito.when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoAtualizado");
        produtoDTO.setPreco(20.0);

        Produto produtoSalvo = new Produto(produtoDTO.getNome(), produtoDTO.getPreco());
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produtoSalvo);

        ResponseEntity<Map<String, Object>> response = produtoController.atualizarProduto(produtoId, produtoDTO);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("success", response.getBody().get("status"));

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        Assertions.assertEquals(produtoId, data.get("id"));
        Assertions.assertEquals("ProdutoAtualizado", data.get("nome"));
        Assertions.assertEquals(20.0, data.get("preco"));
        Assertions.assertEquals("Produto atualizado com sucesso!", response.getBody().get("message"));

        Mockito.verify(produtoRepository, Mockito.times(1)).save(Mockito.any(Produto.class));
    }

    @Test
    void deveDarErroAoAtualizarProduto() {
        Long produtoId = 1L;

        Mockito.when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoAtualizado");
        produtoDTO.setPreco(20.0);

        ResponseEntity<Map<String, Object>> response = produtoController.atualizarProduto(produtoId, produtoDTO);

        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertEquals("error", response.getBody().get("status"));
        Assertions.assertEquals("Produto não encontrado com o ID: " + produtoId, response.getBody().get("message"));
    }

    @Test
    void deveRemoverProduto() {
        Long produtoId = 1L;

        Mockito.when(produtoRepository.existsById(produtoId)).thenReturn(true);

        ResponseEntity<Map<String, Object>> response = produtoController.removerProduto(produtoId);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("success", response.getBody().get("status"));
        Assertions.assertEquals("Produto com o ID " + produtoId + " removido com sucesso!", response.getBody().get("message"));
        Mockito.verify(produtoRepository, Mockito.times(1)).deleteById(produtoId);
    }

    @Test
    void deveDarErroAoRemoverProduto() {
        Long produtoId = 1L;

        Mockito.when(produtoRepository.existsById(produtoId)).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = produtoController.removerProduto(produtoId);

        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertEquals("error", response.getBody().get("status"));
        Assertions.assertEquals("Produto não encontrado com o ID: " + produtoId, response.getBody().get("message"));
    }

}
