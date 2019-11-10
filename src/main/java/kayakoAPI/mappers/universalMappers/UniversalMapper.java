package kayakoAPI.mappers.universalMappers;

import common.annotaions.CustomField;
import common.annotaions.JsonField;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointerException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class UniversalMapper<T> implements Mapper<T> {

    public T mapObject(JSONObject json, T object) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();

        JsonField jsonField;
        CustomField customField;

        for(Field field : fields){
            if((jsonField = field.getAnnotation(JsonField.class)) != null){
                if((customField = field.getAnnotation(CustomField.class)) != null){
                    JSONArray array = (JSONArray) json.query(jsonField.path());
                    String value = fetch(array,customField.contains());
                    write(field,object,value);
                }else {
                    Object value = null;
                    try {
                        value = json.query(jsonField.path());
                    }catch (JSONPointerException e){
                    }
                    if(value != JSONObject.NULL){
                        write(field,object,field.getType().cast(value));
                    }
                }
            }
        }
        return object;
    }

    private void write(Field field, Object object, Object ... args) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        new PropertyDescriptor(field.getName(),object.getClass())
                .getWriteMethod()
                .invoke(object,args);
    }

    private String fetch(JSONArray jsonArray, String contains){
        String result = "(NULL)";

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject field = jsonArray.getJSONObject(i);
            result = field.getString("value");
            if(result.contains(contains)){
                return result;
            }
        }
        return result;
    }

}
