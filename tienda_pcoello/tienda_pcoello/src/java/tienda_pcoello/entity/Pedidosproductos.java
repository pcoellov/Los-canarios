/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tienda_pcoello.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author pcoello
 */
@Entity
@Table(name = "PEDIDOSPRODUCTOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedidosproductos.findAll", query = "SELECT p FROM Pedidosproductos p"),
    @NamedQuery(name = "Pedidosproductos.findByPedido", query = "SELECT p FROM Pedidosproductos p WHERE p.pedidosproductosPK.pedido = :pedido"),
    @NamedQuery(name = "Pedidosproductos.findByProducto", query = "SELECT p FROM Pedidosproductos p WHERE p.pedidosproductosPK.producto = :producto"),
    @NamedQuery(name = "Pedidosproductos.findByCantidad", query = "SELECT p FROM Pedidosproductos p WHERE p.cantidad = :cantidad")})
public class Pedidosproductos implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidosproductosPK pedidosproductosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    @JoinColumn(name = "PRODUCTO", referencedColumnName = "IDPRODUCTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto1;
    @JoinColumn(name = "PEDIDO", referencedColumnName = "IDPEDIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido1;

    public Pedidosproductos() {
    }

    public Pedidosproductos(PedidosproductosPK pedidosproductosPK) {
        this.pedidosproductosPK = pedidosproductosPK;
    }

    public Pedidosproductos(PedidosproductosPK pedidosproductosPK, int cantidad) {
        this.pedidosproductosPK = pedidosproductosPK;
        this.cantidad = cantidad;
    }

    public Pedidosproductos(int pedido, int producto) {
        this.pedidosproductosPK = new PedidosproductosPK(pedido, producto);
    }

    public PedidosproductosPK getPedidosproductosPK() {
        return pedidosproductosPK;
    }

    public void setPedidosproductosPK(PedidosproductosPK pedidosproductosPK) {
        this.pedidosproductosPK = pedidosproductosPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    

    public Producto getProducto1() {
        return producto1;
    }

    public void setProducto1(Producto producto1) {
        this.producto1 = producto1;
    }

    public Pedido getPedido1() {
        return pedido1;
    }

    public void setPedido1(Pedido pedido1) {
        this.pedido1 = pedido1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidosproductosPK != null ? pedidosproductosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedidosproductos)) {
            return false;
        }
        Pedidosproductos other = (Pedidosproductos) object;
        if ((this.pedidosproductosPK == null && other.pedidosproductosPK != null) || (this.pedidosproductosPK != null && !this.pedidosproductosPK.equals(other.pedidosproductosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tienda_pcoello.entity.Pedidosproductos[ pedidosproductosPK=" + pedidosproductosPK + " ]";
    }
    
}
