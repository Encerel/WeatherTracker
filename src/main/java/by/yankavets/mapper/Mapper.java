package by.yankavets.mapper;

import by.yankavets.exception.mapper.UnsupportedMapMethodException;

import java.lang.reflect.Method;
import java.util.List;

public interface Mapper<F, T> {

    default T mapToDto(F entity) throws NoSuchMethodException {
        Class<?> clazz = Mapper.class;
        Method method = clazz.getMethod("mapToDto", Object.class);
        throw new UnsupportedMapMethodException(method.getName());
    }

    default F mapToEntity(T dto) throws NoSuchMethodException {
        Class<?> clazz = Mapper.class;
        Method method = clazz.getMethod("mapToEntity", Object.class);
        throw new UnsupportedMapMethodException(method.getName());
    }

    default List<T> mapToListDto(List<F> entitiesList) throws NoSuchMethodException {
        Class<?> clazz = Mapper.class;
        Method method = clazz.getMethod("mapToListDto", List.class);
        throw new UnsupportedMapMethodException(method.getName());
    }
}
