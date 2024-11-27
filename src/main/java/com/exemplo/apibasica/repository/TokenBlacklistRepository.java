package com.exemplo.apibasica.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class TokenBlacklistRepository {

    private static final String ADD_TOKEN_SQL = "INSERT INTO TOKEN_BLACKLIST (TOKEN, EXPIRATION_DATE) VALUES (:token, :expirationDate)";
    private static final String CHECK_TOKEN_SQL = "SELECT COUNT(1) FROM TOKEN_BLACKLIST WHERE TOKEN = :token";
    private static final String DELETE_EXPIRED_TOKENS_SQL = "DELETE FROM TOKEN_BLACKLIST WHERE EXPIRATION_DATE < SYSDATE";

    private NamedParameterJdbcTemplate jdbcTemplate;

    public TokenBlacklistRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addTokenToBlacklist(String token, Date expirationDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("expirationDate", expirationDate);

        jdbcTemplate.update(ADD_TOKEN_SQL, params);

        log.info("Token adicionado à blacklist: " + token);
    }

    public boolean isTokenBlacklisted(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);

        Integer count = jdbcTemplate.queryForObject(CHECK_TOKEN_SQL, params, Integer.class);
        return Optional.ofNullable(count).orElse(0) > 0;
    }

    public void deleteExpiredTokens() {
        jdbcTemplate.update(DELETE_EXPIRED_TOKENS_SQL, new HashMap<>());

        log.info("Tokens expirados removidos da blacklist");
    }
}
