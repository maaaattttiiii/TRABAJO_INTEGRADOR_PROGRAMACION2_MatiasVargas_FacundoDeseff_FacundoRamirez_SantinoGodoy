package integrador.prog2.entities;

public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    private Double calcularSubtotal(){
        return this.cantidad * this.producto.getPrecio();
    }

    public int getCantidad(){
        return cantidad;
    }

    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public Producto getProducto(){
        return producto;
    }

    @Override
    public String toString() {
        return "Producto: " + producto.getNombre() +
                " | Cantidad: " + cantidad +
                " | Subtotal: $" + subtotal;
    }
}
