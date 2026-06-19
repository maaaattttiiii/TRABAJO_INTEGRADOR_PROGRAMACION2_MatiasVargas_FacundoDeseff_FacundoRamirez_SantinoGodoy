package integrador.prog2.entities;

import integrador.prog2.enums.Estado;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable { // <--- Acá implementamos la interfaz
    private LocalDateTime fechaPedido;
    private Double total;
    private Estado estado;
    private Usuario usuario;

    // Lista para guardar todos los renglones de este pedido
    private List<DetallePedido> detalles;

    // Constructor vacío
    public Pedido() {
        super();
        this.fechaPedido = LocalDateTime.now();
        this.detalles = new ArrayList<>(); // Inicializamos la lista vacía
        this.total = 0.0;
    }

    // Método que nos pide la interfaz Calculable de forma obligatoria
    @Override
    public Double calcularTotal() {
        Double suma = 0.0;
        if (detalles != null) {
            for (DetallePedido detalle : detalles) {
                if (detalle.getSubtotal() != null) {
                    suma += detalle.getSubtotal();
                }
            }
        }
        this.total = suma; // Guardamos el resultado en nuestro atributo
        return this.total;
    }

    // Método auxiliar para agregar renglones al pedido fácilmente
    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
        detalle.setPedido(this); // Vinculamos el detalle con este pedido
        calcularTotal(); // Cada vez que agregamos un producto, recalculamos el total automáticamente
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

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}