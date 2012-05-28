package tienda_pcoello.bean;

import tienda_pcoello.entity.Producto;
import tienda_pcoello.bean.util.JsfUtil;
import tienda_pcoello.bean.util.PaginationHelper;
import tienda_pcoello.session.ProductoFacade;

import java.io.Serializable;
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
import tienda_pcoello.bean.PedidosproductosController;




@ManagedBean(name = "productoController")
@SessionScoped
public class ProductoController implements Serializable {

    @ManagedProperty (value="#{carritoController}")
    private CarritoController carritoController;
    
    private Producto current;
    private DataModel items = null;
    private String nombre = "";
    private String nombreCategoria = "";
    private float precio;
    private int stock = 0;
    private boolean buscado = false;
    
     
    
    @EJB
    private tienda_pcoello.session.ProductoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    

    public ProductoController() {
    }

    public CarritoController getCarritoController() {
        return carritoController;
    }

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }

    public List<CarritoController> getListaCarrito() {
        return carritoController.getListaCarrito();
    }

    public void setListaCarrito(List<CarritoController> listaCarrito) {
        carritoController.setListaCarrito(listaCarrito);
    }

     public List <Producto> mostrarProducto(){
       return filtrarNombre();      
     }
    public List <Producto> filtrarNombre(){
        return ejbFacade.buscarProducto(nombre,nombreCategoria,precio);
    
    }
    public List <Producto> inicializar (){
        return ejbFacade.findAll();
    }
    public Producto getSelected() {
        if (current == null) {
            current = new Producto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProductoFacade getFacade() {
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

    public boolean isBuscado() {
        return buscado;
    }

    public void setBuscado(boolean buscado) {
        this.buscado = buscado;
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Producto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Producto();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    

    public String prepareEdit() {
        current = (Producto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Producto) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
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

    @FacesConverter(forClass = Producto.class)
    public static class ProductoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdproducto());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ProductoController.class.getName());
            }
        }
    }
    
    /*public List<CarritoController> devolverLista() {
        List<CarritoController> listaDevuelta = null;
        List<Integer> productos = PedidosproductosController.listaP;
        List<Integer> cantidades = PedidosproductosController.listaC;
        for (int i = 0; i<productos.size();i++) {
            //Producto nuevo = ejbFacade.buscarProductoPorId(productos.get(i));
            CarritoController cc = new CarritoController(cantidades.get(i),nuevo);
            listaDevuelta.add(cc);
        }
        return listaDevuelta;
    }*/
    
    public void actualizarStock(){
        List<CarritoController> listaActualizaciones = carritoController.getListaCarrito();
        int cantidad;
        Producto producto;
        for(int i=0;i<listaActualizaciones.size();i++){
            cantidad = listaActualizaciones.get(i).getCantidad();
            producto = listaActualizaciones.get(i).getProducto();
            int stock_producto = producto.getStock();
            producto.setStock(stock_producto-cantidad);
            ejbFacade.edit(producto);
        }
        
        //pedidoProductoController.insertarListaProducto();                  
    }
    
    public String disponibilidadProducto(int stock_){
        if(stock_>0){
        return "Inmediata";
        }
        else{
        return "Entrega en 3 semanas";
        }
    }
     public String idByNombre(int idProducto) {
         String vuelta;
         if (idProducto==-1){
             vuelta = "Producto descatalogado";
         }
         else{
             vuelta = ejbFacade.idByNombre(idProducto);
         }
         return vuelta;
        
    }

}   
