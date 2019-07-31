package top.freej.expsharebusiness.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import top.freej.expsharebusiness.Util.ObjectIdUtil;
import top.freej.expsharebusiness.dao.CommonDao;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * 通用controller
 * 包含通用的增删改查
 *
 */
@RestController
public class CommonController {

    @Resource
    private CommonDao commonDao;

    /**
     * 列表查询
     * @param collection 集合名称
     * @return
     */
    @RequestMapping(value = "/api/{collection}",method = RequestMethod.GET)
    public List<Object> listUsers(@PathVariable String collection){
        return commonDao.listObjectByName(collection,0,Integer.MAX_VALUE);
    }

    /**
     * 新增单条记录
     * @param object 新增数据
     * @param collection 集合
     * @return
     */
    @RequestMapping(value = "/api/{collection}",method = RequestMethod.PUT)
    public Object addUser(@RequestBody JSONObject object,@PathVariable String collection){
        Object obj = commonDao.addObject(object,collection);
        return ObjectIdUtil.convertObjectId(obj);
    }

    /**
     * 根据ID获取对象
     * @param id _id
     * @param collection 集合名称
     * @return
     */
    @RequestMapping(value = "/api/{collection}/{id}",method = RequestMethod.GET)
    public Object getUserById(@PathVariable String id, @PathVariable String collection){
        Object obj =  commonDao.getObjectById(collection,id);
        return ObjectIdUtil.convertObjectId(obj);
    }

    /**
     * 更新对象
     * @param object
     * @param collection
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/{collection}/{id}",method = RequestMethod.PUT)
    public Object updateObject(@RequestBody JSONObject object,@PathVariable String collection,@PathVariable String id){
        Object obj = commonDao.replaceObject(object,collection,id);
        return ObjectIdUtil.convertObjectId(obj);
    }

    /**
     * 删除对象
     * @param collection
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/{collection}/{id}",method = RequestMethod.DELETE)
    public Object deleteObject(@PathVariable String collection,@PathVariable String id){
        return commonDao.deleteObject(collection,id);
    }

}
