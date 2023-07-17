package dataBase;

import java.sql.*;
import java.util.List;

public interface DataAccessObject<T> {
    default T findByKeyId(String keyId) {
        return null;
    }

    default List<T> selectAll() {
        return null;
    }

    default T update(T dto) {
        return null;
    }

    default T create(T dto) {
        return null;
    }

    default void delete(String keyId) {
    }
}