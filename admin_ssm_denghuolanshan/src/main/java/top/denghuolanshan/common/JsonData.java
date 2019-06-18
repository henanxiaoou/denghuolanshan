package top.denghuolanshan.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JsonData
 * @Description url请求结果以json方式返回
 * @Author 小欧
 * @Date 2019/6/1 14:50
 * @Version 1.0
 **/
@Data
public class JsonData {
    /**
     * 返回结果
     */
    private boolean ret;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 返回具体数据
     */
    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }
    public static JsonData success(Object object,String msg){
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }
    public static JsonData success(Object object){
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        return jsonData;
    }
    public static JsonData success(){
        return new JsonData(true);
    }
    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("ret",ret);
        result.put("msg",msg);
        result.put("data",data);
        return result;
    }
}
