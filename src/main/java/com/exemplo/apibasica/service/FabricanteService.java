package com.exemplo.apibasica.service;

import com.exemplo.apibasica.model.Fabricante;
import com.exemplo.apibasica.repository.FabricanteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cache de fabricantes para evitar acessos desnecessários ao banco de dados.
 */
@Slf4j
@Service
public class FabricanteService {

    private final FabricanteRepository fabricanteRepository;

    public FabricanteService(FabricanteRepository fabricanteRepository) {
        this.fabricanteRepository = fabricanteRepository;
    }

    /**
     * O resultado do método será armazenado em cache, utilizando o identificador "fabricantes"
     *
     * @return
     */
    @Cacheable("fabricantes")
    public List<Fabricante> getAllFabricantes() {
        log.info("Buscando fabricantes do banco de dados.");
        return fabricanteRepository.findAll();
    }

    /**
     * Remove todas as entradas do cache "fabricantes"
     */
    @CacheEvict(value = "fabricantes", allEntries = true) // O parâmetro allEntries = true garante que todas as entradas desse cache sejam removidas.
    public void evictCache() {
        log.info("Cache de fabricantes limpo!");
    }
}
