package tienda_pcoello.bean;

import tienda_pcoello.entity.Cliente;
import tienda_pcoello.bean.util.JsfUtil;
import tienda_pcoello.bean.util.PaginationHelper;
import tienda_pcoello.session.ClienteFacade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
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
import tienda_pcoello.bean.CarritoController;

@ManagedBean(name = "clienteController")
@SessionScoped
@Stateful

public class ClienteController implements Serializable {

    @ManagedProperty(value="#{carritoController}")
    private CarritoController carritoController;
    
    
    private Cliente current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String password="";
    private String user="";
    private String numeroTarjeta="";
    private String fechaCadTarjeta="";
    private Cliente cliente = new Cliente ("","","");
    
    @EJB
    private tienda_pcoello.session.ClienteFacade ejbFacade;

    public ClienteController() {
    }

    public CarritoController getCarritoController() {
        return carritoController;
    }

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }

    public ClienteFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ClienteFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public List <Pedido> filtrarPedidoCliente(){
        
        
        return ejbFacade.pedidoAnterior(cliente);
    
    }
    
    public Cliente getSelected() {
        if (current == null) {
            current = new Cliente();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    private ClienteFacade getFacade() {
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

    public String getFechaCadTarjeta() {
        return fechaCadTarjeta;
    }

    public void setFechaCadTarjeta(String fechaCadTarjeta) {
        this.fechaCadTarjeta = fechaCadTarjeta;
    }

    public String validarAdmin() {
        Cliente cliente2 = ejbFacade.validarUsuario(user, password);
        if ((this.user.equals(cliente2.getUsuario())) && (this.password.equals(cliente2.getPassword()))) {
            if (cliente2.getIdcliente()==1){
            user = "";
            password = "";
            return "/adminIndex";
            }
            else{
            user = "";
            password = "";
            return "/admin_1";
            }
        }
        else {
            user = "";
            password = "";
            return "/admin_1";
        }
    }    
    
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }    
    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Cliente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Cliente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClienteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Cliente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClienteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Cliente) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClienteDeleted"));
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

    @FacesConverter(forClass = Cliente.class)
    public static class ClienteControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
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

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getIdcliente());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ClienteController.class.getName());
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }
  
    public void setUser(String user) {
        this.user = user;
    }
    
    public String registrarNuevoUsuario() {
        if ((cliente.getNombre() != "") && (cliente.getApellidos() != "") && (cliente.getDireccion() != "")) {
            cliente = new Cliente(cliente.getNombre(), cliente.getApellidos(), cliente.getDireccion(), cliente.getUsuario(),cliente.getPassword());
            ejbFacade.insertarCliente(cliente);
            return "/zonaCliente";
        } else {
            return "/registrarCliente";
        }
    }
    
    public String validarUsuario() {
        this.cliente = ejbFacade.validarUsuario(user, password);
        if ((this.user.equals(this.cliente.getUsuario())) && (this.password.equals(this.cliente.getPassword()))) {
            return "/zonaCliente";
        } else {
            return "/carrito";
        }
    }
    
    @Remove public String cancel () {
        return "/index.xhtml";
    }
}
