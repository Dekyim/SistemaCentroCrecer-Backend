package tip.java.sistemacentrocrecer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SistemaCentroCrecerApplication {

    public static void main(String[] args) {

        SpringApplication.run(SistemaCentroCrecerApplication.class, args);
    }

}
