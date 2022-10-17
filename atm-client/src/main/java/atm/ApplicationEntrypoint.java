package atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApplicationEntrypoint {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntrypoint.class, args);
    }
}
