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

    public Pedido() {
        super();
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
        this.estado = Estado.PENDIENTE;
    }

    public Pedido(Usuario usuario) {
        super();
        this.usuario = usuario;
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
        this.estado = Estado.PENDIENTE;
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        DetallePedido nuevoDetalle = new DetallePedido(cantidad, precioUnitario, producto, this);
        this.detalles.add(nuevoDetalle);
        calcularTotal();
    }

    public void addDetallePedido(int cantidad, Producto producto) {
        addDetallePedido(cantidad, producto.getPrecio(), producto);
    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto() != null && detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }
        return null;
    }

    public boolean deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findeDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalles.remove(detalle);
            calcularTotal();
            return true;
        }
        return false;
    }

    @Override
    public Double calcularTotal() {
        this.total = 0.0;
        for (DetallePedido detalle : detalles) {
            this.total += detalle.getSubtotal();
        }
        return this.total;
    }

    public List<DetallePedido> getDetalles() { return detalles; }
    public Double getTotal() { return total; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setTotal(Double total) { this.total = total; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "Pedido{id=" + getId() + ", fecha=" + fecha + ", total=" + total +
                ", estado=" + estado + ", formaPago=" + formaPago +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "N/A") +
                ", detalles=" + detalles.size() + "}";
    }
}
