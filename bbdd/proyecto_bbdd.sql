CREATE DATABASE db_proyecto;

USE db_proyecto;


CREATE TABLE profesor (
    id_profesor INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    numero INTEGER(5),
    nombre VARCHAR(150) NOT NULL,
    abreviacion VARCHAR(5) NOT NULL,
    minhorasdia INTEGER(3),
    maxhorasdia INTEGER(3),
    departamento VARCHAR(50) NOT NULL
);


CREATE TABLE credencial (
    id_credencial INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(255) NOT NULL,
	passwd_hash VARCHAR(255) NOT NULL,
    profesor INTEGER(11) NOT NULL,
    rol VARCHAR(50),
    CONSTRAINT credencial_prof_fk FOREIGN KEY (profesor) REFERENCES profesor(id_profesor)
);


CREATE TABLE aula (
    id_aula INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    planta INTEGER(3)
);


CREATE TABLE curso (
    id_curso INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    nivel INTEGER(3) NOT NULL,
    nombre VARCHAR(150) NOT NULL
);


CREATE TABLE asignatura (
    id_asignatura INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    numero INTEGER(5) NOT NULL,
    abreviacion VARCHAR(150) NOT NULL,
    nombre VARCHAR(150) NOT NULL
);


CREATE TABLE curso_asignatura (
    curso INTEGER(11),
    asignatura INTEGER(11),
    CONSTRAINT c_s_pk PRIMARY KEY (curso, asignatura),
    CONSTRAINT c_fk FOREIGN KEY (curso) REFERENCES curso(id_curso),
    CONSTRAINT a_fk FOREIGN KEY (asignatura) REFERENCES asignatura(id_asignatura)
);


CREATE TABLE numero_dia (
	id_dia INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    dia VARCHAR(150) NOT NULL
);


CREATE TABLE numero_hora (
	id_hora INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    horas VARCHAR(150) NOT NULL
);


CREATE TABLE franja_horaria (
	id_franja INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    dia INTEGER(11) NOT NULL,
    hora INTEGER(11) NOT NULL,
    CONSTRAINT franja_dia FOREIGN KEY (dia) REFERENCES numero_dia(id_dia),
    CONSTRAINT franja_hora FOREIGN KEY (hora) REFERENCES numero_hora(id_hora)
);



