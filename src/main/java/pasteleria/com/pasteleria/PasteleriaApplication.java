package pasteleria.com.pasteleria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "pasteleria.com.pasteleria.repository")
@EntityScan(basePackages = "pasteleria.com.pasteleria.model")
public class PasteleriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasteleriaApplication.class, args);
    }
}
