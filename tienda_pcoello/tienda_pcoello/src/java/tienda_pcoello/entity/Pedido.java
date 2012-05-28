/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tienda_pcoello.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pcoello
 */
@Entity
@Table(name = "PEDIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByIdpedido", query = "SELECT p FROM Pedido p WHERE p.idpedido = :idpedido"),
    @NamedQuery(name = "Pedido.findByFecha", query = "SELECT p FROM Pedido p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Pedido.findByPreciototal", query = "SELECT p FROM Pedido p WHERE p.preciototal = :preciototal"),
    @NamedQuery(name = "Pedido.findByMetodopago", query = "SELECT p FROM Pedido p WHERE p.metodopago = :metodopago"),
    @NamedQuery(name = "Pedido.findByEstado", query = "SELECT p FROM Pedido p WHERE p.estado = :estado")})
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    // @NotNull
    @Column(name = "IDPEDIDO")
    private Integer idpedido;
    @Size(max = 30)
    @Column(name = "FECHA")
    private String fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIOTOTAL")
    private double preciototal;
    @Column(name = "METODOPAGO")
    private Integer metodopago;
    @Column(name = "ESTADO")
    private Integer estado;
    @JoinColumn(name = "CLIENTE", referencedColumnName = "IDCLIENTE")
    @ManyToOne
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido1")
    private List<Pedidosproductos> pedidosproductosList;

    public Pedido() {
    }

    public Pedido(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public Pedido(Integer idpedido, double preciototal) {
        this.idpedido = idpedido;
        this.preciototal = preciototal;
    }

    public Pedido(String fecha, float preciototal, Integer metodopago, Integer estado, Cliente cliente) {
        this.fecha = fecha;
        this.preciototal = preciototal;
        this.metodopago = metodopago;
        this.estado = estado;
        this.cliente = cliente;
    }
    
    public Integer getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPreciototal() {
        return preciototal;
    }

    public void setPreciototal(double preciototal) {
        this.preciototal = preciototal;
    }

    public Integer getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(Integer metodopago) {
        this.metodopago = metodopago;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @XmlTransient
    public List<Pedidosproductos> getPedidosproductosList() {
        return pedidosproductosList;
    }

    public void setPedidosproductosList(List<Pedidosproductos> pedidosproductosList) {
        this.pedidosproductosList = pedidosproductosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpedido != null ? idpedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idpedido == null && other.idpedido != null) || (this.idpedido != null && !this.idpedido.equals(other.idpedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tienda_pcoello.entity.Pedido[ idpedido=" + idpedido + " ]";
    }
    
}
