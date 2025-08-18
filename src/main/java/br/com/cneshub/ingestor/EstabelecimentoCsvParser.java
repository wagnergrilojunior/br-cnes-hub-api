package br.com.cneshub.ingestor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses Estabelecimentos CSV and stores it in the staging table.
 */
@Component
public class EstabelecimentoCsvParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstabelecimentoCsvParser.class);
    private static final int BATCH_SIZE = 1000;

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public EstabelecimentoCsvParser(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public ParseResult parse(InputStream inputStream, boolean storeRawJson) throws IOException {
        long start = System.currentTimeMillis();
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(reader)) {

            List<CSVRecord> batch = new ArrayList<>(BATCH_SIZE);
            int inserted = 0;
            for (CSVRecord record : parser) {
                batch.add(record);
                if (batch.size() == BATCH_SIZE) {
                    inserted += flush(batch, storeRawJson);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                inserted += flush(batch, storeRawJson);
            }
            long lines = parser.getRecordNumber();
            int rejected = (int) (lines - inserted);
            Duration duration = Duration.ofMillis(System.currentTimeMillis() - start);
            return new ParseResult(lines, inserted, rejected, duration);
        }
    }

    private int flush(List<CSVRecord> batch, boolean storeRawJson) {
        String sql = "INSERT INTO staging_estabelecimentos (cnes, nome, uf, municipio, cod_municipio, tipo_estab, logradouro, numero, bairro, cep, telefone, email, competencia, raw_json) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int[] results = jdbcTemplate.batchUpdate(sql, batch, BATCH_SIZE, (ps, record) -> {
            ps.setString(1, field(record, "cnes"));
            ps.setString(2, field(record, "nome"));
            ps.setString(3, field(record, "uf"));
            ps.setString(4, field(record, "municipio"));
            ps.setString(5, field(record, "cod_municipio"));
            ps.setString(6, field(record, "tipo_estab"));
            ps.setString(7, field(record, "logradouro"));
            ps.setString(8, field(record, "numero"));
            ps.setString(9, field(record, "bairro"));
            ps.setString(10, field(record, "cep"));
            ps.setString(11, field(record, "telefone"));
            ps.setString(12, field(record, "email"));
            ps.setString(13, field(record, "competencia"));
            if (storeRawJson) {
                try {
                    ps.setString(14, objectMapper.writeValueAsString(record.toMap()));
                } catch (JsonProcessingException e) {
                    throw new SQLException("Erro ao serializar linha para JSON", e);
                }
            } else {
                ps.setNull(14, Types.VARCHAR);
            }
        });
        return Arrays.stream(results).sum();
    }

    private String field(CSVRecord record, String name) {
        if (record.isMapped(name)) {
            return record.get(name);
        }
        String upper = name.toUpperCase();
        if (record.isMapped(upper)) {
            return record.get(upper);
        }
        String lower = name.toLowerCase();
        if (record.isMapped(lower)) {
            return record.get(lower);
        }
        return null;
    }

    public record ParseResult(long linesRead, int inserted, int rejected, Duration duration) {}
}

