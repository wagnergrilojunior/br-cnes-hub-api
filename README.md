# CNES Hub API
Gateway de dados abertos do **CNES** (Cadastro Nacional de Estabelecimentos de Saúde) para APIs modernas.  
Baixa, normaliza e expõe via **REST** informações de estabelecimentos, serviços, leitos, equipamentos e profissionais — pronto para integração com ERPs, prontuários e BI.

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-21-blue"/>
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen"/>
  <img alt="Build" src="https://img.shields.io/badge/Build-Maven-orange"/>
  <img alt="License" src="https://img.shields.io/badge/license-MIT-lightgrey"/>
</p>

## 🌟 Por que o CNES Hub API?
Os datasets do CNES são públicos, porém grandes e com formatos variados (CSV/DBF). O **CNES Hub API** automatiza:
1) **Ingestão** de dados do OpenDataSUS/CKAN  
2) **Normalização** e indexação em banco relacional  
3) **Exposição** por **APIs REST** limpas, com paginação, filtros e caching

Tudo com **Java 21 + Spring Boot** e foco em **desempenho e previsibilidade**.

---

## 🚀 Funcionalidades
- **Ingestão automatizada** (agendada) dos datasets do CNES
- **Normalização** (ETL leve) e índices para consulta rápida
- **APIs REST** com filtros e paginação
- **Cache** para consultas frequentes
- **Autenticação por API Key** (opcional)
- **Observabilidade** com Actuator (health, metrics)
- **OpenAPI/Swagger UI** para exploração dos endpoints

---

## 🏗️ Arquitetura (visão)

## 📦 Estrutura de Pacotes
- `br.com.cneshub.api` – controllers REST
- `br.com.cneshub.core` – entidades, serviços e repositórios
- `br.com.cneshub.ingestor` – importação de dados CNES
- `br.com.cneshub.normalizer` – processamento/ETL
- `br.com.cneshub.scheduler` – tarefas agendadas

## 🛠️ Desenvolvimento
Requisitos: Java 21 e Maven.

```bash
mvn spring-boot:run
```

## 📄 Licença
Distribuído sob a [MIT License](LICENSE).