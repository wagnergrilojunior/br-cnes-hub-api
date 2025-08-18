CREATE TABLE IF NOT EXISTS ingest_file (
    file_hash VARCHAR(64) PRIMARY KEY,
    processed_at TIMESTAMP DEFAULT now()
);
