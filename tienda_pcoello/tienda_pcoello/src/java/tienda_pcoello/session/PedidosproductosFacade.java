/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tienda_pcoello.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import tienda_pcoello.entity.Pedidosproductos;
import tienda_pcoello.entity.Producto;

/**
 *
 * @author pcoello
 */
@Stateless
public class PedidosproductosFacade extends AbstractFacade<Pedidosproductos> {
    @PersistenceContext(unitName = "tienda_pcoelloPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidosproductosFacade() {
        super(Pedidosproductos.class);
    }
    
    public void insertarNuevo(Pedidosproductos pedidosproductos){
        em.persist(pedidosproductos);
    }
    public List<Pedidosproductos> pedidoAnterior(int id) {
        Query q;
        List<Pedidosproductos> p = null;
        q = em.createQuery("SELECT p FROM Pedidosproductos p WHERE p.pedidosproductosPK.pedido = :id");
        q.setParameter("id", id);
        p = q.getResultList();
        if (p.size()==0){
            Pedidosproductos pp = new Pedidosproductos();
            Producto pd = new Producto(-1);
            pp.setProducto1(pd);
            p.add(pp);
        }
        return p;
    }
    
    
}
