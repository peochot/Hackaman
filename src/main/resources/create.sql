CREATE SCHEMA IF NOT EXISTS hakaman;

CREATE TABLE user (
    id LONG NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    score LONG DEFAULT 0,
    stageId LONG NOT NULL DEFAULT 0,
    UNIQUE (username)
);

CREATE TABLE question (
  id LONG NOT NULL AUTO_INCREMENT PRIMARY KEY,
  content VARCHAR NOT NULL,
  UNIQUE (content)
);

CREATE TABLE answer (
  id LONG NOT NULL AUTO_INCREMENT PRIMARY KEY,
  questionId LONG NOT NULL,
  charkey VARCHAR NOT NULL,
  content VARCHAR NOT NULL,
  correct BOOLEAN DEFAULT false,
  FOREIGN KEY (questionId) REFERENCES question(id),
  UNIQUE (charkey, content)
);

alter table user add constraint sk1 foreign key (stageId) references question(id);
