package kayakoAPI.mappers.universalMappers;

import org.json.JSONObject;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public interface Mapper<T> {

    T mapObject(JSONObject jsonObject, T object) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
}
