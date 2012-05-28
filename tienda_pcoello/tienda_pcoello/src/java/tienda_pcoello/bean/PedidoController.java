package tienda_pcoello.bean;

import tienda_pcoello.entity.Pedido;
import tienda_pcoello.bean.util.JsfUtil;
import tienda_pcoello.bean.util.PaginationHelper;
import tienda_pcoello.session.PedidoFacade;

import java.io.Serializable;
import java.util.*;
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
import tienda_pcoello.bean.ClienteController;
import tienda_pcoello.entity.Cliente;
import tienda_pcoello.entity.Producto;

@ManagedBean(name = "pedidoController")
@SessionScoped
public class PedidoController implements Serializable {
    
    @ManagedProperty(value="#{clienteController}")
    private ClienteController clienteController;
    
    @ManagedProperty(value="#{carritoController}")
    private CarritoController carritoController;
    
    private Pedido current;
    private Pedido pedido;
    private DataModel items = null;
    
    @EJB
    private tienda_pcoello.session.PedidoFacade ejbFacade;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int metodoPago=0;
    private List<String> opcionesPago = new ArrayList();
    private int pagoTarjeta=0;
    private int pagoReembolso=1;
    private List<Pedido> lista = new ArrayList();
    
    
    public PedidoController() {
        opcionesPago.add("Pago contra reembolso");
        opcionesPago.add("Pago con tarjeta");
    }
    public String estadoPedido (int idEstado){
        String estado = "";
        if (idEstado == 0){
            estado= "Nuevo";
        }
        if (idEstado == 1){
            estado= "En preparación";
        }
        if (idEstado == 2){
            estado= "Lito para enviar";
        }
        if (idEstado ==3){
            estado= "De camino";
        }
        return estado;
    
    }

    public Cliente getCliente() {
        return clienteController.getCliente();
    }

    public CarritoController getCarritoController() {
        return carritoController;
    }

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }

    public ClienteController getClienteController() {
        return clienteController;
    }

    public void setClienteController(ClienteController clienteController) {
        this.clienteController = clienteController;
    }
    
    public void setCliente(Cliente cliente) {
        clienteController.setCliente(cliente);
    }

    public PedidoFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PedidoFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public float getPrecioTotal() {
        return carritoController.getPrecioTotal();
    }

    public void setPrecioTotal(float precioTotal) {
        carritoController.setPrecioTotal(precioTotal);
    }
    
    public String metodoPago (int idMetodoPago){
        String metodo = "";
        if (idMetodoPago == 0){
            metodo = "Tarjeta de Crédito";
        }
        else{
            metodo= "Contrarrembolso";
        }
        return metodo;
    }

    public Pedido getSelected() {
        if (current == null) {
            current = new Pedido();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public int getPagoReembolso() {
        return pagoReembolso;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public List<Pedido> getLista() {
        return lista;
    }

    public void setLista(List<Pedido> lista) {
        this.lista = lista;
    }

    
    
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public void setPagoReembolso(int pagoReembolso) {
        this.pagoReembolso = pagoReembolso;
    }

    public int getPagoTarjeta() {
        return pagoTarjeta;
    }

    public void setPagoTarjeta(int pagoTarjeta) {
        this.pagoTarjeta = pagoTarjeta;
    }

    
       
    public int getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(int metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<String> getOpcionesPago() {
        return opcionesPago;
    }

    public void setOpcionesPago(List<String> opcionesPago) {
        this.opcionesPago = opcionesPago;
    }   
    
    
    private PedidoFacade getFacade() {
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

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Pedido) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Pedido();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Pedido) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Pedido) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PedidoDeleted"));
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
    
    
    
   public String nuevoPedido() {
        Calendar c = Calendar.getInstance();
        String dia = Integer.toString(c.get(Calendar.DATE));
        String mes = Integer.toString(c.get(Calendar.MONTH));
        String annio = Integer.toString(c.get(Calendar.YEAR));
        String fecha = dia + "/" + mes + "/" + annio;
        Cliente cl = clienteController.getCliente();
        float precio = (float)(Math.round((carritoController.getPrecioTotal())*100.0)/100.0);
        pedido = new Pedido(fecha, precio, metodoPago, 0, cl);
        ejbFacade.nuevoPedido(pedido); 
        lista = ejbFacade.findAll();
        return "/finalizarCompra";
    }

    @FacesConverter(forClass = Pedido.class)
    public static class PedidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PedidoController controller = (PedidoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pedidoController");
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
            if (object instanceof Pedido) {
                Pedido o = (Pedido) object;
                return getStringKey(o.getIdpedido());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PedidoController.class.getName());
            }
        }
    }   
}
