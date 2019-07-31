package top.freej.expsharegateway.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("expshare-business")
@Service
public interface ExpshareBusinessFeignClient {

    /**
     * 保存github用户
     * @param githubUser
     * @return
     */
    @RequestMapping(value = "/api/user",method = RequestMethod.PUT)
    JSONObject saveGithubUser(JSONObject githubUser);

}
