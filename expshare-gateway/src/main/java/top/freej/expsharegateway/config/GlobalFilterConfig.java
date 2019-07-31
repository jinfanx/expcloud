package top.freej.expsharegateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import top.freej.expsharegateway.oauth.GithubGatewayFilter;

import javax.annotation.Resource;

@Configuration
public class GlobalFilterConfig {

    @Resource
    private GithubGatewayFilter githubGatewayFilter;

    @Order(-300)
    @Bean
    public GlobalFilter methodFilter(){
        return ((exchange, chain) -> {
           HttpMethod method = exchange.getRequest().getMethod();
           ServerHttpResponse response = exchange.getResponse();

//           if (method == HttpMethod.OPTIONS){
//               response.setStatusCode(HttpStatus.OK);
//               response.getHeaders().add("Access-Control-Allow-Credentials","true");
//               return chain.filter(exchange);
//           }

           if (method == HttpMethod.PUT || method == HttpMethod.DELETE || method == HttpMethod.POST){
               // todo 判断用户是否已登录，
//
//               response.setStatusCode(HttpStatus.OK);
//               return exchange.getResponse().writeWith(Mono.just(response.bufferFactory().wrap("需要登录".getBytes())));
           }
           return chain.filter(exchange);
        });
    }

//    @Bean
//    @Order(-1)
//    public GlobalFilter githubLoginFilter(){
//        return (exchange, chain) -> githubGatewayFilter.filter(exchange,chain);
//    }

//    @Order(-200)
//    @Bean
//    public GlobalFilter addAllowCredentialHeader(){
//        return ((exchange, chain) -> {
//            exchange.getResponse().getHeaders().add("Access-Control-Allow-Credentials","true");
//            return chain.filter(exchange);
//        });
//    }
}
