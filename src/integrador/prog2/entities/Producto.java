package integrador.prog2.entities;

public class Producto extends Base {
    private String nombre;
    private Double precio;
    private Categoria categoria;

    // Constructor vacío (obligatorio)
    public Producto() {
        super();
    }

    // Constructor con parámetros para crear productos rápido
    public Producto(String nombre, Double precio, Categoria categoria) {
        super();
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}