
import java.util.*;

/**
 * 
 */
public class MapperRegistry {

    /**
     * Default constructor
     */
    public MapperRegistry() {
    }

    /**
     * 
     */
    private Map<Class<?>, MapperProxyFactory<?>>> mappers;

    /**
     * @param type 
     * @param sqlSession 
     * @return
     */
    public T getMapper(Class<T> type, SqlSession sqlSession) {
        // TODO implement here
        return null;
    }

    /**
     * @param type
     */
    public void addMapper(Class<T> type) {
        // TODO implement here
    }

    /**
     * @param packageName
     */
    public void addMappers(String packageName) {
        // TODO implement here
    }

}