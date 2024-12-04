package com.exemplo.apibasica.jobs;

import com.exemplo.apibasica.service.FabricanteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por atualizar periodicamente o cache relacionado aos fabricantes.
 */
@Slf4j
@Component
public class FabricanteCacheUpdater {

    private FabricanteService fabricanteService;

    public FabricanteCacheUpdater(FabricanteService fabricanteService) {
        this.fabricanteService = fabricanteService;
    }

    /**
     * Scheduler responsável por atualizar o cache de fabricantes a cada 24h
     */
    @Scheduled(fixedRateString = "${fabricante.cache.refresh.interval}") // 24 horas em milissegundos
    public void refreshFabricantesCache() {
        fabricanteService.evictCache(); // Limpa o cache
        fabricanteService.getAllFabricantes(); // Recarrega o cache
        log.info("Cache de fabricantes atualizado!");
    }
}
