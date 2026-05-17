package tip.java.sistemacentrocrecer.configurations.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.email}")
    private String adminEmail;

    @Value("${app.default-admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        log.info("Iniciando carga de datos iniciales...");

        seedRoles();
        seedAdminUser();

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
}