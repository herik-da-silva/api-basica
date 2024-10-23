package com.exemplo.apibasica.controller;

import org.springframework.security.core.context.SecurityContextHolder;
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

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final List<String> produtos = new ArrayList<>();

    // GET: Listar todos os produtos
    @GetMapping
    public List<String> listarProdutos() {
        System.out.println("Usuário autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());

        return produtos;
    }

    // POST: Adicionar um novo produto
    @PostMapping
    public String adicionarProduto(@RequestBody String nome) {
        System.out.println("Usuário autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());

        produtos.add(nome);
        return "Produto '" + nome + "' adicionado com sucesso!";
    }

    // PUT: Atualizar um produto pelo índice
    @PutMapping("/{index}")
    public String atualizarProduto(@PathVariable int index, @RequestBody String nome) {
        System.out.println("Usuário autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());

        if (index >= produtos.size()) {
            return "Produto não encontrado!";
        }
        produtos.set(index, nome);
        return "Produto atualizado para: " + nome;
    }

    // DELETE: Remover um produto pelo índice
    @DeleteMapping("/{index}")
    public String removerProduto(@PathVariable int index) {
        System.out.println("Usuário autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());

        if (index >= produtos.size()) {
            return "Produto não encontrado!";
        }
        String nome = produtos.remove(index);
        return "Produto '" + nome + "' removido com sucesso!";
    }
}
