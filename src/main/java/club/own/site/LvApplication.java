package club.own.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"club.own.site"})
@EnableAsync
public class LvApplication {
    public static void main(String[] args) {
        SpringApplication.run(LvApplication.class, args);
    }
}
