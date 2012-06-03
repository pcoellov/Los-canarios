package tienda_pcoello.bean;

import tienda_pcoello.entity.Pedidosproductos;
import tienda_pcoello.bean.util.JsfUtil;
import tienda_pcoello.bean.util.PaginationHelper;
import tienda_pcoello.session.PedidosproductosFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import tienda_pcoello.entity.Pedido;
import tienda_pcoello.entity.PedidosproductosPK;
import tienda_pcoello.entity.Producto;

@ManagedBean(name = "pedidosproductosController")
@SessionScoped
public class PedidosproductosController implements Serializable {

    @ManagedProperty (value="#{carritoController}")
    private CarritoController carritoController;
    
    @ManagedProperty (value="#{pedidoController}")
    private PedidoController pedidoController;
    
    private Pedidosproductos current;
    private int cantidad;
    private DataModel items = null;
    @EJB
    private tienda_pcoello.session.PedidosproductosFacade ejbFacade;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<CarritoController> listaAnteriores = new ArrayList();
    private List<Pedidosproductos> listaAnterior = new ArrayList();

    
    public PedidosproductosController() {
    }

    public Pedidosproductos getSelected() {
        if (current == null) {
            current = new Pedidosproductos();
            current.setPedidosproductosPK(new tienda_pcoello.entity.PedidosproductosPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    public CarritoController getCarritoController() {
        return carritoController;
    }

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }

    public PedidoController getPedidoController() {
        return pedidoController;
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }
    
    public List<Pedidosproductos> getListaAnterior() {
        return listaAnterior;
    }

    public PedidosproductosFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PedidosproductosFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Pedido> getLista() {
        return pedidoController.getLista();
    }

    public void setLista(List<Pedido> lista) {
        pedidoController.setLista(lista);
    }

    public List<CarritoController> getListaCarrito() {
        return carritoController.getListaCarrito();
    }

    public void setListaCarrito(List<CarritoController> listaCarrito) {
        carritoController.setListaCarrito(listaCarrito);
    }
    
    public void setListaAnterior(List<Pedidosproductos> listaAnterior) {
        this.listaAnterior = listaAnterior;
    }

   
    
    public List<CarritoController> getListaAnteriores() {
        return listaAnteriores;
    }

    public void setListaAnteriores(List<CarritoController> listaAnteriores) {
        this.listaAnteriores = listaAnteriores;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    private PedidosproductosFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public List<CarritoController> getListaPedido() {
        return listaAnteriores;
    }

    public void setListaPedido(List<CarritoController> listaPedido) {
        this.listaAnteriores = listaPedido;
    }

    
    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Pedidosproductos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Pedidosproductos();
        current.setPedidosproductosPK(new tienda_pcoello.entity.PedidosproductosPK());
        current.setCantidad(cantidad);
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidosproductosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Pedidosproductos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidosproductosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Pedidosproductos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidosproductosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Pedidosproductos.class)
    public static class PedidosproductosControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PedidosproductosController controller = (PedidosproductosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pedidosproductosController");
            return controller.ejbFacade.find(getKey(value));
        }

        tienda_pcoello.entity.PedidosproductosPK getKey(String value) {
            tienda_pcoello.entity.PedidosproductosPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new tienda_pcoello.entity.PedidosproductosPK();
            key.setPedido(Integer.parseInt(values[0]));
            key.setProducto(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(tienda_pcoello.entity.PedidosproductosPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getPedido());
            sb.append(SEPARATOR);
            sb.append(value.getProducto());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Pedidosproductos) {
                Pedidosproductos o = (Pedidosproductos) object;
                return getStringKey(o.getPedidosproductosPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PedidosproductosController.class.getName());
            }
        }
    }

    public String insertarListaProducto() {
        List<CarritoController> listaActualizaciones = carritoController.getListaCarrito();
        int cantidad2;
        Producto producto;
        List<Pedido> lista = pedidoController.getLista();
        Pedido pedido = new Pedido(); 
        for (int i = 1; i<lista.size();i++) {
            pedido = lista.get(i);
        }
        int idPedido = pedido.getIdpedido();
        PedidosproductosPK pk;
        Pedidosproductos p;
        for (int i = 0; i < listaActualizaciones.size(); i++) {
            cantidad2 = listaActualizaciones.get(i).getCantidad();
            producto = listaActualizaciones.get(i).getProducto();
            pk = new PedidosproductosPK(idPedido, producto.getIdproducto());
            p = new Pedidosproductos(pk, cantidad2);
            ejbFacade.insertarNuevo(p);
        }
        return "/finalizarCompra";
    }
    
    public void listaPedidoAntiguo(Pedido pedido) {
        int id = pedido.getIdpedido();
        listaAnterior = ejbFacade.pedidoAnterior(id);
        
        

    }
}
