/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tienda_pcoello.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import tienda_pcoello.entity.Cliente;
import tienda_pcoello.entity.Pedido;

/**
 *
 * @author pcoello
 */
@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {
    @PersistenceContext(unitName = "tienda_pcoelloPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }
    
    public Cliente validarUsuario (String user, String password){
        Query q;
         Cliente c ;
                    q = em.createQuery("SELECT c FROM Cliente c WHERE c.usuario= :user AND c.password= :pass");
                    q.setParameter("user",user);
                    q.setParameter("pass",password);
                    try{
                    c = (Cliente)q.getSingleResult(); 
                    }
                    catch (NoResultException e){
                        c = new Cliente ("nombre","Apellidos","Direccion");
                    }
                    return c;    
    }    
    public List <Pedido> pedidoAnterior (Cliente cliente){
        Query q;
         List<Pedido> p = null;
                    q = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.idcliente = :id");
                    q.setParameter("id",cliente.getIdcliente());
                    p = q.getResultList();               
                    return p;
    
    }
    public void insertarCliente (Cliente cliente){
        em.persist(cliente);
    
    }
}
