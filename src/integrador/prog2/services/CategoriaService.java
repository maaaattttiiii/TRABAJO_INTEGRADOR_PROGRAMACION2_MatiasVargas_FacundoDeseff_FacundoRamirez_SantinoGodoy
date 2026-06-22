package integrador.prog2.services;

import integrador.prog2.DAO.CategoriaDAO;
import integrador.prog2.DAO.ProductoDAO;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.Producto;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.ValidacionNegocioException;

import java.util.List;
import java.util.Optional;

public class CategoriaService {

    private CategoriaDAO categoriaDAO;
    private ProductoDAO productoDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
        this.productoDAO = new ProductoDAO();
    }

    public void validarCategoria(Categoria categoria) throws ValidacionNegocioException {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new ValidacionNegocioException("Error: El nombre de la categoria es obligatorio.");
        }
        if (categoria.getDescripcion() == null || categoria.getDescripcion().trim().isEmpty()) {
            throw new ValidacionNegocioException("Error: La descripcion de la categoria es obligatoria.");
        }
    }

    public void guardarCategoria(Categoria categoria) throws ValidacionNegocioException {
        validarCategoria(categoria);
        categoriaDAO.crear(categoria);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaDAO.buscarPorId(id);
    }

    public List<Categoria> listar() {
        return categoriaDAO.listar();
    }

    public void actualizar(Categoria categoria) throws ValidacionNegocioException, EntidadNoEncontradaException {
        Optional<Categoria> existente = categoriaDAO.buscarPorId(categoria.getId());
        if (existente.isEmpty()) {
            throw new EntidadNoEncontradaException("Error: La categoria con ID " + categoria.getId() + " no existe.");
        }
        validarCategoria(categoria);
        categoriaDAO.actualizar(categoria);
    }

    public void eliminar(Long id) throws ValidacionNegocioException, EntidadNoEncontradaException {
        Optional<Categoria> cat = categoriaDAO.buscarPorId(id);
        if (cat.isEmpty()) {
            throw new EntidadNoEncontradaException("Error: La categoria con ID " + id + " no existe.");
        }
        List<Producto> productos = productoDAO.listarPorCategoria(id);
        if (!productos.isEmpty()) {
            throw new ValidacionNegocioException("Error: No se puede eliminar la categoria porque tiene " +
                    productos.size() + " producto(s) asociado(s).");
        }
        categoriaDAO.eliminar(id);
    }
}
