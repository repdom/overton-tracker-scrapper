-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE media_outlets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    domain VARCHAR(255) UNIQUE NOT NULL,
    source_type VARCHAR(50) CHECK (source_type IN ('news', 'think_tank', 'blog')),
    base_ideology_score DECIMAL(3,2),
    country_code CHAR(2) DEFAULT 'US'
);

CREATE TABLE stories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    first_seen_at TIMESTAMP NOT NULL
);

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    media_id INTEGER REFERENCES media_outlets(id),
    story_id INTEGER REFERENCES stories(id),
    original_url TEXT UNIQUE NOT NULL,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    published_at TIMESTAMP NOT NULL,
    embedding vector(768),
    main_topic VARCHAR(100),
    keywords JSONB
);

CREATE TABLE article_classifications (
    article_id BIGINT REFERENCES articles(id),
    dimension VARCHAR(100),
    confidence_score DECIMAL(5,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE named_entities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    normalized_name VARCHAR(255),
    entity_type VARCHAR(50)
);

CREATE TABLE article_entities (
    article_id BIGINT REFERENCES articles(id),
    entity_id BIGINT REFERENCES named_entities(id),
    PRIMARY KEY (article_id, entity_id)
);

-- Index for vector similarity search (cosine distance)
CREATE INDEX ON articles USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
