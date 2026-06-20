package integrador.prog2.entities;

public class DetallePedido extends Base {

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    // Relaciones
    private Producto producto;
    private Pedido pedido;

    // Constructor vacío
    public DetallePedido() {
    }

    // Constructor lleno
    public DetallePedido(Integer cantidad, Double precioUnitario, Producto producto, Pedido pedido) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
        this.pedido = pedido;
        this.subtotal = cantidad * precioUnitario; // Se calcula automáticamente al crearlo
    }

    // Getters y Setters
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal(); // Si cambia la cantidad, se actualiza el subtotal
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal(); // Si cambia el precio, se actualiza el subtotal
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    // Método auxiliar para mantener el subtotal al día
    private void calcularSubtotal() {
        if (this.cantidad != null && this.precioUnitario != null) {
            this.subtotal = this.cantidad * this.precioUnitario;
        }
    }
}