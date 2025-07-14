CREATE DATABASE hospital_mayorca_soto;

-- Tabla Empleado
CREATE TABLE Empleado (
    id_empleado SERIAL PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    area VARCHAR(50) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- Tabla Turno
CREATE TABLE Turno (
    id_turno SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    descripcion VARCHAR(200)
);

-- Tabla Horario
CREATE TABLE Horario (
    id_horario SERIAL PRIMARY KEY,
    id_empleado INT NOT NULL,
    id_turno INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado),
    FOREIGN KEY (id_turno) REFERENCES Turno(id_turno)
);

-- Tabla Asistencia
CREATE TYPE asistencia_estado AS ENUM ('Presente', 'Ausente', 'Tardanza');

CREATE TABLE Asistencia (
    id_asistencia SERIAL PRIMARY KEY,
    id_empleado INT NOT NULL,
    id_horario INT NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    estado asistencia_estado NOT NULL,
    justificacion VARCHAR(200),
    FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado),
    FOREIGN KEY (id_horario) REFERENCES Horario(id_horario)
);

-- Tabla Usuario

CREATE TABLE Usuario (
    id_usuario SERIAL PRIMARY KEY,
    id_empleado INT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado)
);