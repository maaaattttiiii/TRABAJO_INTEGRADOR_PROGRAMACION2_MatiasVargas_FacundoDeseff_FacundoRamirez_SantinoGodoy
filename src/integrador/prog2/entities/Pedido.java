package integrador.prog2.entities;

import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private LocalDateTime fecha;
    private Double total;
    private Estado estado;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;

    // Constructor por defecto vacío
    public Pedido() {
        super();
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
        this.estado = Estado.PENDIENTE;
    }

    // Constructor con parámetros (el que pide el main)
    public Pedido(Usuario usuario) {
        super();
        this.usuario = usuario;
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
        this.estado = Estado.PENDIENTE;
    }

    // Método para agregar detalles directamente con cantidad y producto
    public void addDetallePedido(int cantidad, Producto producto) {
        DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto.getPrecio(), producto, this);
        this.detalles.add(nuevoDetalle);
        calcularTotal();
    }

    @Override
    public Double calcularTotal() {
        this.total = 0.0;
        for (DetallePedido detalle : detalles) {
            this.total += detalle.getSubtotal();
        }
        return this.total;
    }

    // Getters y Setters necesarios
    public List<DetallePedido> getDetalles() { return detalles; }
    public Double getTotal() { return total; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Pedido{" +
                "fecha=" + fecha +
                ", total=" + total +
                ", estado=" + estado +
                ", formaPago=" + formaPago +
                ", usuario=" + usuario +
                ", detalles=" + detalles +
                '}';
    }
}