package integrador.prog2.controllers;

import integrador.prog2.entities.Categoria;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.CategoriaService;

import java.util.List;
import java.util.Optional;

public class CategoriaController {

    private CategoriaService categoriaService;

    public CategoriaController() {
        this.categoriaService = new CategoriaService();
    }

    public void registrarCategoria(Categoria categoria) {
        try {
            categoriaService.guardarCategoria(categoria);
            System.out.println("[CategoriaController] Categoria registrada con exito. ID: " + categoria.getId());
        } catch (ValidacionNegocioException e) {
            System.out.println("[CategoriaController] Error al validar categoria:");
            System.out.println("   -> " + e.getMessage());
        }
    }

    public List<Categoria> listarCategorias() {
        return categoriaService.listar();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaService.buscarPorId(id);
    }

    public void actualizarCategoria(Categoria categoria) {
        try {
            categoriaService.actualizar(categoria);
            System.out.println("[CategoriaController] Categoria actualizada con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("[CategoriaController] Error al actualizar categoria:");
            System.out.println("   -> " + e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[CategoriaController] " + e.getMessage());
        }
    }

    public void eliminarCategoria(Long id) {
        try {
            categoriaService.eliminar(id);
            System.out.println("[CategoriaController] Categoria eliminada con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("[CategoriaController] Error al eliminar categoria:");
            System.out.println("   -> " + e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[CategoriaController] " + e.getMessage());
        }
    }
}
