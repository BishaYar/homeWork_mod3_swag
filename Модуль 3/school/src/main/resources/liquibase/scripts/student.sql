-- liquibase formatted sql

-- changeset yar:1
CREATE INDEX student_name_index ON student (name)
