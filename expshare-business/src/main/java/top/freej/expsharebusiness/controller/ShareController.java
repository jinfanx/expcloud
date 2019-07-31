package top.freej.expsharebusiness.controller;

import org.springframework.web.bind.annotation.*;
import top.freej.expsharebusiness.service.ShareService;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ShareController {

    @Resource
    private ShareService shareService;

    @RequestMapping(value = "/api/share/{category}/{categoryType}",method = RequestMethod.GET)
    public List<Object> listSharesByCategory(@PathVariable String category,@PathVariable int categoryType) throws Exception{

        return shareService.listSharesByCategory(category,categoryType);
    }

    @RequestMapping(value = "/api/share/search")
    public List<Object> searchSharesByKeywords(String keywords){
        return shareService.keywordsSearch(keywords);
    }

}
