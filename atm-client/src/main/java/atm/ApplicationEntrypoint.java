package atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableFeignClients(basePackages = "api")
public class ApplicationEntrypoint {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntrypoint.class, args);
    }

}
