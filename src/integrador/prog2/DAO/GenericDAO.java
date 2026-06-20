package integrador.prog2.DAO;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    void crear(T entidad);
    Optional<T> buscarPorId(Long id);
    List<T> listar();
    void actualizar(T entidad);
    void eliminar(Long id);
}
