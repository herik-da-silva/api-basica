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

import java.util.List;
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

        String resultado = produtoController.adicionarProduto(produtoDTO);

        Assertions.assertTrue(resultado.contains("Produto 'NovoProduto' adicionado com sucesso"));
        Mockito.verify(produtoRepository, Mockito.times(1)).save(Mockito.any(Produto.class));
    }

    @Test
    void deveAtualizarProduto() {
        Long produtoId = 1L;
        Produto produto = new Produto("ProdutoExistente", 15.0);
        produto.setId(produtoId);

        Mockito.when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("ProdutoAtualizado");
        produtoDTO.setPreco(20.0);

        String resultado = produtoController.atualizarProduto(produtoId, produtoDTO);

        Assertions.assertTrue(resultado.contains("Produto atualizado para: ProdutoAtualizado"));
        Mockito.verify(produtoRepository).save(produto);
    }

    @Test
    void deveRemoverProduto() {
        Long produtoId = 1L;

        Mockito.when(produtoRepository.existsById(produtoId)).thenReturn(true);

        String resultado = produtoController.removerProduto(produtoId);

        Assertions.assertTrue(resultado.contains("Produto removido com sucesso"));
        Mockito.verify(produtoRepository, Mockito.times(1)).deleteById(produtoId);
    }

}
