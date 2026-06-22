package integrador.prog2.services;

import integrador.prog2.DAO.UsuarioDAO;
import integrador.prog2.entities.Usuario;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.ValidacionNegocioException;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void validarUsuario(Usuario usuario) throws ValidacionNegocioException {
        System.out.println(" Validando datos del usuario: " + usuario.getNombre() + "...");

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error: El nombre del usuario es obligatorio.");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error: El apellido del usuario es obligatorio.");
        }
        if (usuario.getMail() == null || usuario.getMail().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error: El correo electrónico es obligatorio.");
        }
        if (!usuario.getMail().contains("@")) {
            throw new ValidacionNegocioException("🔴 Error: El correo '" + usuario.getMail() + "' no es válido.");
        }
        System.out.println(" El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' es válido.");
    }

    public void guardarUsuario(Usuario usuario) throws ValidacionNegocioException {
        validarUsuario(usuario);
        if (usuarioDAO.existeMail(usuario.getMail())) {
            throw new ValidacionNegocioException("🔴 Error: El correo '" + usuario.getMail() + "' ya se encuentra registrado en el sistema.");
        }
        System.out.println(" [UsuarioService] Guardando usuario en la base de datos...");
        usuarioDAO.crear(usuario);
        System.out.println(" [UsuarioService] Usuario guardado exitosamente con ID: " + usuario.getId());
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listar() {
        return usuarioDAO.listar();
    }

    public void actualizar(Usuario usuario) throws ValidacionNegocioException, EntidadNoEncontradaException {
        Optional<Usuario> existente = usuarioDAO.buscarPorId(usuario.getId());
        if (existente.isEmpty()) {
            throw new EntidadNoEncontradaException("🔴 Error: El usuario con ID " + usuario.getId() + " no existe.");
        }
        validarUsuario(usuario);
        if (usuarioDAO.existeMail(usuario.getMail()) &&
                !existente.get().getMail().equals(usuario.getMail())) {
            throw new ValidacionNegocioException("🔴 Error: El correo '" + usuario.getMail() + "' ya se encuentra registrado en el sistema.");
        }
        usuarioDAO.actualizar(usuario);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Optional<Usuario> existente = usuarioDAO.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new EntidadNoEncontradaException("🔴 Error: El usuario con ID " + id + " no existe.");
        }
        usuarioDAO.eliminar(id);
    }
}
