package by.yankavets.mapper;

public interface Mapper<F, T> {

    T mapToDto(F entity);

    default F mapToEntity(T dto) {
        return null;
    }
}
