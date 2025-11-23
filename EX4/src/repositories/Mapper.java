package EX4.src.repositories;

public interface Mapper<T, V> {
    V map(T object);
}
