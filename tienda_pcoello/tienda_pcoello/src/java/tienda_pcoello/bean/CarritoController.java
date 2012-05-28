package tienda_pcoello.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import tienda_pcoello.entity.Cliente;
import tienda_pcoello.entity.Producto;

@ManagedBean(name = "carritoController")
@SessionScoped

public class CarritoController implements Serializable {

    private Integer cantidad=0;
    private Producto producto;
    private List<CarritoController> listaCarrito = new ArrayList();
    private CarritoController carrito;
    private List<Integer> listaCantidad = new ArrayList();
    private Cliente cliente;
    private String user;
    private String password;
    private float precioTotal;
    private boolean incrementar = true;
    

            
    public CarritoController() {
    }
    
    public CarritoController(Integer cantidad_, Producto producto_) {
        this.cantidad = cantidad_;
        this.producto = producto_;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }

    public void meterCantidad() {
        listaCantidad.add(cantidad);
    }
    
    public void setCantidad(Integer cantidad_) {
        this.cantidad = cantidad_;
        meterCantidad();
    }

    public Producto getProducto() {
        return producto;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }  
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public void borrarCarrito() {
        this.listaCarrito.clear();
        calcularPTotal();
    }
    
    public String borrarCarritoZC() {
        this.listaCarrito.clear();
        calcularPTotal();
        return "/index";
    }

    public List<CarritoController> getListaCarrito() {
        return listaCarrito;
    }

    public void setListaCarrito(List<CarritoController> ListaCarrito) {
        this.listaCarrito = ListaCarrito;
    }

    public CarritoController getCarrito() {
        return carrito;
    }

    public void setCarrito(CarritoController carrito) {
        this.carrito = carrito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Integer> getListaCantidad() {
        return listaCantidad;
    }

    public void setListaCantidad(List<Integer> listaCantidad) {
        this.listaCantidad = listaCantidad;
    }
    
    public void calcularPTotal () {
        precioTotal = 0;
        for (int i = 0; i < this.listaCarrito.size(); i++) {
            float meter = (float)this.listaCarrito.get(i).producto.getPrecio();
            meter = meter * this.listaCarrito.get(i).cantidad;
            precioTotal = precioTotal + meter;
        }
        precioTotal = (float) (Math.round(precioTotal*100.0)/100.0);
    }
    
    public void borrarProducto (){
        
        this.listaCarrito.remove(this.carrito);
        calcularPTotal();
    }    
    
    public void anyadirProductoCarrito() {
        int c = 0;
        boolean esNuevoProducto = true;
        for (int i = 0; i < listaCantidad.size(); i++) {
            if (!(listaCantidad.get(i).equals(0))) {
                c = listaCantidad.get(i);
                break;
            }
        }
        for (int i = 0; i < this.listaCarrito.size(); i++) {
            if(this.listaCarrito.get(i).getProducto().equals(this.producto)){
                int cantidadAntigua = this.listaCarrito.get(i).getCantidad();
                this.listaCarrito.get(i).setCantidad(cantidadAntigua + c);
                esNuevoProducto = false;
            } 
        }

        if ((esNuevoProducto)&&(c!=0)) {
            CarritoController carrito_ = new CarritoController(c, this.producto);
            this.listaCarrito.add(carrito_);
            
        }       
        calcularPTotal();
        this.cantidad = 0;
        this.listaCantidad.clear();
    }
    
    public void incrementoReembolso(){
        float incremento = 5;
        
        if (incrementar){
        precioTotal = precioTotal + incremento;
        precioTotal = (float) (Math.round(precioTotal*100.0)/100.0);
        incrementar=false;
        }
        else{
        precioTotal = precioTotal - incremento;
        precioTotal = (float) (Math.round(precioTotal*100.0)/100.0);
        incrementar=true;
        }        
    }    
}
