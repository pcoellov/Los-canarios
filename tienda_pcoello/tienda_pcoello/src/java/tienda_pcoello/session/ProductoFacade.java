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
import tienda_pcoello.entity.Producto;

/**
 *
 * @author pcoello
 */
@Stateless
public class ProductoFacade extends AbstractFacade<Producto> {
    @PersistenceContext(unitName = "tienda_pcoelloPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }
    public List <Producto> buscarProducto (String nombre, String nombreCategoria, float precio){
         Query q;
         List<Producto> p = null;
         if (!(nombre.isEmpty())){
             if (!(nombreCategoria.isEmpty())){
                 if(!(precio == 0.0)){
                    q = em.createQuery("SELECT p FROM Producto p WHERE p.precio < :precio AND p.categoria.nombre = :nombreCategoria AND p.nombre = :nombre");
                    q.setParameter("precio",precio);
                    q.setParameter("nombre",nombre);
                    q.setParameter("nombreCategoria",nombreCategoria);
                    p = q.getResultList();  
                 }
                 else{
                    q = em.createQuery("SELECT p FROM Producto p WHERE p.categoria.nombre = :nombreCategoria AND p.nombre = :nombre");
                     q.setParameter("nombre",nombre);
                    q.setParameter("nombreCategoria",nombreCategoria);
                    p = q.getResultList();
                 }
             }
             else{
               if(!(precio == 0.0)){
                  q = em.createQuery("SELECT p FROM Producto p WHERE p.precio < :precio AND p.nombre = :nombre"); 
                  q.setParameter("precio",precio);
                  q.setParameter("nombre",nombre);
                  p = q.getResultList();
               }
               else{
                  q = em.createQuery("SELECT p FROM Producto p WHERE p.nombre = :nombre");  
                  q.setParameter("nombre",nombre);
                  p = q.getResultList();
               }
             }
         
         }
         else{
            if (!(nombreCategoria.isEmpty())){
                if(!(precio == 0.0)){
                    q = em.createQuery("SELECT p FROM Producto p WHERE p.precio < :precio AND p.categoria.nombre = :nombreCategoria");
                    q.setParameter("precio",precio);
                    q.setParameter("nombreCategoria",nombreCategoria);
                    p = q.getResultList();
                }
                else{
                   q = em.createQuery("SELECT p FROM Producto p WHERE p.categoria.nombre = :nombreCategoria"); 
                   q.setParameter("nombreCategoria",nombreCategoria);
                    p = q.getResultList();
                }
            
            } 
            else{
                if(!(precio == 0.0)){
                    q = em.createQuery("SELECT p FROM Producto p WHERE p.precio < :precio");
                    q.setParameter("precio",precio);
                    p = q.getResultList();
                    
                }
            }
         
        }
        return p;
    }
    public String idByNombre(int id) {
        Query q;
        Producto p;
        q = em.createQuery("SELECT p FROM Producto p WHERE p.idproducto = :idproducto");
        q.setParameter("idproducto", id);
        /*try {
            p = (Producto) q.getSingleResult();
        } catch (NoResultException e) {
            p = new Producto("Producto no encontrado");
        }*/
        p = (Producto) q.getSingleResult();
        return p.getNombre();
    }
    
   
}
