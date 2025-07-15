package Prueba;


import Service.AsistenciaReporteService;
import businessEntity.*;
import dao.*;
import dto.AsistenciaReporteDTO;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;
import util.ConexionBD;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static Connection conn;
    private static EmpleadoDAO empleadoDAO;
    private static TurnoDAO turnoDAO;
    private static HorarioDAO horarioDAO;
    private static AsistenciaDAO asistenciaDAO;
    private static UsuarioDAO usuarioDAO;
    private static AsistenciaReporteService reporteService;

    public static void main(String[] args) {
        try {
            conn = ConexionBD.getConnection();
            System.out.println("Conexion exitosa a PostgreSQL!");
            
            empleadoDAO = new EmpleadoDAO(conn);
            turnoDAO = new TurnoDAO(conn);
            horarioDAO = new HorarioDAO(conn);
            asistenciaDAO = new AsistenciaDAO(conn);
            usuarioDAO = new UsuarioDAO(conn);
            reporteService = new AsistenciaReporteService(conn);
            
            mostrarMenuPrincipal();
            
        } catch (Exception e) {
            System.err.println("Error en la conexion o operaciones: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar la conexion: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== SISTEMA DE GESTION DE ASISTENCIA ===");
            System.out.println("1. Gestion de Empleados");
            System.out.println("2. Gestion de Turnos");
            System.out.println("3. Gestion de Horarios");
            System.out.println("4. Registro de Asistencias");
            System.out.println("5. Generar Reportes");
            System.out.println("6. Gestion de Usuarios");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            
            switch (opcion) {
                case 1:
                    gestionEmpleados();
                    break;
                case 2:
                    gestionTurnos();
                    break;
                case 3:
                    gestionHorarios();
                    break;
                case 4:
                    gestionAsistencias();
                    break;
                case 5:
                    generarReportes();
                    break;
                case 6:
                    gestionUsuarios();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opcion no valida. Intente nuevamente.");
            }
        }
    }

    private static void gestionEmpleados() {
        while (true) {
            System.out.println("\n--- GESTION DE EMPLEADOS ---");
            System.out.println("1. Listar empleados");
            System.out.println("2. Agregar empleado");
            System.out.println("3. Actualizar empleado");
            System.out.println("4. Eliminar empleado");
            System.out.println("5. Buscar empleado por DNI");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        listarEmpleados();
                        break;
                    case 2:
                        agregarEmpleado();
                        break;
                    case 3:
                        actualizarEmpleado();
                        break;
                    case 4:
                        eliminarEmpleado();
                        break;
                    case 5:
                        buscarEmpleadoPorDNI();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listarEmpleados() throws Exception {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        System.out.println("\nLISTA DE EMPLEADOS:");
        System.out.printf("%-5s %-10s %-20s %-20s %-15s %-15s %-10s %-10s%n", 
                "ID", "DNI", "Nombres", "Apellidos", "Cargo", "Área", "Estado", "Líder");
        for (Empleado emp : empleados) {
            System.out.printf("%-5d %-10s %-20s %-20s %-15s %-15s %-10s %-10s%n",
                    emp.getIdEmpleado(), emp.getDni(), emp.getNombres(), emp.getApellidos(),
                    emp.getCargo(), emp.getArea(), emp.isEstado() ? "Activo" : "Inactivo",
                    emp.isEsLider() ? "Sí" : "No");
        }
    }

    private static void agregarEmpleado() throws Exception {
        System.out.println("\nAGREGAR NUEVO EMPLEADO");
        Empleado emp = new Empleado();
        
        System.out.print("DNI: ");
        emp.setDni(scanner.nextLine());
        
        System.out.print("Nombres: ");
        emp.setNombres(scanner.nextLine());
        
        System.out.print("Apellidos: ");
        emp.setApellidos(scanner.nextLine());
        
        System.out.print("Cargo: ");
        emp.setCargo(scanner.nextLine());
        
        System.out.print("Area: ");
        emp.setArea(scanner.nextLine());
        
        System.out.print("Estado (1=Activo, 0=Inactivo): ");
        emp.setEstado(scanner.nextInt() == 1);
        scanner.nextLine();
        
        System.out.print("Es lider? (1=Si, 0=No): ");
        emp.setEsLider(scanner.nextInt() == 1);
        scanner.nextLine();
        
        boolean resultado = empleadoDAO.insertar(emp);
        System.out.println(resultado ? "Empleado agregado con exito." : "Error al agregar empleado.");
    }

    private static void actualizarEmpleado() throws Exception {
        System.out.println("\nACTUALIZAR EMPLEADO");
        System.out.print("Ingrese el ID del empleado a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Empleado emp = empleadoDAO.obtenerPorId(id);
        if (emp == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea cambiar.");
        
        System.out.print("DNI (" + emp.getDni() + "): ");
        String dni = scanner.nextLine();
        if (!dni.isEmpty()) emp.setDni(dni);
        
        System.out.print("Nombres (" + emp.getNombres() + "): ");
        String nombres = scanner.nextLine();
        if (!nombres.isEmpty()) emp.setNombres(nombres);
        
        System.out.print("Apellidos (" + emp.getApellidos() + "): ");
        String apellidos = scanner.nextLine();
        if (!apellidos.isEmpty()) emp.setApellidos(apellidos);
        
        System.out.print("Cargo (" + emp.getCargo() + "): ");
        String cargo = scanner.nextLine();
        if (!cargo.isEmpty()) emp.setCargo(cargo);
        
        System.out.print("Área (" + emp.getArea() + "): ");
        String area = scanner.nextLine();
        if (!area.isEmpty()) emp.setArea(area);
        
        System.out.print("Estado (" + (emp.isEstado() ? "Activo" : "Inactivo") + ") (1=Activo, 0=Inactivo, Enter=No cambiar): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) emp.setEstado(estado.equals("1"));
        
        System.out.print("Es lider? (" + (emp.isEsLider() ? "Si" : "No") + ") (1=Sí, 0=No, Enter=No cambiar): ");
        String lider = scanner.nextLine();
        if (!lider.isEmpty()) emp.setEsLider(lider.equals("1"));
        
        boolean resultado = empleadoDAO.actualizar(emp);
        System.out.println(resultado ? "Empleado actualizado con exito." : "Error al actualizar empleado.");
    }

    private static void eliminarEmpleado() throws Exception {
        System.out.println("\nELIMINAR EMPLEADO");
        System.out.print("Ingrese el ID del empleado a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Esta seguro que desea eliminar este empleado? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            boolean resultado = empleadoDAO.eliminar(id);
            System.out.println(resultado ? "Empleado eliminado con éxito." : "Error al eliminar empleado.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private static void buscarEmpleadoPorDNI() {
        System.out.println("\nBUSCAR EMPLEADO POR DNI");
        System.out.print("Ingrese el DNI: ");
        String dni = scanner.nextLine();
        
        Empleado emp = empleadoDAO.obtenerPorDNI(dni);
        if (emp != null) {
            System.out.println("\nEMPLEADO ENCONTRADO:");
            System.out.println("ID: " + emp.getIdEmpleado());
            System.out.println("DNI: " + emp.getDni());
            System.out.println("Nombres: " + emp.getNombres());
            System.out.println("Apellidos: " + emp.getApellidos());
            System.out.println("Cargo: " + emp.getCargo());
            System.out.println("Area: " + emp.getArea());
            System.out.println("Estado: " + (emp.isEstado() ? "Activo" : "Inactivo"));
            System.out.println("Es lider: " + (emp.isEsLider() ? "Sí" : "No"));
        } else {
            System.out.println("No se encontro ningun empleado con ese DNI.");
        }
    }

    private static void gestionTurnos() {
        while (true) {
            System.out.println("\n--- GESTION DE TURNOS ---");
            System.out.println("1. Listar turnos");
            System.out.println("2. Agregar turno");
            System.out.println("3. Actualizar turno");
            System.out.println("4. Eliminar turno");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        listarTurnos();
                        break;
                    case 2:
                        agregarTurno();
                        break;
                    case 3:
                        actualizarTurno();
                        break;
                    case 4:
                        eliminarTurno();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listarTurnos() throws Exception {
        List<Turno> turnos = turnoDAO.obtenerTodos();
        System.out.println("\nLISTA DE TURNOS:");
        System.out.printf("%-5s %-15s %-30s %-10s %-10s%n", 
                "ID", "Nombre", "Descripcion", "Inicio", "Fin");
        for (Turno turno : turnos) {
            System.out.printf("%-5d %-15s %-30s %-10s %-10s%n",
                    turno.getIdTurno(), turno.getNombre(), turno.getDescripcion(),
                    turno.getHoraInicio(), turno.getHoraFin());
        }
    }

    private static void agregarTurno() throws Exception {
        System.out.println("\nAGREGAR NUEVO TURNO");
        Turno turno = new Turno();
        
        System.out.print("Nombre: ");
        turno.setNombre(scanner.nextLine());
        
        System.out.print("Descripcion: ");
        turno.setDescripcion(scanner.nextLine());
        
        System.out.print("Hora de inicio (HH:MM): ");
        turno.setHoraInicio(LocalTime.parse(scanner.nextLine()));
        
        System.out.print("Hora de fin (HH:MM): ");
        turno.setHoraFin(LocalTime.parse(scanner.nextLine()));
        
        boolean resultado = turnoDAO.insertar(turno);
        System.out.println(resultado ? "Turno agregado con exito." : "Error al agregar turno.");
    }

    private static void actualizarTurno() throws Exception {
        System.out.println("\nACTUALIZAR TURNO");
        System.out.print("Ingrese el ID del turno a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Turno turno = turnoDAO.obtenerPorId(id);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea cambiar.");
        
        System.out.print("Nombre (" + turno.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) turno.setNombre(nombre);
        
        System.out.print("Descripcion (" + turno.getDescripcion() + "): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) turno.setDescripcion(descripcion);
        
        System.out.print("Hora de inicio (" + turno.getHoraInicio() + ") (HH:MM): ");
        String horaInicio = scanner.nextLine();
        if (!horaInicio.isEmpty()) turno.setHoraInicio(LocalTime.parse(horaInicio));
        
        System.out.print("Hora de fin (" + turno.getHoraFin() + ") (HH:MM): ");
        String horaFin = scanner.nextLine();
        if (!horaFin.isEmpty()) turno.setHoraFin(LocalTime.parse(horaFin));
        
        boolean resultado = turnoDAO.actualizar(turno);
        System.out.println(resultado ? "Turno actualizado con exito." : "Error al actualizar turno.");
    }

    private static void eliminarTurno() throws Exception {
        System.out.println("\nELIMINAR TURNO");
        System.out.print("Ingrese el ID del turno a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Esta seguro que desea eliminar este turno? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            boolean resultado = turnoDAO.eliminar(id);
            System.out.println(resultado ? "Turno eliminado con exito." : "Error al eliminar turno.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void gestionHorarios() {
        while (true) {
            System.out.println("\n--- GESTION DE HORARIOS ---");
            System.out.println("1. Listar horarios");
            System.out.println("2. Asignar horario a empleado");
            System.out.println("3. Actualizar horario");
            System.out.println("4. Eliminar horario");
            System.out.println("5. Generar horarios desde mes base");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        listarHorarios();
                        break;
                    case 2:
                        asignarHorario();
                        break;
                    case 3:
                        actualizarHorario();
                        break;
                    case 4:
                        eliminarHorario();
                        break;
                    case 5:
                        generarHorariosDesdeMesBase();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listarHorarios() throws Exception {
        List<Horario> horarios = horarioDAO.obtenerTodos();
        System.out.println("\nLISTA DE HORARIOS:");
        System.out.printf("%-5s %-10s %-10s %-15s %-15s %-15s %-10s%n", 
                "ID", "ID Empleado", "ID Turno", "Fecha", "Hora Inicio", "Hora Fin", "Estado");
        for (Horario horario : horarios) {
            System.out.printf("%-5d %-10d %-10d %-15s %-15s %-15s %-10s%n",
                    horario.getIdHorario(), horario.getIdEmpleado(), horario.getIdTurno(),
                    horario.getFecha(), horario.getHoraInicio(), horario.getHoraFin(),
                    horario.isEstado() ? "Activo" : "Inactivo");
        }
    }

    private static void asignarHorario() throws Exception {
        System.out.println("\nASIGNAR HORARIO A EMPLEADO");
        Horario horario = new Horario();
        
        System.out.print("ID del empleado: ");
        horario.setIdEmpleado(scanner.nextInt());
        scanner.nextLine();
        
        System.out.print("ID del turno: ");
        horario.setIdTurno(scanner.nextInt());
        scanner.nextLine();
        
        System.out.print("Fecha (AAAA-MM-DD): ");
        horario.setFecha(LocalDate.parse(scanner.nextLine()));
        
        System.out.print("Hora de inicio (HH:MM): ");
        horario.setHoraInicio(LocalTime.parse(scanner.nextLine()));
        
        System.out.print("Hora de fin (HH:MM): ");
        horario.setHoraFin(LocalTime.parse(scanner.nextLine()));
        
        System.out.print("Estado (1=Activo, 0=Inactivo): ");
        horario.setEstado(scanner.nextInt() == 1);
        scanner.nextLine();
        
        boolean resultado = horarioDAO.insertar(horario);
        System.out.println(resultado ? "Horario asignado con exito." : "Error al asignar horario.");
    }

    private static void actualizarHorario() throws Exception {
        System.out.println("\nACTUALIZAR HORARIO");
        System.out.print("Ingrese el ID del horario a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Horario horario = horarioDAO.obtenerPorId(id);
        if (horario == null) {
            System.out.println("Horario no encontrado.");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea cambiar (excepto ID).");
        
        System.out.print("ID Empleado (" + horario.getIdEmpleado() + "): ");
        String idEmpleado = scanner.nextLine();
        if (!idEmpleado.isEmpty()) horario.setIdEmpleado(Integer.parseInt(idEmpleado));
        
        System.out.print("ID Turno (" + horario.getIdTurno() + "): ");
        String idTurno = scanner.nextLine();
        if (!idTurno.isEmpty()) horario.setIdTurno(Integer.parseInt(idTurno));
        
        System.out.print("Fecha (" + horario.getFecha() + ") (AAAA-MM-DD): ");
        String fecha = scanner.nextLine();
        if (!fecha.isEmpty()) horario.setFecha(LocalDate.parse(fecha));
        
        System.out.print("Hora de inicio (" + horario.getHoraInicio() + ") (HH:MM): ");
        String horaInicio = scanner.nextLine();
        if (!horaInicio.isEmpty()) horario.setHoraInicio(LocalTime.parse(horaInicio));
        
        System.out.print("Hora de fin (" + horario.getHoraFin() + ") (HH:MM): ");
        String horaFin = scanner.nextLine();
        if (!horaFin.isEmpty()) horario.setHoraFin(LocalTime.parse(horaFin));
        
        System.out.print("Estado (" + (horario.isEstado() ? "Activo" : "Inactivo") + ") (1=Activo, 0=Inactivo, Enter=No cambiar): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) horario.setEstado(estado.equals("1"));
        
        boolean resultado = horarioDAO.actualizar(horario);
        System.out.println(resultado ? "Horario actualizado con exito." : "Error al actualizar horario.");
    }

    private static void eliminarHorario() throws Exception {
        System.out.println("\nELIMINAR HORARIO");
        System.out.print("Ingrese el ID del horario a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Esta seguro que desea eliminar este horario? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            boolean resultado = horarioDAO.eliminar(id);
            System.out.println(resultado ? "Horario eliminado con éxito." : "Error al eliminar horario.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private static void generarHorariosDesdeMesBase() throws Exception {
        System.out.println("\nGENERAR HORARIOS DESDE MES BASE");
        
        System.out.print("Anho base: ");
        int anioBase = scanner.nextInt();
        
        System.out.print("Mes base (1-12): ");
        int mesBase = scanner.nextInt();
        
        System.out.print("Año destino: ");
        int anioDestino = scanner.nextInt();
        
        System.out.print("Mes destino (1-12): ");
        int mesDestino = scanner.nextInt();
        scanner.nextLine();
        
        boolean resultado = horarioDAO.generarHorariosDesdeMesBase(anioBase, mesBase, anioDestino, mesDestino);
        System.out.println(resultado ? "Horarios generados con exito." : "Error al generar horarios.");
    }

    private static void gestionAsistencias() {
        while (true) {
            System.out.println("\n--- REGISTRO DE ASISTENCIAS ---");
            System.out.println("1. Registrar asistencia");
            System.out.println("2. Listar asistencias");
            System.out.println("3. Actualizar asistencia");
            System.out.println("4. Eliminar asistencia");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        registrarAsistencia();
                        break;
                    case 2:
                        listarAsistencias();
                        break;
                    case 3:
                        actualizarAsistencia();
                        break;
                    case 4:
                        eliminarAsistencia();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void registrarAsistencia() throws Exception {
        System.out.println("\nREGISTRAR ASISTENCIA");
        Asistencia asistencia = new Asistencia();
        
        System.out.print("ID del empleado: ");
        asistencia.setIdEmpleado(scanner.nextInt());
        scanner.nextLine();
        
        System.out.print("ID del horario: ");
        asistencia.setIdHorario(scanner.nextInt());
        scanner.nextLine();
        
        asistencia.setFechaHora(LocalDateTime.now());
        System.out.println("Fecha y hora actual: " + asistencia.getFechaHora());
        
        System.out.print("Estado (Asistió/Tardanza/Falta): ");
        asistencia.setEstado(scanner.nextLine());
        
        System.out.print("Justificacion (opcional): ");
        asistencia.setJustificacion(scanner.nextLine());
        
        boolean resultado = asistenciaDAO.insertar(asistencia);
        System.out.println(resultado ? "Asistencia registrada con exito." : "Error al registrar asistencia.");
    }

    private static void listarAsistencias() throws Exception {
        List<Asistencia> asistencias = asistenciaDAO.obtenerTodos();
        System.out.println("\nLISTA DE ASISTENCIAS:");
        System.out.printf("%-5s %-10s %-10s %-20s %-15s %-30s%n", 
                "ID", "ID Empleado", "ID Horario", "Fecha y Hora", "Estado", "Justificación");
        for (Asistencia asistencia : asistencias) {
            System.out.printf("%-5d %-10d %-10d %-20s %-15s %-30s%n",
                    asistencia.getIdAsistencia(), asistencia.getIdEmpleado(), asistencia.getIdHorario(),
                    asistencia.getFechaHora(), asistencia.getEstado(), asistencia.getJustificacion());
        }
    }

    private static void actualizarAsistencia() throws Exception {
        System.out.println("\nACTUALIZAR ASISTENCIA");
        System.out.print("Ingrese el ID de la asistencia a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Asistencia asistencia = asistenciaDAO.obtenerPorId(id);
        if (asistencia == null) {
            System.out.println("Asistencia no encontrada.");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea cambiar.");
        
        System.out.print("ID Empleado (" + asistencia.getIdEmpleado() + "): ");
        String idEmpleado = scanner.nextLine();
        if (!idEmpleado.isEmpty()) asistencia.setIdEmpleado(Integer.parseInt(idEmpleado));
        
        System.out.print("ID Horario (" + asistencia.getIdHorario() + "): ");
        String idHorario = scanner.nextLine();
        if (!idHorario.isEmpty()) asistencia.setIdHorario(Integer.parseInt(idHorario));
        
        System.out.print("Fecha y hora (" + asistencia.getFechaHora() + ") (AAAA-MM-DDTHH:MM:SS): ");
        String fechaHora = scanner.nextLine();
        if (!fechaHora.isEmpty()) asistencia.setFechaHora(LocalDateTime.parse(fechaHora));
        
        System.out.print("Estado (" + asistencia.getEstado() + "): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) asistencia.setEstado(estado);
        
        System.out.print("Justificación (" + asistencia.getJustificacion() + "): ");
        String justificacion = scanner.nextLine();
        if (!justificacion.isEmpty()) asistencia.setJustificacion(justificacion);
        
        boolean resultado = asistenciaDAO.actualizar(asistencia);
        System.out.println(resultado ? "Asistencia actualizada con éxito." : "Error al actualizar asistencia.");
    }

    private static void eliminarAsistencia() throws Exception {
        System.out.println("\nELIMINAR ASISTENCIA");
        System.out.print("Ingrese el ID de la asistencia a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Esta seguro que desea eliminar esta asistencia? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            boolean resultado = asistenciaDAO.eliminar(id);
            System.out.println(resultado ? "Asistencia eliminada con exito." : "Error al eliminar asistencia.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private static void generarReportes() {
        while (true) {
            System.out.println("\n--- GENERAR REPORTES ---");
            System.out.println("1. Reporte mensual de asistencia por empleado");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        generarReporteMensual();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void generarReporteMensual() throws Exception {
        System.out.println("\nREPORTE MENSUAL DE ASISTENCIA POR EMPLEADO");
        
        System.out.print("ID del empleado: ");
        int idEmpleado = scanner.nextInt();
        
        System.out.print("Año: ");
        int anio = scanner.nextInt();
        
        System.out.print("Mes (1-12): ");
        int mes = scanner.nextInt();
        scanner.nextLine();
        
        List<AsistenciaReporteDTO> reporte = reporteService.generarReporteMensual(idEmpleado, anio, mes);
        
        System.out.println("\nREPORTE DE ASISTENCIA MENSUAL:");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-30s%n", 
                "Fecha", "Hora Inicio", "Hora Fin", "Hora Registro", "Estado", "Justificación");
        
        for (AsistenciaReporteDTO registro : reporte) {
            System.out.printf("%-15s %-15s %-15s %-15s %-15s %-30s%n",
                    registro.getFecha(),
                    registro.getHoraEsperadaInicio(),
                    registro.getHoraEsperadaFin(),
                    registro.getHoraRegistro() != null ? registro.getHoraRegistro() : "N/A",
                    registro.getEstado(),
                    registro.getJustificacion() != null ? registro.getJustificacion() : "N/A");
        }
    }

    private static void gestionUsuarios() {
        while (true) {
            System.out.println("\n--- GESTION DE USUARIOS ---");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Agregar usuario");
            System.out.println("3. Actualizar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("5. Buscar usuario por username");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (opcion) {
                    case 1:
                        listarUsuarios();
                        break;
                    case 2:
                        agregarUsuario();
                        break;
                    case 3:
                        actualizarUsuario();
                        break;
                    case 4:
                        eliminarUsuario();
                        break;
                    case 5:
                        buscarUsuarioPorUsername();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opcion no válida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listarUsuarios() throws Exception {
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        System.out.println("\nLISTA DE USUARIOS:");
        System.out.printf("%-5s %-10s %-15s %-15s %-10s%n", 
                "ID", "ID Empleado", "Username", "Password", "Estado");
        for (Usuario usuario : usuarios) {
            System.out.printf("%-5d %-10d %-15s %-15s %-10s%n",
                    usuario.getIdUsuario(), usuario.getIdEmpleado(), 
                    usuario.getUsername(), usuario.getPassword(),
                    usuario.isEstado() ? "Activo" : "Inactivo");
        }
    }

    private static void agregarUsuario() throws Exception {
        System.out.println("\nAGREGAR NUEVO USUARIO");
        Usuario usuario = new Usuario();
        
        System.out.print("ID del empleado: ");
        usuario.setIdEmpleado(scanner.nextInt());
        scanner.nextLine();
        
        System.out.print("Username: ");
        usuario.setUsername(scanner.nextLine());
        
        System.out.print("Password: ");
        usuario.setPassword(scanner.nextLine());
        
        System.out.print("Estado (1=Activo, 0=Inactivo): ");
        usuario.setEstado(scanner.nextInt() == 1);
        scanner.nextLine();
        
        boolean resultado = usuarioDAO.insertar(usuario);
        System.out.println(resultado ? "Usuario agregado con éxito." : "Error al agregar usuario.");
    }

    private static void actualizarUsuario() throws Exception {
        System.out.println("\nACTUALIZAR USUARIO");
        System.out.print("Ingrese el ID del usuario a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Usuario usuario = usuarioDAO.obtenerPorId(id);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea cambiar.");
        
        System.out.print("ID Empleado (" + usuario.getIdEmpleado() + "): ");
        String idEmpleado = scanner.nextLine();
        if (!idEmpleado.isEmpty()) usuario.setIdEmpleado(Integer.parseInt(idEmpleado));
        
        System.out.print("Username (" + usuario.getUsername() + "): ");
        String username = scanner.nextLine();
        if (!username.isEmpty()) usuario.setUsername(username);
        
        System.out.print("Password (dejar en blanco para no cambiar): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) usuario.setPassword(password);
        
        System.out.print("Estado (" + (usuario.isEstado() ? "Activo" : "Inactivo") + ") (1=Activo, 0=Inactivo, Enter=No cambiar): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) usuario.setEstado(estado.equals("1"));
        
        boolean resultado = usuarioDAO.actualizar(usuario);
        System.out.println(resultado ? "Usuario actualizado con éxito." : "Error al actualizar usuario.");
    }

    private static void eliminarUsuario() throws Exception {
        System.out.println("\nELIMINAR USUARIO");
        System.out.print("Ingrese el ID del usuario a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Esta seguro que desea eliminar este usuario? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            boolean resultado = usuarioDAO.eliminar(id);
            System.out.println(resultado ? "Usuario eliminado con éxito." : "Error al eliminar usuario.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private static void buscarUsuarioPorUsername() throws Exception {
        System.out.println("\nBUSCAR USUARIO POR USERNAME");
        System.out.print("Ingrese el username: ");
        String username = scanner.nextLine();
        
        Usuario usuario = usuarioDAO.obtenerPorUsername(username);
        if (usuario != null) {
            System.out.println("\nUSUARIO ENCONTRADO:");
            System.out.println("ID: " + usuario.getIdUsuario());
            System.out.println("ID Empleado: " + usuario.getIdEmpleado());
            System.out.println("Username: " + usuario.getUsername());
            System.out.println("Password: " + usuario.getPassword());
            System.out.println("Estado: " + (usuario.isEstado() ? "Activo" : "Inactivo"));
        } else {
            System.out.println("No se encontro ningun usuario con ese username.");
        }
    }
}
