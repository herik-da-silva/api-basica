package com.exemplo.apibasica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProdutoDTO {

    private Long id;

    @NotBlank(message = "O nome do produto não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private double preco;

    @NotNull(message = "O fabricante é obrigatório.")
    @Positive(message = "O fabricante deve ser um valor positivo.")
    private Long fabricante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Long getFabricante() {
        return fabricante;
    }

    public void setFabricante(Long fabricante) {
        this.fabricante = fabricante;
    }
}
