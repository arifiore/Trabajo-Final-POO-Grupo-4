
package dao;

import java.util.List;

public interface IBaseDAO<T>  {
    boolean insertar(T t) throws Exception;
    boolean actualizar(T t) throws Exception;
    boolean eliminar(int id) throws Exception;
    T obtenerPorId(int id) throws Exception;
    List<T> obtenerTodos() throws Exception;
}
