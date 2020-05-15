CREATE DATABASE llm;

-- CREATE ROLE llm;

create user llm with encrypted password 'llm';
grant all privileges on database llm to llm