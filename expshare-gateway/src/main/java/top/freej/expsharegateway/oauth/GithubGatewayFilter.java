package top.freej.expsharegateway.oauth;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.session.web.server.session.SpringSessionWebSessionStore;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.WebSessionStore;
import reactor.core.publisher.Mono;
import top.freej.expsharegateway.service.UserService;

import javax.annotation.Resource;
import java.time.Duration;

@Component
public class GithubGatewayFilter implements GatewayFilter, Ordered {

    @Resource
    private GithubOauthConfig githubOauthConfig;

    @Resource
    private UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        WebSession session = exchange.getSession().block();

        // todo 判断是否已登录
        Object userObject = session.getAttributes().get("user");
        if (userObject!=null){
            response.setStatusCode(HttpStatus.OK);

            return response.writeWith(Mono.just(response.bufferFactory().wrap(((JSONObject)userObject).toJSONString().getBytes())));
//            return chain.filter(exchange);
        }

        // todo 获取请求参数
        String code = request.getQueryParams().getFirst("code");
        if (StringUtils.isEmpty(code)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return chain.filter(exchange);
        }

        // TODO 调用github，获取token
        JSONObject tokenJson = getAccessToken(code);
        String token = tokenJson.getString("access_token");
        if (StringUtils.isEmpty(token)){
            System.err.println("获取令牌失败");
            return chain.filter(exchange);
        }

        // TODO 获取并保存用户信息
        JSONObject user = getUserInfo(token);
        JSONObject userSaved = userService.saveGithubUser(user);

        // todo 设置cookie
        session.getAttributes().put("user",userSaved);

        System.out.println(session.getClass().getSimpleName());

        session.setMaxIdleTime(Duration.ofDays(30));
        session.save();
        response.setStatusCode(HttpStatus.OK);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(userSaved.toJSONString().getBytes())));
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 获取令牌
     * @param code
     * @return
     */
    public JSONObject getAccessToken(String code) {
        JSONObject json = new RestTemplate().getForObject(githubOauthConfig.getClient().getAccessTokenUri() +
                        "?client_id=" + githubOauthConfig.getClient().getClientId() +
                        "&client_secret=" + githubOauthConfig.getClient().getClientSecret() +
                        "&code=" + code +
                        "&redirect_uri" + githubOauthConfig.getClient().getRedirectUri(),
                JSONObject.class);
        return json;
    }

    /**
     * 获取github用户
     * @param accessToken
     * @return
     */
    public JSONObject getUserInfo(String accessToken){
        JSONObject user = new RestTemplate().getForObject(githubOauthConfig.getResource().getUserInfoUri()+accessToken,JSONObject.class);
        return user;
    }

    @Component
    @ConfigurationProperties("github")
    public static class GithubOauthConfig {

        private Client client;

        private Resource resource;

        public Client getClient() {
            return client;
        }

        public void setClient(Client client) {
            this.client = client;
        }

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public static class Client {
            private String clientId;
            private String clientSecret;
            private String accessTokenUri;
            private String userAuthorizationUri;
            private String redirectUri;

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }

            public String getAccessTokenUri() {
                return accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getUserAuthorizationUri() {
                return userAuthorizationUri;
            }

            public void setUserAuthorizationUri(String userAuthorizationUri) {
                this.userAuthorizationUri = userAuthorizationUri;
            }

            public String getRedirectUri() {
                return redirectUri;
            }

            public void setRedirectUri(String redirectUri) {
                this.redirectUri = redirectUri;
            }
        }

        public static class Resource {

            private String userInfoUri;

            public String getUserInfoUri() {
                return userInfoUri;
            }

            public void setUserInfoUri(String userInfoUri) {
                this.userInfoUri = userInfoUri;
            }

        }
    }
}