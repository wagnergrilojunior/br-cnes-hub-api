package br.com.cneshub.ingestor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestInputStream;

/**
 * Service responsible for ingesting Estabelecimentos CSV files.
 */
@Service
public class EstabelecimentoIngestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstabelecimentoIngestService.class);

    private final EstabelecimentoCsvParser parser;
    private final JdbcTemplate jdbcTemplate;
    private final boolean storeRawJson;

    public EstabelecimentoIngestService(EstabelecimentoCsvParser parser,
                                        JdbcTemplate jdbcTemplate,
                                        @Value("${cneshub.ingest.store-raw-json:false}") boolean storeRawJson) {
        this.parser = parser;
        this.jdbcTemplate = jdbcTemplate;
        this.storeRawJson = storeRawJson;
    }

    /**
     * Ingests data from the provided CSV file path.
     *
     * @param arquivo path to CSV file
     */
    public void ingestFromCsv(Path arquivo) throws IOException {
        String hash = hash(arquivo);
        Integer exists = jdbcTemplate.queryForObject("SELECT count(1) FROM ingest_file WHERE file_hash = ?", Integer.class, hash);
        if (exists != null && exists > 0) {
            LOGGER.info("Arquivo {} já processado", arquivo);
            return;
        }
        long start = System.currentTimeMillis();
        try (InputStream in = Files.newInputStream(arquivo)) {
            EstabelecimentoCsvParser.ParseResult result = parser.parse(in, storeRawJson);
            jdbcTemplate.update("INSERT INTO ingest_file(file_hash) VALUES (?)", hash);
            long total = System.currentTimeMillis() - start;
            LOGGER.info("Ingestão concluída em {} ms - linhas lidas: {}, inseridas: {}, rejeitadas: {}",
                    total, result.linesRead(), result.inserted(), result.rejected());
        }
    }

    private String hash(Path arquivo) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not disponível", e);
        }
        try (InputStream in = Files.newInputStream(arquivo);
             DigestInputStream dis = new DigestInputStream(in, digest)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // ler arquivo
            }
        }
        byte[] hash = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

