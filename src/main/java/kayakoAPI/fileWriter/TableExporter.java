package kayakoAPI.fileWriter;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface TableExporter {
    <T> void writeObjects(List<T> objects, Class objectClass) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException;
}
