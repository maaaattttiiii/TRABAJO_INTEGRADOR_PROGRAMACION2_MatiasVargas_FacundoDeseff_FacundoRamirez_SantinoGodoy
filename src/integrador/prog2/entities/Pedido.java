package integrador.prog2.entities;

import integrador.prog2.enums.Estado; // Importa "Estado" a secas
import java.time.LocalDateTime;

public class Pedido extends Base {
    private LocalDateTime fechaPedido;
    private Double total;
    private Estado estado; // Tipo de dato "Estado"
    private Usuario usuario;

    // Constructor vacío
    public Pedido() {
        super();
        this.fechaPedido = LocalDateTime.now();
    }


    // Getters y Setters
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}