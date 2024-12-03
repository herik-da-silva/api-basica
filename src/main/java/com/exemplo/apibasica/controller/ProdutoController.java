package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.ProdutoDTO;
import com.exemplo.apibasica.exception.ResourceNotFoundException;
import com.exemplo.apibasica.model.Fabricante;
import com.exemplo.apibasica.model.Produto;
import com.exemplo.apibasica.repository.FabricanteRepository;
import com.exemplo.apibasica.repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para operações de produtos.
 * Responsável por gerenciar as requisições relacionadas a produtos.
 */
@Slf4j
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FabricanteRepository fabricanteRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarProdutos(Pageable pageable) {
        log.info("Listando produtos com paginação - Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Produto> page = produtoRepository.findAll(pageable);
        List<ProdutoDTO> produtos = page.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());

        Map<String, Object> successResponse = Map.of(
                "status", "success",
                "data", produtos,
                "page", Map.of(
                        "currentPage", page.getNumber(),
                        "totalItems", page.getTotalElements(),
                        "totalPages", page.getTotalPages(),
                        "pageSize", page.getSize()
                )
        );

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> adicionarProduto(@RequestBody @Valid ProdutoDTO produtoDTO) {
        log.info("Adicionando produto: {}", produtoDTO.getNome());

        Produto produto = convertToEntity(produtoDTO);
        Produto produtoSalvo = produtoRepository.save(produto);
        log.info("Produto '{}' adicionado com sucesso", produtoSalvo.getNome());

        Map<String, Object> successResponse = Map.of(
                "status", "success",
                "data", Map.of(
                        "id", produtoSalvo.getId(),
                        "nome", produtoSalvo.getNome(),
                        "preco", produtoSalvo.getPreco(),
                        "fabricante", produtoSalvo.getFabricante().getId()
                ),
                "message", "Produto adicionado com sucesso!"
        );

        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDTO) {
        log.info("Atualizando produto com ID: {}", id);

        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isEmpty()) {
            log.warn("Produto com ID {} não encontrado", id);

            Map<String, Object> errorResponse = Map.of(
                    "status", "error",
                    "message", "Produto não encontrado com o ID: " + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Produto produto = produtoExistente.get();
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setFabricante(getFabricante(produtoDTO.getFabricante()));
        produtoRepository.save(produto);

        log.info("Produto atualizado para: {}", produto.getNome());

        Map<String, Object> successResponse = Map.of(
                "status", "success",
                "data", Map.of(
                        "id", produto.getId(),
                        "nome", produto.getNome(),
                        "preco", produto.getPreco(),
                        "fabricante", produto.getFabricante().getId()
                ),
                "message", "Produto atualizado com sucesso!"
        );
        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> removerProduto(@PathVariable Long id) {
        log.info("Removendo produto com ID: {}", id);

        if (!produtoRepository.existsById(id)) {
            log.warn("Produto com ID {} não encontrado", id);

            Map<String, Object> errorResponse = Map.of(
                    "status", "error",
                    "message", "Produto não encontrado com o ID: " + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        produtoRepository.deleteById(id);
        log.info("Produto com ID {} removido com sucesso", id);

        Map<String, Object> successResponse = Map.of(
                "status", "success",
                "message", "Produto com o ID " + id + " removido com sucesso!"
        );
        return ResponseEntity.ok(successResponse);
    }

    // Métodos auxiliares para conversão
    private ProdutoDTO convertToDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        dto.setFabricante(produto.getFabricante().getId());
        return dto;
    }

    private Produto convertToEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());

        if (dto.getFabricante() != null) {
            produto.setFabricante(getFabricante(dto.getFabricante()));
        }

        return produto;
    }

    private Fabricante getFabricante(Long idFabricante) {
        return fabricanteRepository.findById(idFabricante)
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante com ID " + idFabricante + " não encontrado."));
    }
}
