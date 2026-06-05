-- daily database setup

DROP DATABASE dailymanagement_db;

CREATE DATABASE dailymanagement_db;
CREATE USER general_user WITH PASSWORD 'pass123';

\c dailymanagement_db

-- 権限設定
GRANT CONNECT ON DATABASE dailymanagement_db TO general_user;--3. スキーマレベルの権限
GRANT USAGE, CREATE ON SCHEMA public TO general_user;--4. 既存オブジェクトへの一括権限
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO general_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO general_user;--5. 今後作成されるオブジェクトへの自動権限設定
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO general_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO general_user;

CREATE TABLE users (
    user_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    mail_address VARCHAR(255) UNIQUE NOT NULL,
    remind_status BOOLEAN NOT NULL DEFAULT TRUE,
    remind_time TIME NOT NULL DEFAULT '09:30'
);

CREATE TABLE reminds (
    remind_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    remind_content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


CREATE TABLE dailies (
    daily_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    daily_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE categories (
    category_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);

CREATE TABLE daily_details (
    daily_detail_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    daily_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    content TEXT NOT NULL,

    FOREIGN KEY (daily_id) REFERENCES dailies(daily_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE daily_summaries (
    daily_summary_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    daily_id INTEGER NOT NULL UNIQUE,
    daily_summary_content TEXT NOT NULL,

    FOREIGN KEY (daily_id) REFERENCES dailies(daily_id)
);

CREATE TABLE weekly_summaries (
    weekly_summary_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    weekly_summary_content TEXT NOT NULL,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

