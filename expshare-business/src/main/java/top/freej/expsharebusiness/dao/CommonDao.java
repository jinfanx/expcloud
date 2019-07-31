package top.freej.expsharebusiness.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import top.freej.expsharebusiness.Util.ObjectIdUtil;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CommonDao {

    @Resource
    private MongoTemplate mongoTemplate;


    /**
     * 根据集合名称查询集合元素
     *
     * @param collection 集合名称
     * @param skip       查询起始位置
     * @param limit      查询条目数
     * @return
     */
    public List<Object> listObjectByName(String collection, long skip, int limit) {

        if (skip <= 0) {
            skip = 0;
        }

        if (limit <= 0) {
            limit = Integer.MAX_VALUE;
        }

        Query query = new Query();
        query.skip(skip);
        query.limit(limit);

        List<Object> result = mongoTemplate.find(query, Object.class, collection);
        return ObjectIdUtil.convertObjectId(result);

    }


    /**
     * 添加对象到指定集合
     *
     * @param object     待插入对象
     * @param collection 待插入集合
     * @return
     */
    public Object addObject(Object object, String collection) {

        return mongoTemplate.insert(object, collection);
    }

    /**
     * 根据id查询对象
     *
     * @param collection 集合名称
     * @param id         对象_id
     * @return
     */
    public Object getObjectById(String collection, String id) {

        Query query = new Query(
                Criteria.where("_id").is(new ObjectId(id))
        );

        return mongoTemplate.findOne(query, Object.class, collection);

    }

    /**
     * 根据id替换，用来更新对象
     * @param object
     * @param collection
     * @return
     */
    public Object replaceObject(Object object,String collection,String id){
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findAndReplace(query,object,collection);
    }

    /**
     * 删除对象
     * @param collection
     * @param id
     * @return
     */
    public Object deleteObject(String collection,String id){
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.remove(query,collection);
    }

    /**
     * 条件查询集合
     * @param collection
     * @param query
     * @return
     */
    public List<Object> queryObjectList(String collection,Query query){
        return ObjectIdUtil.convertObjectId(mongoTemplate.find(query,Object.class,collection));
    }

    /**
     * 查询单个对象
     * @param collection
     * @param query
     * @return
     */
    public Object queryOneObject(String collection,Query query){
        return ObjectIdUtil.convertObjectId(mongoTemplate.findOne(query,Object.class,collection));
    }

}
