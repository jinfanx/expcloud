//package top.freej.expsharegateway.controller;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import top.freej.expsharegateway.oauth.GithubGatewayFilter;
//
//import javax.annotation.Resource;
//
//@RestController
//public class MainController {
//
//    @Resource
//    GithubGatewayFilter githubGatewayFilter;
//
//    @GetMapping("/github/login")
//    public Mono<Void> githubLogin(ServerWebExchange exchange, GatewayFilterChain chain){
//        return githubGatewayFilter.filter(exchange,chain);
//    }
//}
