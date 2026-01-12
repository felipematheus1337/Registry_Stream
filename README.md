# 🧩 Registry_Stream

> 🔁 **Event-Driven Microservice** usando Spring Boot + Spring Cloud Stream (Kafka), PostgreSQL, CSV generation e AWS S3 integration.  
> Arquitetura orientada a eventos com Publisher, Consumer, DLQ, persistência de status e exportação de dados.

---

## 🚀 Visão Geral

O **Registry_Stream** é um microserviço demonstrativo que ilustra um pipeline de eventos completo:  
📌 Recepção de comandos via REST → 📤 Publicação de eventos no Kafka → 📥 Consumo por listener → 📊 Persistência de status → 📦 Exportação de dados CSV → ☁️ Armazenamento em AWS S3

Esse projeto foi construído com foco em:
- 📘 **Spring Cloud Stream** para abstração de mensageria
- 🐘 **PostgreSQL** como base transacional
- 📄 **CSV generation** via Jackson
- ☁️ **AWS S3** para armazenar artefatos gerados
- 📈 Visibilidade e confiabilidade com estado de evento

---

## 🧱 Tecnologias

---

### 📦 Core
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-green)  
![Spring Cloud Stream](https://img.shields.io/badge/Spring%20Cloud%20Stream-2025.1.0-blue)  
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-latest-orange)

### 🗄️ Persistence
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-blueviolet)

### ☁️ Cloud
![AWS S3](https://img.shields.io/badge/AWS-S3-yellow)

### 🛠️ Format & Utility
![Jackson Csv](https://img.shields.io/badge/Jackson-CSV-e89b00)  
![Maven](https://img.shields.io/badge/Maven-Build-Project-blue)

---

## 📌 Diagrama de Arquitetura

┌──────────────┐ ┌────────────────┐
│ REST API │ │ Spring Cloud │
│ POST /users │ │ Stream/Kafka │
└──────┬───────┘ └───────┬────────┘
│ │
│ Create + Publish Event ▼
│ Kafka Topic
▼ │
┌──────────────┐ │
│ PostgreSQL │ │
│ registry │ │
└──────┬───────┘ │
│ ▼
│ Consumer Listener
▼ (Kafka → Business Logic)
┌──────────────┐ │
│ Status/CSV │ ✨ Generates │
│ Persisted │ ⟶ CSV Bytes │
└──────┬───────┘ ▼
│ AWS S3 Upload
▼
┌─────────────┐
│ Reporting / │
│ Monitoring │
└─────────────┘


---

## 🪄 Principais Funcionalidades

✔️ Register user via REST API  
✔️ Publish `UserCreated` event to Kafka  
✔️ Consumer with status tracking (`PENDING`, `PUBLISHED`, `SUCCESS`, `FAILED`)  
✔️ Retry & DLQ via Spring Cloud Stream Kafka Binder  
✔️ CSV export of processed messages  
✔️ Upload CSV to AWS S3

---

## 📁 Endpoints

### POST `/users`

📥 Registra um usuário e publica o evento `UserCreated` no Kafka.

**Request**
```json
{
   "name": "Felipe",
   "email": "felipe@example.com"
}
````


**Response**
```json
{
   "id": 1,
   "name": "Felipe",
   "email": "felipe@example.com",
   "status": "PENDING"
}
````

📦 Kafka Integration

Spring Cloud Stream abstrai conectividade com Kafka usando bindings e funções (Supplier, Consumer). Ele gerencia:

Publishing events

Concurrent consumers

Retry / DLQ

Ele mapeia canais para tópicos e lida com serialização/deserialização de payloads.
📤 CSV Generation

Utiliza Jackson CSV para converter listas de entidades em CSV.

byte[] csv = csvConverter.toCsvBytes(users, User.class);

☁️ AWS S3 Integration

O armazenamento de artefatos CSV é realizado com SDK AWS v2 (S3Client).
As credenciais AWS são obtidas por padrão via ambiente (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_REGION).

Dotenv example:

AWS_ACCESS_KEY_ID=AKIA...
AWS_SECRET_ACCESS_KEY=XXXX
AWS_REGION=sa-east-1
APP_S3_BUCKET=my-bucket


📦 Environment Variables
Variable	Description
AWS_ACCESS_KEY_ID	AWS access key
AWS_SECRET_ACCESS_KEY	AWS secret key
AWS_REGION	AWS region (ex: sa-east-1)
APP_S3_BUCKET	S3 bucket name
