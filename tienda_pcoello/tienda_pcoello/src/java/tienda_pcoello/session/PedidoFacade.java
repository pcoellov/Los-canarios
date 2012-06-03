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
import tienda_pcoello.entity.Pedido;

/**
 *
 * @author pcoello
 */
@Stateless
public class PedidoFacade extends AbstractFacade<Pedido> {
    @PersistenceContext(unitName = "tienda_pcoelloPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoFacade() {
        super(Pedido.class);
    }
    
    public void nuevoPedido(Pedido pedido) {
        em.persist(pedido);
        
    }
    
    public List<Pedido> todosPedidos(){
        Query q;
        List<Pedido> p = null;
        q = em.createQuery("SELECT p FROM Pedido p");
        p = q.getResultList();
        return p;
    }
   
}
