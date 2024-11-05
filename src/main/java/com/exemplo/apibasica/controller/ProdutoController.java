package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.ProdutoDTO;
import com.exemplo.apibasica.model.Produto;
import com.exemplo.apibasica.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final List<String> produtos = new ArrayList<>();

    @Autowired
    private ProdutoRepository produtoRepository;

    // GET: Listar todos os produtos
    @GetMapping
    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // POST: Adicionar um novo produto
    @PostMapping
    public String adicionarProduto(@RequestBody @Valid ProdutoDTO produtoDTO) {
        Produto produto = convertToEntity(produtoDTO);
        produtoRepository.save(produto);
        return "Produto '" + produto.getNome() + "' adicionado com sucesso!";
    }

    // PUT: Atualizar um produto pelo índice
    @PutMapping("/{id}")
    public String atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDTO) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isEmpty()) {
            return "Produto não encontrado!";
        }

        Produto produto = produtoExistente.get();
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produtoRepository.save(produto);

        return "Produto atualizado para: " + produto.getNome();
    }

    // DELETE: Remover um produto pelo índice
    @DeleteMapping("/{id}")
    public String removerProduto(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return "Produto não encontrado!";
        }

        produtoRepository.deleteById(id);
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
