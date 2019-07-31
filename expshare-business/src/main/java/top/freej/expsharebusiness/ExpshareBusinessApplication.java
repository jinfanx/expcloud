package top.freej.expsharebusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ExpshareBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpshareBusinessApplication.class, args);
    }

}
