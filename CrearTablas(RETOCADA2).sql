DROP TABLE PedidosProductos;
DROP TABLE Pedido;
DROP TABLE Cliente;
DROP TABLE Producto;
DROP TABLE Categoria;


CREATE TABLE Categoria (
        idCategoria INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        nombre VARCHAR(30),
        PRIMARY KEY (idCategoria));
		
INSERT INTO Categoria (nombre) VALUES ('Alimentación');
INSERT INTO Categoria (nombre) VALUES ('Moda');
INSERT INTO Categoria (nombre) VALUES ('Electrónica');
INSERT INTO Categoria (nombre) VALUES ('Hogar');

CREATE TABLE Producto (
        idProducto INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        nombre VARCHAR(30),
		descripcion VARCHAR(300),
        categoria INT,
        imagen VARCHAR(30),
        precio FLOAT NOT NULL,    
		stock INT,
        PRIMARY KEY (idProducto));

INSERT INTO Producto (nombre,descripcion,categoria,imagen,precio,stock) VALUES ('Aceite','Botella de 1L de aceite de oliva "Carbonell".',1,'img/aceite.jpg', 1.99, 10);
INSERT INTO Producto (nombre,descripcion,categoria,imagen,precio,stock) VALUES ('Sal','Paquete de 300 gramos de sal fina.',1,'img/sal.jpg', 0.59, 10);
INSERT INTO Producto (nombre,descripcion,categoria,imagen,precio,stock) VALUES ('Pantalon','Pantalón vaquero. Tallas 34 a 46.',2,'img/pantalon.jpg', 25.99, 10);
INSERT INTO Producto (nombre,descripcion,categoria,imagen,precio,stock) VALUES ('Portátil','AsusX59SL / RAM 4GB / IntelCore i7 2.2GHz / Windows 7 Starter',3,'img/portatil.jpg', 450.00, 10);
INSERT INTO Producto (nombre,descripcion,categoria,imagen,precio,stock) VALUES ('Sabana','Sábana con dibujos de Toy Story tamaño individual 80x180.',4,'img/sabana.jpg', 15.99, 10);

CREATE TABLE Cliente (
        idCliente INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        nombre VARCHAR(30),
		apellidos VARCHAR(30),
		direccion VARCHAR(30),
		numeroTarjeta VARCHAR(16),
		fechaCadTarjeta VARCHAR(5),
		usuario VARCHAR(10),
		password VARCHAR(10),
        PRIMARY KEY (idCliente));

INSERT INTO Cliente (nombre,apellidos,direccion,numeroTarjeta,fechaCadTarjeta,usuario,password) VALUES ('Pablo','Coello','Calle Tulipan 23','5869001652814758','04/14','Pablo','1234');
INSERT INTO Cliente (nombre,apellidos,direccion,numeroTarjeta,fechaCadTarjeta,usuario,password) VALUES ('Sergio','Jiménez','Calle Atocha 45','5681242257469558','02/13','Sergio','1234');
INSERT INTO Cliente (nombre,apellidos,direccion,numeroTarjeta,fechaCadTarjeta,usuario,password) VALUES ('Javier','Aceitón','Calle España 7','6699558214557459','12/13','Javier','1234');
INSERT INTO Cliente (nombre,apellidos,direccion,numeroTarjeta,fechaCadTarjeta,usuario,password) VALUES ('Davinia','de la Rosa','Calle Canarias 11','5986455125778889','07/14','Davinia','1234');
		
CREATE TABLE Pedido (
        idPedido INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		fecha VARCHAR(30),
        	cliente INT,
		precioTotal FLOAT NOT NULL, 
		metodoPago INT,
		estado INT,
        PRIMARY KEY (idPedido));

INSERT INTO Pedido (fecha,cliente,precioTotal,metodoPago,estado) VALUES ('15/05/2012',4,220.20,0,0);

CREATE TABLE PedidosProductos (
        pedido INT NOT NULL,
        producto INT NOT NULL,
        cantidad INT NOT NULL,
        PRIMARY KEY (pedido,producto));

ALTER TABLE Producto ADD FOREIGN KEY (categoria) REFERENCES Categoria(idCategoria);
ALTER TABLE Pedido ADD FOREIGN KEY (cliente) REFERENCES Cliente(idCliente);
ALTER TABLE PedidosProductos ADD FOREIGN KEY (pedido) REFERENCES Pedido(idPedido);
ALTER TABLE PedidosProductos ADD FOREIGN KEY (producto) REFERENCES Producto(idProducto);

