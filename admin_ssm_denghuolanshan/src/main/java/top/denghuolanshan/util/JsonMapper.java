package top.denghuolanshan.util;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * @ClassName JsonMapper
 * @Description JSON与对象转换工具类
 * @Author 小欧
 * @Date 2019/6/1 21:11
 * @Version 1.0
 **/
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();
     static {
         objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
         objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
         objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
         objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
     }

    /**
     * 对象转字符串
     * @param src
     * @param <T>
     * @return
     */
     public static <T> String obj2String(T src){
        if (src == null){
            return null;
        }
        try {
            return src instanceof String ?(src.toString()) : objectMapper.writeValueAsString(src);
        } catch (JsonGenerationException e) {
            log.warn("parse object to string JsonGenerationException",e);
            return null;
        } catch (JsonMappingException e) {
            log.warn("parse object to string JsonMappingException",e);
            return null;
        } catch (IOException e) {
            log.warn("parse object to string IOException",e);
            return null;
        }catch (Exception e){
            log.warn("parse object to string exception",e);
            return null;
        }
     }

    public static <T> T string2obj(String src, TypeReference<T> reference){
         if (src == null || reference == null){
             return null;
         }
         try {
             return (T) (reference.getType().equals(String.class) ? src : objectMapper.readValue(src,reference));
         } catch (JsonParseException e) {
             log.warn("String to object JsonParseException",src,reference.getType(),e);
             return null;
         } catch (JsonMappingException e) {
             log.warn("String to object JsonMappingException",src,reference.getType(),e);
             return null;
         } catch (IOException e) {
             log.warn("String to object IOException",src,reference.getType(),e);
             return null;
         }catch (Exception e){
             log.warn("String to object Exception",src,reference.getType(),e);
             return null;
         }
    }
}
