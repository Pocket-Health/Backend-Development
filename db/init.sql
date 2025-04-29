CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       notifications_enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE medical_cards (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id UUID UNIQUE NOT NULL,
                               full_name VARCHAR(255) NOT NULL,
                               height INT,
                               weight DECIMAL(5, 2),
                               blood_type VARCHAR(10) NOT NULL,
                               allergies TEXT,
                               diseases TEXT,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE medication_schedule (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     user_id UUID NOT NULL,
                                     medication_name VARCHAR(255) NOT NULL,
                                     scheduled_days INTEGER[] NOT NULL,
                                     scheduled_times TIME[] NOT NULL,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE chat_history (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              user_id UUID NOT NULL,
                              messages JSONB NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE knowledge_base (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                disease_name VARCHAR(255) NOT NULL,
                                description TEXT NOT NULL,
                                types TEXT NOT NULL,
                                symptoms TEXT NOT NULL,
                                causes TEXT NOT NULL
);