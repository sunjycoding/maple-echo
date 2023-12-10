DROP TABLE IF EXISTS tb_question_answer;
CREATE TABLE tb_question_answer
(
    id                 VARCHAR(32) PRIMARY KEY,
    question           VARCHAR(255) UNIQUE   NOT NULL,
    answer_type        ENUM ('TEXT', 'FILE') NOT NULL,
    answer_text        VARCHAR(255),
    answer_file_url    VARCHAR(255),
    created_date       DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('8a50287e85e34499927015470061c671', 'BOSS', 'FILE', '', 'answers/boss.png', '2023-12-10 06:52:53', '2023-12-09 22:53:35');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('91f704d50dce42d4bccffa271a4875ec', '装备', 'FILE', '', 'answers/equipment.png', '2023-12-10 06:52:18', '2023-12-09 22:53:35');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('99b90858ba0943ef85e8dabfa87a779b', '期望', 'TEXT', 'https://brendonmay.github.io/', null, '2023-12-10 06:51:41', '2023-12-10 06:51:41');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('a4c94218f1d744b18051d84a78c7eaaa', 'BUFF', 'FILE', '', 'answers/buff.png', '2023-12-10 06:53:07', '2023-12-09 22:53:35');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('b303732327874f29ad17ab605635fff9', '核心', 'FILE', '', 'answers/matrix.png', '2023-12-10 06:52:32', '2023-12-09 22:53:35');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('cccae9733a8c48bc9bd21907599cffbb', '火花', 'FILE', '', 'answers/flame.png', '2023-12-10 06:52:10', '2023-12-09 22:53:35');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('ed3d7a559c68440c8219a664b8b288c2', 'LINK', 'FILE', '', 'answers/link.png', '2023-12-10 06:52:43', '2023-12-09 22:53:35');
INSERT INTO db_maple_aide.tb_question_answer (id, question, answer_type, answer_text, answer_file_url, created_date, last_modified_date) VALUES ('ee78ba1368e14bd6afc37891dc9f1714', '官网', 'TEXT', 'https://maplestory.nexon.net/', null, '2023-12-10 06:49:18', '2023-12-10 06:49:18');

DROP TABLE IF EXISTS tb_qq_character;
CREATE TABLE tb_qq_character
(
    id                 VARCHAR(32) PRIMARY KEY,
    qq_number          VARCHAR(255) UNIQUE NOT NULL,
    character_name     VARCHAR(255) UNIQUE NOT NULL,
    created_date       DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);