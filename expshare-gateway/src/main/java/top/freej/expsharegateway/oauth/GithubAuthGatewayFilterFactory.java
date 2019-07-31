package top.freej.expsharegateway.oauth;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GithubAuthGatewayFilterFactory extends AbstractGatewayFilterFactory {

    @Resource
    private GithubGatewayFilter githubGatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return githubGatewayFilter;
    }
}
