/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tienda_pcoello.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author pcoello
 */
@Embeddable
public class PedidosproductosPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PEDIDO")
    private int pedido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTO")
    private int producto;

    public PedidosproductosPK() {
    }

    public PedidosproductosPK(int pedido, int producto) {
        this.pedido = pedido;
        this.producto = producto;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pedido;
        hash += (int) producto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidosproductosPK)) {
            return false;
        }
        PedidosproductosPK other = (PedidosproductosPK) object;
        if (this.pedido != other.pedido) {
            return false;
        }
        if (this.producto != other.producto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tienda_pcoello.entity.PedidosproductosPK[ pedido=" + pedido + ", producto=" + producto + " ]";
    }
    
}
