package tip.java.sistemacentrocrecer.configurations.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;
import tip.java.sistemacentrocrecer.biz.dao.entities.Agenda;
import tip.java.sistemacentrocrecer.biz.dao.entities.AgendaLimpieza;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.biz.dao.entities.DetalleAgenda;
import tip.java.sistemacentrocrecer.biz.dao.entities.DiaNoLaborable;
import tip.java.sistemacentrocrecer.biz.dao.entities.DocumentoAdjunto;
import tip.java.sistemacentrocrecer.biz.dao.entities.EmpresaExterna;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Inscripcion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Notificacion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Permiso;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteGrupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteNinio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.biz.dao.entities.ResponsableNinio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.biz.dao.entities.SubtipoAgenda;
import tip.java.sistemacentrocrecer.biz.dao.entities.TipoAgenda;
import tip.java.sistemacentrocrecer.biz.dao.entities.Turno;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;
import tip.java.sistemacentrocrecer.biz.dao.enums.TipoDiaNoLaborableEnum;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ActividadRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AgendaLimpiezaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AgendaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AsistenciaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.CondicionMedicaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.DetalleAgendaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.DiaNoLaborableRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.EmpresaExternaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.InscripcionRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NotificacionRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.PermisoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableNinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.SubtipoAgendaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.TipoAgendaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.TurnoRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ResponsableRepository responsableRepository;
    private final NinioRepository ninioRepository;
    private final ResponsableNinioRepository responsableNinioRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final GrupoRepository grupoRepository;
    private final TipoAgendaRepository tipoAgendaRepository;
    private final SubtipoAgendaRepository subtipoAgendaRepository;
    private final TurnoRepository turnoRepository;
    private final AgendaRepository agendaRepository;
    private final DetalleAgendaRepository detalleAgendaRepository;
    private final AgendaLimpiezaRepository agendaLimpiezaRepository;
    private final CondicionMedicaRepository condicionMedicaRepository;
    private final ActividadRepository actividadRepository;
    private final EmpresaExternaRepository empresaExternaRepository;
    private final PermisoRepository permisoRepository;
    private final ReporteRepository reporteRepository;
    private final NotificacionRepository notificacionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final DiaNoLaborableRepository diaNoLaborableRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEMO_PASSWORD = "Centro1234";

    @Value("${app.default-admin.email}")
    private String adminEmail;

    @Value("${app.default-admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        log.info("Iniciando carga de datos iniciales...");

        seedRoles();
        seedAdminUser();
        seedDemoFuncionarios();
        seedDemoResponsables();
        seedDemoNiniosYResponsables();
        seedDemoGrupos();
        seedDemoTiposAgenda();
        seedDemoTurnos();
        seedDemoCondicionesMedicas();
        seedDemoCalendarioLaboral();
        seedDemoInscripciones();
        seedDemoAgendas();
        seedDemoAgendaLimpieza();
        seedDemoActividades();
        seedDemoReportes();
        seedDemoNotificaciones();
        seedDemoAsistencias();

        log.info("Carga de datos iniciales completada.");
    }

    private void seedRoles() {

        log.info("Cargando roles del sistema...");

        Rol adminSistema = createRol("ADMINISTRADOR_SISTEMA", null);

        Rol coordinadora = createRol("COORDINADORA", null);

        Rol asistenteSocial = createRol("ASISTENTE_SOCIAL", coordinadora);

        Rol psicologo = createRol("PSICOLOGO", coordinadora);

        Rol psicomotricista = createRol("PSICOMOTRICISTA", coordinadora);

        Rol maestra = createRol("MAESTRA", coordinadora);

        Rol administrativo = createRol("ADMINISTRATIVO", coordinadora);

        Rol educador = createRol("EDUCADOR", coordinadora);

        Rol talleristaPlastica = createRol("TALLERISTA_PLASTICA", coordinadora);

        Rol talleristaCeramica = createRol("TALLERISTA_CERAMICA", coordinadora);

        Rol talleristaCorporal = createRol("TALLERISTA_CORPORAL", coordinadora);

        Rol auxiliarLimpieza = createRol("AUXILIAR_LIMPIEZA", coordinadora);

        log.info("Roles cargados correctamente: {}",
                List.of(
                        adminSistema.getNombre(),
                        coordinadora.getNombre(),
                        asistenteSocial.getNombre(),
                        psicologo.getNombre(),
                        psicomotricista.getNombre(),
                        maestra.getNombre(),
                        administrativo.getNombre(),
                        educador.getNombre(),
                        talleristaPlastica.getNombre(),
                        talleristaCeramica.getNombre(),
                        talleristaCorporal.getNombre(),
                        auxiliarLimpieza.getNombre()
                )
        );
    }

    private Rol createRol(String nombre, Rol padre) {

        return rolRepository.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> {

                    log.info("Creando rol: {}", nombre);

                    Rol rol = Rol.builder()
                            .nombre(nombre)
                            .activo(true)
                            .padre(padre)
                            .build();

                    return rolRepository.save(rol);
                });
    }

    private void seedAdminUser() {

        if (funcionarioRepository.existsByEmail(adminEmail)) {

            log.info("El usuario administrador ya existe. Omitiendo creación.");

            return;
        }

        log.info("Creando usuario administrador por defecto...");

        Rol adminRol = rolRepository
                .findByNombreIgnoreCase("ADMINISTRADOR_SISTEMA")
                .orElseThrow(() -> new RuntimeException("No se encontró el rol ADMINISTRADOR_SISTEMA."));

        Funcionario admin = Funcionario.builder()
                .cedula("00000001")
                .nombre("Administrador")
                .apellido("Sistema")
                .email(adminEmail)
                .telefono("+59899000000")
                .contrasenia(passwordEncoder.encode(adminPassword))
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .activo(true)
                .rol(adminRol)
                .build();

        funcionarioRepository.save(admin);

        log.info("Usuario administrador creado correctamente: {}", adminEmail);
    }

    private void seedDemoFuncionarios() {

        log.info("Cargando funcionarios de ejemplo...");

        seedFuncionario(
                "45678123",
                "Marcela",
                "Rodriguez",
                "marcela.rodriguez@centrocrecer.example.com",
                "+59899123456",
                LocalDate.of(1982, 4, 18),
                "COORDINADORA"
        );

        seedFuncionario(
                "46789234",
                "Andrea",
                "Silva",
                "andrea.silva@centrocrecer.example.com",
                "+59899234567",
                LocalDate.of(1990, 9, 3),
                "PSICOLOGO"
        );

        seedFuncionario(
                "47890345",
                "Lucia",
                "Fernandez",
                "lucia.fernandez@centrocrecer.example.com",
                "+59899345678",
                LocalDate.of(1988, 2, 26),
                "MAESTRA"
        );

        seedFuncionario(
                "48901456",
                "Diego",
                "Pereira",
                "diego.pereira@centrocrecer.example.com",
                "+59899456789",
                LocalDate.of(1994, 7, 12),
                "EDUCADOR"
        );

        seedFuncionario(
                "49012567",
                "Valentina",
                "Castro",
                "valentina.castro@centrocrecer.example.com",
                "+59899567890",
                LocalDate.of(1992, 11, 8),
                "ASISTENTE_SOCIAL"
        );

        seedFuncionario(
                "40123678",
                "Sofia",
                "Mendez",
                "sofia.mendez@centrocrecer.example.com",
                "+59899678901",
                LocalDate.of(1993, 6, 21),
                "PSICOMOTRICISTA"
        );

        seedFuncionario(
                "41234789",
                "Camila",
                "Ramos",
                "camila.ramos@centrocrecer.example.com",
                "+59899789012",
                LocalDate.of(1991, 10, 4),
                "ADMINISTRATIVO"
        );

        seedFuncionario(
                "42345890",
                "Florencia",
                "Suarez",
                "florencia.suarez@centrocrecer.example.com",
                "+59899890123",
                LocalDate.of(1995, 1, 16),
                "TALLERISTA_PLASTICA"
        );

        seedFuncionario(
                "43456901",
                "Matias",
                "Nunez",
                "matias.nunez@centrocrecer.example.com",
                "+59899901234",
                LocalDate.of(1989, 8, 9),
                "TALLERISTA_CERAMICA"
        );

        seedFuncionario(
                "44567012",
                "Paula",
                "Ibarra",
                "paula.ibarra@centrocrecer.example.com",
                "+59899012345",
                LocalDate.of(1990, 12, 27),
                "TALLERISTA_CORPORAL"
        );

        seedFuncionario(
                "45670123",
                "Romina",
                "Alvarez",
                "romina.alvarez@centrocrecer.example.com",
                "+59899111222",
                LocalDate.of(1985, 3, 11),
                "AUXILIAR_LIMPIEZA"
        );

        seedFuncionario(
                "46781234",
                "Gonzalo",
                "Sosa",
                "gonzalo.sosa@centrocrecer.example.com",
                "+59899222333",
                LocalDate.of(1987, 9, 25),
                "EDUCADOR"
        );

        seedFuncionario(
                "47892345",
                "Noelia",
                "Molina",
                "noelia.molina@centrocrecer.example.com",
                "+59899333444",
                LocalDate.of(1996, 5, 7),
                "MAESTRA"
        );

        seedFuncionario(
                "48903456",
                "Federico",
                "Cabrera",
                "federico.cabrera@centrocrecer.example.com",
                "+59899444555",
                LocalDate.of(1983, 2, 13),
                "PSICOLOGO"
        );

        seedFuncionario(
                "49014567",
                "Agustina",
                "Torres",
                "agustina.torres@centrocrecer.example.com",
                "+59899555666",
                LocalDate.of(1994, 4, 2),
                "COORDINADORA"
        );
    }

    private void seedFuncionario(String cedula, String nombre, String apellido, String email,
                                 String telefono, LocalDate fechaNacimiento, String rolNombre) {

        if (funcionarioRepository.existsByEmail(email) || funcionarioRepository.existsByCedula(cedula)) {
            log.info("Funcionario de ejemplo ya existe. Omitiendo: {}", email);
            return;
        }

        Rol rol = rolRepository
                .findByNombreIgnoreCase(rolNombre)
                .orElseThrow(() -> new RuntimeException("No se encontro el rol " + rolNombre + "."));

        Funcionario funcionario = Funcionario.builder()
                .cedula(cedula)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .telefono(telefono)
                .contrasenia(passwordEncoder.encode(DEMO_PASSWORD))
                .fechaNacimiento(fechaNacimiento)
                .activo(true)
                .mustChangePassword(false)
                .rol(rol)
                .build();

        funcionarioRepository.save(funcionario);

        log.info("Funcionario de ejemplo creado: {}", email);
    }

    private void seedDemoResponsables() {

        log.info("Cargando responsables de ejemplo...");

        seedResponsable(
                "50123678",
                "Laura",
                "Martinez",
                "laura.martinez@example.com",
                "+59898123456",
                LocalDate.of(1987, 5, 14)
        );

        seedResponsable(
                "51234789",
                "Pablo",
                "Gonzalez",
                "pablo.gonzalez@example.com",
                "+59898234567",
                LocalDate.of(1984, 1, 22)
        );

        seedResponsable(
                "52345890",
                "Carolina",
                "Lopez",
                "carolina.lopez@example.com",
                "+59898345678",
                LocalDate.of(1991, 8, 30)
        );

        seedResponsable(
                "53456901",
                "Santiago",
                "Acosta",
                "santiago.acosta@example.com",
                "+59898456789",
                LocalDate.of(1986, 12, 6)
        );

        seedResponsable(
                "54567012",
                "Natalia",
                "Varela",
                "natalia.varela@example.com",
                "+59898567890",
                LocalDate.of(1989, 3, 19)
        );

        seedResponsable(
                "55678123",
                "Martin",
                "Rivas",
                "martin.rivas@example.com",
                "+59898678901",
                LocalDate.of(1983, 7, 2)
        );

        seedResponsable(
                "56789234",
                "Gabriela",
                "Mendez",
                "gabriela.mendez@example.com",
                "+59898789012",
                LocalDate.of(1990, 10, 17)
        );

        seedResponsable(
                "57890345",
                "Sebastian",
                "Suarez",
                "sebastian.suarez@example.com",
                "+59898890123",
                LocalDate.of(1985, 4, 9)
        );

        seedResponsable(
                "58901456",
                "Veronica",
                "Ramos",
                "veronica.ramos@example.com",
                "+59898901234",
                LocalDate.of(1988, 11, 28)
        );

        seedResponsable(
                "59012567",
                "Rodrigo",
                "Cabrera",
                "rodrigo.cabrera@example.com",
                "+59898012345",
                LocalDate.of(1982, 6, 5)
        );

        seedResponsable(
                "60123678",
                "Paola",
                "Almeida",
                "paola.almeida@example.com",
                "+59898111222",
                LocalDate.of(1992, 1, 13)
        );

        seedResponsable(
                "61234789",
                "Ignacio",
                "Molina",
                "ignacio.molina@example.com",
                "+59898222333",
                LocalDate.of(1986, 9, 21)
        );

        seedResponsable(
                "62345890",
                "Cecilia",
                "Pintos",
                "cecilia.pintos@example.com",
                "+59898333444",
                LocalDate.of(1991, 12, 1)
        );

        seedResponsable(
                "63456901",
                "Hernan",
                "Vega",
                "hernan.vega@example.com",
                "+59898444555",
                LocalDate.of(1984, 8, 14)
        );

        seedResponsable(
                "64567012",
                "Mariana",
                "Pereyra",
                "mariana.pereyra@example.com",
                "+59898555666",
                LocalDate.of(1987, 2, 23)
        );
    }

    private void seedResponsable(String cedula, String nombre, String apellido, String email,
                                 String telefono, LocalDate fechaNacimiento) {

        if (responsableRepository.findByEmail(email).isPresent()
                || responsableRepository.findByCedula(cedula).isPresent()) {
            log.info("Responsable de ejemplo ya existe. Omitiendo: {}", email);
            return;
        }

        Responsable responsable = Responsable.builder()
                .cedula(cedula)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .telefono(telefono)
                .contrasenia(passwordEncoder.encode(DEMO_PASSWORD))
                .fechaNacimiento(fechaNacimiento)
                .activo(true)
                .build();

        responsableRepository.save(responsable);

        log.info("Responsable de ejemplo creado: {}", email);
    }

    private void seedDemoNiniosYResponsables() {

        log.info("Cargando ninios de ejemplo y vinculos con responsables...");

        seedNinioConResponsable(
                "70123678",
                "Mateo",
                "Martinez",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2019, 5, 10),
                "Av. Italia 1234",
                "Sin observaciones",
                "50123678",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "71234789",
                "Sofia",
                "Gonzalez",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2020, 3, 18),
                "Bvar. Artigas 2450",
                "Retira tambien abuela autorizada",
                "51234789",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "72345890",
                "Benjamin",
                "Lopez",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2018, 11, 2),
                "Rivera 1589",
                "Requiere apoyo en adaptacion",
                "52345890",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "73456901",
                "Emma",
                "Acosta",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2021, 1, 27),
                "Colonia 940",
                "Sin observaciones",
                "53456901",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "74567012",
                "Joaquin",
                "Varela",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2019, 9, 14),
                "Mercedes 1120",
                "Trae medicacion indicada por familia",
                "54567012",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "75678123",
                "Valentina",
                "Rivas",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2020, 6, 6),
                "San Jose 1530",
                "Sin observaciones",
                "55678123",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "76789234",
                "Felipe",
                "Mendez",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2018, 8, 23),
                "Canelones 1875",
                "Preferencia por actividades plasticas",
                "56789234",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "77890345",
                "Catalina",
                "Suarez",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2021, 4, 12),
                "Durazno 990",
                "Sin observaciones",
                "57890345",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "78901456",
                "Tomas",
                "Ramos",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2019, 12, 30),
                "Maldonado 1432",
                "Asiste en horario matutino",
                "58901456",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "79012567",
                "Isabella",
                "Cabrera",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2020, 2, 8),
                "Minas 760",
                "Sin observaciones",
                "59012567",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "80123678",
                "Agustin",
                "Almeida",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2018, 7, 19),
                "Soriano 1288",
                "Necesita acompanamiento en comedor",
                "60123678",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "81234789",
                "Julieta",
                "Molina",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2021, 10, 3),
                "Constituyente 1644",
                "Sin observaciones",
                "61234789",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "82345890",
                "Santino",
                "Pintos",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2019, 1, 25),
                "Ejido 1180",
                "Buena integracion grupal",
                "62345890",
                "Madre",
                true
        );

        seedNinioConResponsable(
                "83456901",
                "Renata",
                "Vega",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2020, 9, 9),
                "Paysandu 1321",
                "Sin observaciones",
                "63456901",
                "Padre",
                true
        );

        seedNinioConResponsable(
                "84567012",
                "Lautaro",
                "Pereyra",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2018, 4, 16),
                "18 de Julio 2100",
                "Disfruta actividades corporales",
                "64567012",
                "Madre",
                true
        );

        // --- Sala Cuna (0 a 1 anios) ---

        seedResponsable(
                "65678123",
                "Camila",
                "Salinas",
                "camila.salinas@example.com",
                "+59898666777",
                LocalDate.of(1993, 5, 11)
        );

        seedNinioConResponsable(
                "90123678",
                "Bruno",
                "Salinas",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2025, 9, 4),
                "Treinta y Tres 880",
                "Lactante, requiere control de horarios de alimentacion",
                "65678123",
                "Madre",
                true
        );

        seedResponsable(
                "66789234",
                "Bruno",
                "Techera",
                "bruno.techera@example.com",
                "+59898777888",
                LocalDate.of(1990, 8, 24)
        );

        seedNinioConResponsable(
                "91234789",
                "Mia",
                "Techera",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2025, 11, 20),
                "Garibaldi 455",
                "Lactante, sin observaciones",
                "66789234",
                "Padre",
                true
        );

        seedResponsable(
                "67890345",
                "Yamila",
                "Correa",
                "yamila.correa@example.com",
                "+59898888999",
                LocalDate.of(1995, 2, 9)
        );

        seedNinioConResponsable(
                "92345890",
                "Thiago",
                "Correa",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2025, 12, 30),
                "Wilson Ferreira 700",
                "Lactante, en proceso de adaptacion",
                "67890345",
                "Madre",
                true
        );

        // --- Gateadores (1 a 2 anios) ---

        seedResponsable(
                "68901456",
                "Diego",
                "Bauza",
                "diego.bauza@example.com",
                "+59898999000",
                LocalDate.of(1988, 6, 17)
        );

        seedNinioConResponsable(
                "93456901",
                "Camila",
                "Bauza",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2024, 10, 15),
                "Florida 1023",
                "Comenzando a caminar, requiere apoyo motriz",
                "68901456",
                "Padre",
                true
        );

        seedResponsable(
                "69012567",
                "Florencia",
                "Heguy",
                "florencia.heguy@example.com",
                "+59898000111",
                LocalDate.of(1991, 4, 3)
        );

        seedNinioConResponsable(
                "94567012",
                "Bautista",
                "Heguy",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2024, 8, 2),
                "Tacuarembo 1455",
                "Sin observaciones",
                "69012567",
                "Madre",
                true
        );

        seedResponsable(
                "70234789",
                "Marcelo",
                "Andrade",
                "marcelo.andrade@example.com",
                "+59898111223",
                LocalDate.of(1986, 9, 28)
        );

        seedNinioConResponsable(
                "95678123",
                "Olivia",
                "Andrade",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2024, 12, 5),
                "Cerro Largo 320",
                "Sin observaciones",
                "70234789",
                "Padre",
                true
        );

        // --- Descubridores (2 a 3 anios) ---

        seedResponsable(
                "71345890",
                "Patricia",
                "Olivera",
                "patricia.olivera@example.com",
                "+59898222334",
                LocalDate.of(1984, 11, 19)
        );

        seedNinioConResponsable(
                "96789234",
                "Dante",
                "Olivera",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2023, 6, 22),
                "Rio Negro 980",
                "Buena adaptacion al grupo",
                "71345890",
                "Madre",
                true
        );

        seedResponsable(
                "72456901",
                "Ramiro",
                "Castelli",
                "ramiro.castelli@example.com",
                "+59898333445",
                LocalDate.of(1989, 1, 8)
        );

        seedNinioConResponsable(
                "97890345",
                "Abril",
                "Castelli",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2023, 9, 17),
                "Yi 1199",
                "Sin observaciones",
                "72456901",
                "Padre",
                true
        );

        seedResponsable(
                "73567012",
                "Daiana",
                "Recoba",
                "daiana.recoba@example.com",
                "+59898444556",
                LocalDate.of(1992, 3, 31)
        );

        seedNinioConResponsable(
                "98901456",
                "Ian",
                "Recoba",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2023, 11, 11),
                "Convencion 1340",
                "Le gustan las actividades al aire libre",
                "73567012",
                "Madre",
                true
        );

        // --- Aventureros (3 a 4 anios) ---

        seedResponsable(
                "74678123",
                "Lorena",
                "Bentancor",
                "lorena.bentancor@example.com",
                "+59898555667",
                LocalDate.of(1987, 7, 26)
        );

        seedNinioConResponsable(
                "99012567",
                "Maximo",
                "Bentancor",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2022, 4, 9),
                "Andes 1567",
                "Sin observaciones",
                "74678123",
                "Madre",
                true
        );

        seedResponsable(
                "75789234",
                "Ezequiel",
                "Larrosa",
                "ezequiel.larrosa@example.com",
                "+59898666778",
                LocalDate.of(1985, 10, 13)
        );

        seedNinioConResponsable(
                "10123678",
                "Delfina",
                "Larrosa",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2022, 7, 28),
                "Uruguay 2210",
                "Sin observaciones",
                "75789234",
                "Padre",
                true
        );

        seedResponsable(
                "76890345",
                "Vanessa",
                "Cardozo",
                "vanessa.cardozo@example.com",
                "+59898777889",
                LocalDate.of(1990, 12, 21)
        );

        seedNinioConResponsable(
                "11234789",
                "Bautista",
                "Cardozo",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2022, 2, 14),
                "Paraguay 845",
                "Requiere acompanamiento en siestas",
                "76890345",
                "Madre",
                true
        );

        // --- Pequenios Exploradores (4 a 5 anios) ---

        seedResponsable(
                "77901456",
                "Nicolas",
                "Ferraro",
                "nicolas.ferraro@example.com",
                "+59898888990",
                LocalDate.of(1983, 5, 5)
        );

        seedNinioConResponsable(
                "12345890",
                "Helena",
                "Ferraro",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2021, 8, 19),
                "Carlos Roxlo 612",
                "Sin observaciones",
                "77901456",
                "Padre",
                true
        );

        seedResponsable(
                "78012567",
                "Brenda",
                "Sanguinetti",
                "brenda.sanguinetti@example.com",
                "+59898999001",
                LocalDate.of(1986, 2, 17)
        );

        seedNinioConResponsable(
                "13456901",
                "Ciro",
                "Sanguinetti",
                SexoNinioEnum.MASCULINO,
                LocalDate.of(2021, 5, 3),
                "Joaquin Suarez 134",
                "Sin observaciones",
                "78012567",
                "Madre",
                true
        );

        seedResponsable(
                "79123678",
                "Alvaro",
                "Pintos",
                "alvaro.pintos@example.com",
                "+59898000112",
                LocalDate.of(1991, 9, 30)
        );

        seedNinioConResponsable(
                "14567012",
                "Martina",
                "Pintos",
                SexoNinioEnum.FEMENINO,
                LocalDate.of(2021, 12, 24),
                "Lord Ponsonby 980",
                "Le gusta participar en talleres de plastica",
                "79123678",
                "Padre",
                true
        );
    }

    private void seedNinioConResponsable(String cedula, String nombre, String apellido, SexoNinioEnum sexo,
                                         LocalDate fechaNacimiento, String direccion, String observaciones,
                                         String responsableCedula, String tipoRelacion,
                                         Boolean autorizadoRetiro) {

        Ninio ninio = ninioRepository.findByCedula(cedula)
                .orElseGet(() -> {
                    Ninio nuevoNinio = Ninio.builder()
                            .cedula(cedula)
                            .nombre(nombre)
                            .apellido(apellido)
                            .sexo(sexo)
                            .fechaNacimiento(java.sql.Date.valueOf(fechaNacimiento))
                            .direccion(direccion)
                            .observaciones(observaciones)
                            .activo(true)
                            .build();

                    Ninio guardado = ninioRepository.save(nuevoNinio);
                    log.info("Ninio de ejemplo creado: {} {}", nombre, apellido);
                    return guardado;
                });

        Responsable responsable = responsableRepository.findByCedula(responsableCedula)
                .orElseThrow(() -> new RuntimeException("No se encontro el responsable " + responsableCedula + "."));

        if (responsableNinioRepository.existsByNinioIdAndResponsableId(ninio.getId(), responsable.getId())) {
            log.info("Vinculo responsable-ninio ya existe. Omitiendo: {} -> {}", responsableCedula, cedula);
            return;
        }

        ResponsableNinio responsableNinio = ResponsableNinio.builder()
                .ninio(ninio)
                .responsable(responsable)
                .tipoRelacion(tipoRelacion)
                .autorizadoRetiro(autorizadoRetiro)
                .build();

        responsableNinioRepository.save(responsableNinio);

        log.info("Vinculo responsable-ninio creado: {} -> {}", responsableCedula, cedula);
    }

    private void seedDemoGrupos() {

        log.info("Cargando grupos de ejemplo...");

        Grupo exploradores = seedGrupo(
                "Exploradores",
                "5 a 12 anios",
                LocalTime.of(8, 30),
                LocalTime.of(12, 30),
                List.of("lucia.fernandez@centrocrecer.example.com", "diego.pereira@centrocrecer.example.com"),
                List.of("70123678", "71234789", "72345890", "73456901", "74567012")
        );

        Grupo creadores = seedGrupo(
                "Creadores",
                "5 a 12 anios",
                LocalTime.of(13, 0),
                LocalTime.of(17, 0),
                List.of("gonzalo.sosa@centrocrecer.example.com", "noelia.molina@centrocrecer.example.com"),
                List.of("75678123", "76789234", "77890345", "78901456", "79012567")
        );

        Grupo integracion = seedGrupo(
                "Integracion",
                "5 a 12 anios",
                LocalTime.of(9, 0),
                LocalTime.of(15, 0),
                List.of("marcela.rodriguez@centrocrecer.example.com", "valentina.castro@centrocrecer.example.com"),
                List.of("80123678", "81234789", "82345890", "83456901", "84567012")
        );

        Grupo lactantes = seedGrupo(
                "Sala Cuna",
                "0 a 1 anios",
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                List.of("sofia.mendez@centrocrecer.example.com", "andrea.silva@centrocrecer.example.com"),
                List.of("90123678", "91234789", "92345890")
        );

        Grupo gateadores = seedGrupo(
                "Gateadores",
                "1 a 2 anios",
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                List.of("federico.cabrera@centrocrecer.example.com", "agustina.torres@centrocrecer.example.com"),
                List.of("93456901", "94567012", "95678123")
        );

        Grupo descubridores = seedGrupo(
                "Descubridores",
                "2 a 3 anios",
                LocalTime.of(13, 0),
                LocalTime.of(17, 0),
                List.of("matias.nunez@centrocrecer.example.com", "florencia.suarez@centrocrecer.example.com"),
                List.of("96789234", "97890345", "98901456")
        );

        Grupo aventureros = seedGrupo(
                "Aventureros",
                "3 a 4 anios",
                LocalTime.of(8, 30),
                LocalTime.of(12, 30),
                List.of("paula.ibarra@centrocrecer.example.com", "diego.pereira@centrocrecer.example.com"),
                List.of("99012567", "10123678", "11234789")
        );

        Grupo exploradoresJunior = seedGrupo(
                "Pequenios Exploradores",
                "4 a 5 anios",
                LocalTime.of(13, 0),
                LocalTime.of(17, 0),
                List.of("lucia.fernandez@centrocrecer.example.com", "noelia.molina@centrocrecer.example.com"),
                List.of("12345890", "13456901", "14567012")
        );

        log.info("Grupos de ejemplo listos: {}, {}, {}, {}, {}, {}, {}, {}",
                exploradores.getNombre(), creadores.getNombre(), integracion.getNombre(),
                lactantes.getNombre(), gateadores.getNombre(), descubridores.getNombre(),
                aventureros.getNombre(), exploradoresJunior.getNombre());
    }

    private Grupo seedGrupo(String nombre, String rangoEdad, LocalTime horaInicio, LocalTime horaFin,
                            List<String> funcionariosEmails, List<String> niniosCedulas) {

        Grupo grupo = grupoRepository.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> {
                    Grupo nuevoGrupo = Grupo.builder()
                            .nombre(nombre)
                            .rangoEdad(rangoEdad)
                            .horaInicio(horaInicio)
                            .horaFin(horaFin)
                            .activo(true)
                            .build();
                    Grupo guardado = grupoRepository.save(nuevoGrupo);
                    log.info("Grupo de ejemplo creado: {}", nombre);
                    return guardado;
                });

        List<Funcionario> funcionarios = funcionariosEmails.stream()
                .map(email -> funcionarioRepository.findByEmail(email).orElse(null))
                .filter(funcionario -> funcionario != null)
                .toList();
        grupo.setFuncionarios(funcionarios);
        grupoRepository.save(grupo);

        for (String cedula : niniosCedulas) {
            ninioRepository.findByCedula(cedula).ifPresent(ninio -> {
                ninio.setGrupo(grupo);
                ninioRepository.save(ninio);
            });
        }

        return grupo;
    }

    private void seedDemoTiposAgenda() {

        log.info("Cargando tipos y subtipos de agenda...");

        seedTipoAgenda("Atencion a familias");
        seedTipoAgenda("Seguimiento pedagogico");
        seedTipoAgenda("Coordinacion interna");
        seedTipoAgenda("Taller grupal");
        seedTipoAgenda("Entrevista individual");

        seedSubtipoAgenda("Limpieza de aulas");
        seedSubtipoAgenda("Limpieza de cocina");
        seedSubtipoAgenda("Limpieza de banios");
        seedSubtipoAgenda("Materiales de taller");
        seedSubtipoAgenda("Evaluacion psicologica");
        seedSubtipoAgenda("Planificacion educativa");
    }

    private TipoAgenda seedTipoAgenda(String tipo) {
        return tipoAgendaRepository.findByTipo(tipo)
                .orElseGet(() -> tipoAgendaRepository.save(TipoAgenda.builder().tipo(tipo).build()));
    }

    private SubtipoAgenda seedSubtipoAgenda(String subtipo) {
        return subtipoAgendaRepository.findBySubtipo(subtipo)
                .orElseGet(() -> subtipoAgendaRepository.save(SubtipoAgenda.builder().subtipo(subtipo).build()));
    }

    private void seedDemoTurnos() {

        log.info("Cargando turnos de ejemplo...");

        for (Funcionario funcionario : funcionarioRepository.findAll()) {
            if (!funcionario.getEmail().endsWith("@centrocrecer.example.com")) {
                continue;
            }

            LocalTime inicio = "romina.alvarez@centrocrecer.example.com".equals(funcionario.getEmail())
                    ? LocalTime.of(7, 0)
                    : LocalTime.of(8, 0);
            LocalTime fin = inicio.plusHours(6);

            if (turnoRepository.existsByFuncionarioIdAndHoraInicioAndHoraFin(funcionario.getId(), inicio, fin)) {
                continue;
            }

            Turno turno = Turno.builder()
                    .funcionario(funcionario)
                    .horaInicio(inicio)
                    .horaFin(fin)
                    .activo(true)
                    .dias(List.of(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY
                    ))
                    .build();

            turnoRepository.save(turno);
        }
    }

    private void seedDemoCondicionesMedicas() {

        log.info("Cargando condiciones medicas de ejemplo...");

        seedCondicionMedica("72345890", "Asma leve", "Usa inhalador indicado por familia ante crisis.", true);
        seedCondicionMedica("74567012", "Alergia alimentaria", "Evitar frutos secos en meriendas.", true);
        seedCondicionMedica("80123678", "Intolerancia a la lactosa", "Ofrecer alternativa sin lactosa.", true);
        seedCondicionMedica("83456901", "Dermatitis atopica", "Avisar si aparecen irritaciones en piel.", true);
        seedCondicionMedica("76789234", "Control fonoaudiologico", "Seguimiento semanal externo.", false);
    }

    private void seedCondicionMedica(String ninioCedula, String condicion, String observaciones, Boolean esCronica) {
        Ninio ninio = ninioRepository.findByCedula(ninioCedula).orElse(null);
        if (ninio == null) {
            return;
        }

        boolean existe = condicionMedicaRepository.findByNinioId(ninio.getId()).stream()
                .anyMatch(c -> condicion.equalsIgnoreCase(c.getCondicion()));
        if (existe) {
            return;
        }

        condicionMedicaRepository.save(CondicionMedica.builder()
                .ninio(ninio)
                .condicion(condicion)
                .observaciones(observaciones)
                .esCronica(esCronica)
                .build());
    }

    private void seedDemoCalendarioLaboral() {

        log.info("Cargando calendario laboral de ejemplo...");

        Funcionario admin = funcionarioRepository.findByEmail(adminEmail)
                .orElseGet(() -> funcionarioRepository.findAll().stream().findFirst().orElse(null));

        seedDiaNoLaborable(LocalDate.now().plusDays(10), "Jornada institucional de planificacion", TipoDiaNoLaborableEnum.SUSPENSION, admin);
        seedDiaNoLaborable(LocalDate.now().plusMonths(1), "Feriado nacional", TipoDiaNoLaborableEnum.FERIADO, admin);
        seedDiaNoLaborable(LocalDate.now().plusMonths(2), "Receso de invierno del centro", TipoDiaNoLaborableEnum.VACACIONES, admin);
    }

    private void seedDiaNoLaborable(LocalDate fecha, String motivo, TipoDiaNoLaborableEnum tipo, Funcionario creadoPor) {
        if (diaNoLaborableRepository.existsByFechaAndActivoTrue(fecha)) {
            return;
        }

        diaNoLaborableRepository.save(DiaNoLaborable.builder()
                .fecha(fecha)
                .motivo(motivo)
                .tipo(tipo)
                .activo(true)
                .creadoPor(creadoPor)
                .build());
    }

    private void seedDemoInscripciones() {

        log.info("Cargando inscripciones de ejemplo...");

        for (Ninio ninio : ninioRepository.findAll()) {
            if (!ninio.getCedula().startsWith("7") && !ninio.getCedula().startsWith("8")) {
                continue;
            }

            boolean existe = !inscripcionRepository.findByNinioId(ninio.getId()).isEmpty();
            if (existe) {
                continue;
            }

            Inscripcion inscripcion = Inscripcion.builder()
                    .ninio(ninio)
                    .fechaInscripcion(LocalDate.now().minusMonths(2))
                    .fechaInicio(LocalDate.now().minusMonths(1))
                    .estadoInscripcion(EstadoInscripcionEnum.ACTIVA)
                    .observaciones("Inscripcion demo activa para pruebas del sistema")
                    .build();

            inscripcionRepository.save(inscripcion);
        }
    }

    private void seedDemoAgendas() {

        log.info("Cargando agendas de ejemplo...");

        seedAgenda(
                "marcela.rodriguez@centrocrecer.example.com",
                "Coordinacion semanal de equipo",
                "Coordinacion interna",
                "Planificacion educativa",
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Revision de agenda, actividades y situaciones prioritarias."
        );

        seedAgenda(
                "andrea.silva@centrocrecer.example.com",
                "Entrevista familiar",
                "Atencion a familias",
                "Evaluacion psicologica",
                LocalDate.now().plusDays(2),
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                "Encuentro con familia para seguimiento emocional."
        );

        seedAgenda(
                "lucia.fernandez@centrocrecer.example.com",
                "Planificacion grupo Exploradores",
                "Seguimiento pedagogico",
                "Planificacion educativa",
                LocalDate.now().plusDays(3),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                "Definicion de objetivos pedagogicos semanales."
        );

        seedAgenda(
                "florencia.suarez@centrocrecer.example.com",
                "Taller de expresion plastica",
                "Taller grupal",
                "Materiales de taller",
                LocalDate.now().plusDays(4),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                "Preparacion de materiales y actividad de collage."
        );
    }

    private void seedAgenda(String funcionarioEmail, String descripcion, String tipoNombre, String subtipoNombre,
                            LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String detalleDescripcion) {

        Funcionario funcionario = funcionarioRepository.findByEmail(funcionarioEmail).orElse(null);
        if (funcionario == null) {
            return;
        }

        boolean existe = agendaRepository.findByFechaAndFuncionarioId(fecha, funcionario.getId()).stream()
                .anyMatch(agenda -> descripcion.equalsIgnoreCase(agenda.getDescripcion()));
        if (existe) {
            return;
        }

        TipoAgenda tipo = seedTipoAgenda(tipoNombre);
        SubtipoAgenda subtipo = seedSubtipoAgenda(subtipoNombre);

        Agenda agenda = Agenda.builder()
                .funcionario(funcionario)
                .tipo(tipo)
                .descripcion(descripcion)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .activo(true)
                .build();

        agenda = agendaRepository.save(agenda);

        DetalleAgenda detalle = DetalleAgenda.builder()
                .agenda(agenda)
                .subtipoAgenda(subtipo)
                .descripcionEspecifica(detalleDescripcion)
                .requiereParticipantes(true)
                .activo(true)
                .build();

        detalleAgendaRepository.save(detalle);
    }

    private void seedDemoAgendaLimpieza() {

        log.info("Cargando agendas de limpieza de ejemplo...");

        seedAgendaLimpieza(
                "romina.alvarez@centrocrecer.example.com",
                "Limpieza de aulas",
                "Aulas planta baja",
                "Repaso general de mesas, pisos y materiales",
                1,
                LocalDate.now().plusDays(1),
                LocalTime.of(7, 0),
                LocalTime.of(8, 0),
                EstadoLimpiezaEnum.PENDIENTE
        );

        seedAgendaLimpieza(
                "romina.alvarez@centrocrecer.example.com",
                "Limpieza de cocina",
                "Cocina",
                "Limpieza profunda posterior a merienda",
                1,
                LocalDate.now().plusDays(2),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                EstadoLimpiezaEnum.EN_PROCESO
        );

        seedAgendaLimpieza(
                "romina.alvarez@centrocrecer.example.com",
                "Limpieza de banios",
                "Banios",
                "Control de insumos e higiene",
                1,
                LocalDate.now().minusDays(1),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                EstadoLimpiezaEnum.FINALIZADA
        );
    }

    private void seedAgendaLimpieza(String funcionarioEmail, String subtipoNombre, String zona,
                                    String descripcion, Integer frecuencia, LocalDate fecha,
                                    LocalTime horaInicio, LocalTime horaFin, EstadoLimpiezaEnum estado) {

        Funcionario funcionario = funcionarioRepository.findByEmail(funcionarioEmail).orElse(null);
        if (funcionario == null) {
            return;
        }

        boolean existe = agendaLimpiezaRepository.findByFuncionarioId(funcionario.getId()).stream()
                .anyMatch(agenda -> fecha.equals(agenda.getFecha()) && zona.equalsIgnoreCase(agenda.getZona()));
        if (existe) {
            return;
        }

        SubtipoAgenda subtipo = seedSubtipoAgenda(subtipoNombre);

        agendaLimpiezaRepository.save(AgendaLimpieza.builder()
                .funcionario(funcionario)
                .subtipoAgenda(subtipo)
                .zona(zona)
                .descripcion(descripcion)
                .frecuencia(frecuencia)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .estado(estado)
                .build());
    }

    private void seedDemoActividades() {

        log.info("Cargando actividades de ejemplo...");

        seedActividad(
                "Salida al parque Rivera",
                "Actividad recreativa al aire libre con juegos cooperativos.",
                LocalDate.now().plusDays(7),
                LocalDate.now().plusDays(7),
                LocalTime.of(9, 30),
                LocalTime.of(12, 0),
                "Parque Rivera",
                2,
                List.of("70123678", "71234789", "72345890", "73456901"),
                List.of("Transporte Alegre", "Cobertura Emergencia Movil")
        );

        seedActividad(
                "Taller de ceramica familiar",
                "Taller con participacion de referentes familiares.",
                LocalDate.now().plusDays(14),
                LocalDate.now().plusDays(14),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                "Salon de talleres",
                3,
                List.of("76789234", "77890345", "78901456", "79012567"),
                List.of("Insumos Creativos Sur")
        );

        seedActividad(
                "Jornada de integracion grupal",
                "Actividad de convivencia entre grupos del centro.",
                LocalDate.now().plusDays(21),
                LocalDate.now().plusDays(21),
                LocalTime.of(10, 0),
                LocalTime.of(15, 0),
                "Patio central",
                2,
                List.of("80123678", "81234789", "82345890", "83456901", "84567012"),
                List.of()
        );
    }

    private void seedActividad(String nombre, String descripcion, LocalDate fechaDesde, LocalDate fechaHasta,
                               LocalTime horaInicio, LocalTime horaSalida, String lugar,
                               Integer diasLimiteModificacion, List<String> niniosCedulas,
                               List<String> empresas) {

        boolean existe = actividadRepository.findAll().stream()
                .anyMatch(actividad -> nombre.equalsIgnoreCase(actividad.getNombre()));
        if (existe) {
            return;
        }

        List<Ninio> ninios = niniosCedulas.stream()
                .map(cedula -> ninioRepository.findByCedula(cedula).orElse(null))
                .filter(ninio -> ninio != null)
                .toList();

        Actividad actividad = Actividad.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .fechaDesde(fechaDesde)
                .fechaHasta(fechaHasta)
                .horaInicio(horaInicio)
                .horaSalida(horaSalida)
                .lugar(lugar)
                .diasLimiteModificacion(diasLimiteModificacion)
                .activo(true)
                .ninios(ninios)
                .build();

        actividad = actividadRepository.save(actividad);

        for (Ninio ninio : ninios) {
            if (!permisoRepository.existsByActividadIdAndNinioId(actividad.getId(), ninio.getId())) {
                permisoRepository.save(Permiso.builder()
                        .actividad(actividad)
                        .ninio(ninio)
                        .ninioCedula(ninio.getCedula())
                        .activo(true)
                        .autorizado(false)
                        .respondido(false)
                        .build());
            }
        }

        for (String empresaNombre : empresas) {
            if (!empresaExternaRepository.existsByNombreAndActividadId(empresaNombre, actividad.getId())) {
                empresaExternaRepository.save(EmpresaExterna.builder()
                        .actividad(actividad)
                        .nombre(empresaNombre)
                        .tipoServicio(empresaNombre.contains("Transporte") ? "Transporte" : "Servicio externo")
                        .telefono("+59824000000")
                        .build());
            }
        }
    }

    private void seedDemoReportes() {

        log.info("Cargando reportes de ejemplo...");

        seedReporte(
                "Seguimiento semanal - Exploradores",
                "Se observa buena participacion en rutinas y actividades grupales.",
                "marcela.rodriguez@centrocrecer.example.com",
                List.of("70123678", "71234789", "72345890"),
                List.of("Exploradores"),
                false
        );

        seedReporte(
                "Informe pedagogico individual",
                "Avances en lenguaje, autonomia y participacion en propuestas.",
                "lucia.fernandez@centrocrecer.example.com",
                List.of("76789234"),
                List.of(),
                true
        );

        seedReporte(
                "Registro de taller corporal",
                "Actividad orientada a coordinacion, juego simbolico y expresion.",
                "paula.ibarra@centrocrecer.example.com",
                List.of("80123678", "82345890", "84567012"),
                List.of("Integracion"),
                false
        );
    }

    private void seedReporte(String titulo, String descripcion, String funcionarioEmail,
                             List<String> niniosCedulas, List<String> gruposNombres, Boolean visto) {

        boolean existe = reporteRepository.findAll().stream()
                .anyMatch(reporte -> titulo.equalsIgnoreCase(reporte.getTitulo()));
        if (existe) {
            return;
        }

        Funcionario funcionario = funcionarioRepository.findByEmail(funcionarioEmail).orElse(null);
        if (funcionario == null) {
            return;
        }

        Reporte reporte = Reporte.builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .fechaGeneracion(new Date())
                .funcionario(funcionario)
                .activo(true)
                .visto(visto)
                .build();

        reporte = reporteRepository.save(reporte);
        final Reporte reporteGuardado = reporte;

        List<ReporteNinio> reporteNinios = new ArrayList<>();
        for (String cedula : niniosCedulas) {
            ninioRepository.findByCedula(cedula).ifPresent(ninio -> reporteNinios.add(ReporteNinio.builder()
                    .reporte(reporteGuardado)
                    .ninio(ninio)
                    .reporteTitulo(titulo)
                    .nombreNinio(ninio.getNombre() + " " + ninio.getApellido())
                    .build()));
        }

        List<ReporteGrupo> reporteGrupos = new ArrayList<>();
        for (String grupoNombre : gruposNombres) {
            grupoRepository.findByNombreIgnoreCase(grupoNombre).ifPresent(grupo -> reporteGrupos.add(ReporteGrupo.builder()
                    .reporte(reporteGuardado)
                    .grupo(grupo)
                    .grupoNombre(grupo.getNombre())
                    .reporteTitulo(titulo)
                    .build()));
        }

        List<DocumentoAdjunto> documentos = List.of(DocumentoAdjunto.builder()
                .reporte(reporteGuardado)
                .nombreArchivo(titulo.toLowerCase().replace(" ", "-") + ".pdf")
                .tipoArchivo("application/pdf")
                .url("/documentos/demo/" + titulo.toLowerCase().replace(" ", "-") + ".pdf")
                .fechaSubida(new Date())
                .build());

        reporte.setReporteNinios(reporteNinios);
        reporte.setReporteGrupos(reporteGrupos);
        reporte.setDocumentos(documentos);
        reporteRepository.save(reporte);
    }

    private void seedDemoNotificaciones() {

        log.info("Cargando notificaciones de ejemplo...");

        for (Reporte reporte : reporteRepository.findAll()) {
            String funcionarioEmail = emailFuncionarioPorTituloReporte(reporte.getTitulo());
            if (funcionarioEmail == null) {
                continue;
            }

            Funcionario funcionario = funcionarioRepository.findByEmail(funcionarioEmail).orElse(null);
            if (funcionario == null || notificacionRepository.existsByFuncionario_IdAndReporte_Id(funcionario.getId(), reporte.getId())) {
                continue;
            }

            notificacionRepository.save(Notificacion.builder()
                    .funcionario(funcionario)
                    .reporte(reporte)
                    .mensaje("Nuevo movimiento asociado al reporte: " + reporte.getTitulo())
                    .fechaCreacion(LocalDateTime.now().minusDays(1))
                    .leida(false)
                    .build());
        }
    }

    private String emailFuncionarioPorTituloReporte(String titulo) {
        return switch (titulo) {
            case "Seguimiento semanal - Exploradores" -> "marcela.rodriguez@centrocrecer.example.com";
            case "Informe pedagogico individual" -> "lucia.fernandez@centrocrecer.example.com";
            case "Registro de taller corporal" -> "paula.ibarra@centrocrecer.example.com";
            default -> null;
        };
    }

    private void seedDemoAsistencias() {

        log.info("Cargando asistencias de ejemplo...");

        List<LocalDate> fechas = ultimosDiasHabiles(5);

        seedAsistenciasFuncionarios(fechas);
        seedAsistenciasNinios(fechas);
    }

    private void seedAsistenciasFuncionarios(List<LocalDate> fechas) {

        List<String> funcionariosEmails = List.of(
                "marcela.rodriguez@centrocrecer.example.com",
                "andrea.silva@centrocrecer.example.com",
                "lucia.fernandez@centrocrecer.example.com",
                "diego.pereira@centrocrecer.example.com",
                "valentina.castro@centrocrecer.example.com",
                "sofia.mendez@centrocrecer.example.com",
                "camila.ramos@centrocrecer.example.com",
                "florencia.suarez@centrocrecer.example.com",
                "matias.nunez@centrocrecer.example.com",
                "paula.ibarra@centrocrecer.example.com",
                "romina.alvarez@centrocrecer.example.com",
                "gonzalo.sosa@centrocrecer.example.com",
                "noelia.molina@centrocrecer.example.com",
                "federico.cabrera@centrocrecer.example.com",
                "agustina.torres@centrocrecer.example.com"
        );

        int indice = 0;
        for (String email : funcionariosEmails) {
            Funcionario funcionario = funcionarioRepository.findByEmail(email).orElse(null);
            if (funcionario == null) {
                continue;
            }

            for (LocalDate fecha : fechas) {
                if (existeAsistenciaFuncionario(funcionario, fecha)) {
                    continue;
                }

                LocalTime entrada = LocalTime.of(8, 0).plusMinutes((indice % 4) * 5L);
                LocalTime salida = LocalTime.of(16, 0).plusMinutes((indice % 3) * 10L);

                Asistencia asistencia = Asistencia.builder()
                        .fecha(fecha)
                        .horaEntrada(entrada)
                        .horaSalida(salida)
                        .funcionario(funcionario)
                        .funcionarioNombre(funcionario.getNombre() + " " + funcionario.getApellido())
                        .funcionarioCedula(funcionario.getCedula())
                        .observaciones("Asistencia demo de funcionario")
                        .activo(true)
                        .build();

                asistenciaRepository.save(asistencia);
            }
            indice++;
        }
    }

    private void seedAsistenciasNinios(List<LocalDate> fechas) {

        List<String> niniosCedulas = List.of(
                "70123678",
                "71234789",
                "72345890",
                "73456901",
                "74567012",
                "75678123",
                "76789234",
                "77890345",
                "78901456",
                "79012567",
                "80123678",
                "81234789",
                "82345890",
                "83456901",
                "84567012",
                "90123678",
                "91234789",
                "92345890",
                "93456901",
                "94567012",
                "95678123",
                "96789234",
                "97890345",
                "98901456",
                "99012567",
                "10123678",
                "11234789",
                "12345890",
                "13456901",
                "14567012"
        );

        List<String> funcionariosEmails = List.of(
                "lucia.fernandez@centrocrecer.example.com",
                "diego.pereira@centrocrecer.example.com",
                "gonzalo.sosa@centrocrecer.example.com",
                "noelia.molina@centrocrecer.example.com",
                "marcela.rodriguez@centrocrecer.example.com",
                "sofia.mendez@centrocrecer.example.com",
                "andrea.silva@centrocrecer.example.com",
                "federico.cabrera@centrocrecer.example.com",
                "agustina.torres@centrocrecer.example.com",
                "matias.nunez@centrocrecer.example.com",
                "florencia.suarez@centrocrecer.example.com",
                "paula.ibarra@centrocrecer.example.com"
        );

        int indice = 0;
        for (String cedula : niniosCedulas) {
            Ninio ninio = ninioRepository.findByCedula(cedula).orElse(null);
            if (ninio == null) {
                continue;
            }

            Funcionario funcionario = funcionarioRepository
                    .findByEmail(funcionariosEmails.get(indice % funcionariosEmails.size()))
                    .orElse(null);

            for (LocalDate fecha : fechas) {
                if (asistenciaRepository.existsByNinio_IdAndFechaAndActivoTrue(ninio.getId(), fecha)) {
                    continue;
                }

                LocalTime entrada = LocalTime.of(8, 30).plusMinutes((indice % 3) * 5L);
                LocalTime salida = LocalTime.of(12, 30).plusMinutes((indice % 4) * 5L);

                Asistencia asistencia = Asistencia.builder()
                        .fecha(fecha)
                        .horaEntrada(entrada)
                        .horaSalida(salida)
                        .ninio(ninio)
                        .ninioCedula(ninio.getCedula())
                        .ninioNombre(ninio.getNombre() + " " + ninio.getApellido())
                        .funcionario(funcionario)
                        .funcionarioNombre(funcionario != null ? funcionario.getNombre() + " " + funcionario.getApellido() : null)
                        .funcionarioCedula(funcionario != null ? funcionario.getCedula() : null)
                        .observaciones("Asistencia demo de ninio")
                        .activo(true)
                        .build();

                asistenciaRepository.save(asistencia);
            }
            indice++;
        }
    }

    private boolean existeAsistenciaFuncionario(Funcionario funcionario, LocalDate fecha) {
        return asistenciaRepository.findByFuncionario_Id(funcionario.getId())
                .stream()
                .anyMatch(asistencia -> asistencia.getNinio() == null
                        && fecha.equals(asistencia.getFecha())
                        && Boolean.TRUE.equals(asistencia.getActivo()));
    }

    private List<LocalDate> ultimosDiasHabiles(int cantidad) {
        java.util.ArrayList<LocalDate> fechas = new java.util.ArrayList<>();
        LocalDate fecha = LocalDate.now();

        while (fechas.size() < cantidad) {
            if (fecha.getDayOfWeek().getValue() <= 5) {
                fechas.add(fecha);
            }
            fecha = fecha.minusDays(1);
        }

        return fechas;
    }
}