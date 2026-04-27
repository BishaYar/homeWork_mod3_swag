-- liquibase formatted sql

-- changeset yar:1
CREATE INDEX faculty_name_color_index ON faculty (name, color)
