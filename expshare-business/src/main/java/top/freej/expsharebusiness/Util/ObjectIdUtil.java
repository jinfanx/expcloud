package top.freej.expsharebusiness.Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.bson.types.ObjectId;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 转换ObjectId为字符串
 * 适用于使用Object作为返回类型查询出来的MongoDB数据
 * 和fastjson的JsonObject
 *
 * 其他返回类型为测试，可能不是LinkedHashMap
 *
 */
public class ObjectIdUtil {

    /**
     * ObjectID转json时，时间错、机器码、进程号等会被写入json
     * 此方法讲对象中的ObjectID转换为字符串
     * @param objects
     * @return
     */
    public static List<Object> convertObjectId(List<Object> objects){
        for (Object o:objects){
            convertObjectId(o);
        }

        return objects;
    }

    /**
     * 单个对象中objectID转换
     * @param object
     * @return
     */
    public static Object convertObjectId(Object object){

        if (object instanceof LinkedHashMap){
            LinkedHashMap obj = (LinkedHashMap) object;
            for (Object key:obj.keySet()){
                Object value = obj.get(key);
                if(value instanceof ObjectId){
                    obj.put(key,value.toString());
                }

            }
            return object;
        }

        if(object instanceof JSONObject){

            ValueFilter filter = (obj, s, v) -> {
                if (v instanceof  ObjectId)
                    return v.toString();
                return v;
            };
            JSONObject jsonObject = (JSONObject) object;

            return JSONObject.parseObject(JSONObject.toJSONString(jsonObject,filter));
        }


        return object;
    }

    /**
     * 普通对象转jsonObject,添加ObjectID
     * @param object
     * @return
     */
    public static JSONObject addObjectId2JsonObject(Object object){
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);

        for(String key:jsonObject.keySet()){
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject){
                JSONObject valueJsonObject = (JSONObject) value;
                valueJsonObject.put("id",new ObjectId());
                addObjectId2JsonObject(valueJsonObject);
            }else if (value instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) value;
                for (Object obj:jsonArray){
                    addObjectId2JsonObject(obj);
                }
            }
        }

        return jsonObject;
    }

}
