package top.freej.expsharegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisWebSession
@EnableFeignClients
public class ExpshareGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpshareGatewayApplication.class, args);
    }
}

