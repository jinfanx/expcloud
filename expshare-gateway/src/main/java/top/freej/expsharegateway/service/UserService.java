package top.freej.expsharegateway.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private ExpshareBusinessFeignClient expshareBusinessFeignClient;
    public JSONObject saveGithubUser(JSONObject githubUser){

        githubUser.put("username",githubUser.getString("login"));
        githubUser.remove("login");
        return expshareBusinessFeignClient.saveGithubUser(githubUser);
    }

}
