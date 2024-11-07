package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.ProdutoDTO;
import com.exemplo.apibasica.model.Produto;
import com.exemplo.apibasica.repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para operações de produtos.
 * Responsável por gerenciar as requisições relacionadas a produtos.
 */
@Slf4j
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<ProdutoDTO> listarProdutos() {
        log.info("Listando todos os produtos");
        return produtoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostMapping
    public String adicionarProduto(@RequestBody @Valid ProdutoDTO produtoDTO) {
        log.info("Adicionando produto: {}", produtoDTO.getNome());
        Produto produto = convertToEntity(produtoDTO);
        produtoRepository.save(produto);
        log.info("Produto '{}' adicionado com sucesso", produto.getNome());
        return "Produto '" + produto.getNome() + "' adicionado com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDTO) {
        log.info("Atualizando produto com ID: {}", id);
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isEmpty()) {
            log.warn("Produto com ID {} não encontrado", id);
            return "Produto não encontrado!";
        }

        Produto produto = produtoExistente.get();
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produtoRepository.save(produto);

        log.info("Produto atualizado para: {}", produto.getNome());
        return "Produto atualizado para: " + produto.getNome();
    }

    @DeleteMapping("/{id}")
    public String removerProduto(@PathVariable Long id) {
        log.info("Removendo produto com ID: {}", id);
        if (!produtoRepository.existsById(id)) {
            log.warn("Produto com ID {} não encontrado", id);
            return "Produto não encontrado!";
        }

        produtoRepository.deleteById(id);
        log.info("Produto com ID {} removido com sucesso", id);
        return "Produto removido com sucesso!";
    }

    // Métodos auxiliares para conversão
    private ProdutoDTO convertToDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        return dto;
    }

    private Produto convertToEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        return produto;
    }
}
