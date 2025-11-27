/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.5-10.4.32-MariaDB : Database - agrosell
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`agrosell` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */;

USE `agrosell`;

/*Table structure for table `calificaciones` */

DROP TABLE IF EXISTS `calificaciones`;

CREATE TABLE `calificaciones` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `producto_id` INT(11) DEFAULT NULL,
  `usuario_id` BIGINT(20) DEFAULT NULL,
  `comentario` VARCHAR(255) DEFAULT NULL,
  `calificacion` INT(11) DEFAULT NULL,
  `fecha_creacion` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_calificacion_producto` (`producto_id`),
  CONSTRAINT `fk_calificacion_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `consultas_sipsa` */

DROP TABLE IF EXISTS `consultas_sipsa`;

CREATE TABLE `consultas_sipsa` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cantidad_kg` DOUBLE DEFAULT NULL,
  `ciudad` VARCHAR(255) DEFAULT NULL,
  `codigo_producto` INT(11) DEFAULT NULL,
  `fecha_captura` DATETIME(6) DEFAULT NULL,
  `fecha_consulta` DATETIME(6) DEFAULT NULL,
  `fuente` VARCHAR(255) DEFAULT NULL,
  `precio_promedio` DOUBLE DEFAULT NULL,
  `producto` VARCHAR(255) DEFAULT NULL,
  `tipo_consulta` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `facturas` */

DROP TABLE IF EXISTS `facturas`;

CREATE TABLE `facturas` (
  `id_factura` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `numero_factura` BIGINT(20) NOT NULL,
  `ID_USUARIO` INT(11) NOT NULL,
  `id_pago` INT(11) NOT NULL,
  `fecha_emision` DATETIME DEFAULT CURRENT_TIMESTAMP(),
  `subtotal` DECIMAL(10,2) NOT NULL,
  `descuento` DECIMAL(10,2) DEFAULT 0.00,
  `impuesto` DECIMAL(10,2) DEFAULT 0.00,
  `total` DOUBLE DEFAULT NULL,
  `estado_factura` ENUM('emitida','pagada','anulada') DEFAULT 'emitida',
  `detalle` LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`detalle`)),
  `fecha` DATE DEFAULT NULL,
  `id_reserva` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id_factura`),
  UNIQUE KEY `numero_factura` (`numero_factura`),
  KEY `fk_facturas_usuario` (`ID_USUARIO`),
  KEY `fk_facturas_pagos` (`id_pago`),
  KEY `FK9fcidtcs4sx1jex4t0ytr9txc` (`id_reserva`),
  CONSTRAINT `FK9fcidtcs4sx1jex4t0ytr9txc` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id_reservas`),
  CONSTRAINT `fk_facturas_pagos` FOREIGN KEY (`id_pago`) REFERENCES `pagos` (`id_pago`),
  CONSTRAINT `fk_facturas_usuario` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `inventario` */

DROP TABLE IF EXISTS `inventario`;

CREATE TABLE `inventario` (
  `ID_inventario` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_producto` INT(11) NOT NULL,
  `Nombre_producto` VARCHAR(120) NOT NULL,
  `productor` VARCHAR(120) NOT NULL,
  `Precio` DECIMAL(10,0) NOT NULL,
  `Fecha_cosecha` DATE NOT NULL,
  `Peso_kg` DECIMAL(10,0) NOT NULL,
  `stock` INT(11) NOT NULL,
  `Descripcion` VARCHAR(100) NOT NULL,
  `Numero_referencia` VARCHAR(120) NOT NULL,
  PRIMARY KEY (`ID_inventario`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `metodo_pago` */

DROP TABLE IF EXISTS `metodo_pago`;

CREATE TABLE `metodo_pago` (
  `id_metodo` INT(11) NOT NULL AUTO_INCREMENT,
  `metodo` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_metodo`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `ofertas_productos` */

DROP TABLE IF EXISTS `ofertas_productos`;

CREATE TABLE `ofertas_productos` (
  `ID_OFERTA_PRODUCTO` INT(11) NOT NULL AUTO_INCREMENT,
  `USUARIO_CAMPESINO` VARCHAR(50) NOT NULL,
  `FECHA_INICIO_OFERTA` DATE NOT NULL,
  `FECHA_FIN_OFERTA` DATE NOT NULL,
  `ID_PRODUCTO` INT(11) NOT NULL,
  `precio_oferta` DECIMAL(10,2) NOT NULL,
  `descripcion_oferta` TEXT NOT NULL,
  PRIMARY KEY (`ID_OFERTA_PRODUCTO`),
  KEY `idx_ID_PRODUCTO` (`ID_PRODUCTO`),
  CONSTRAINT `fk_ID_PRODUCTO` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `pagos` */

DROP TABLE IF EXISTS `pagos`;

CREATE TABLE `pagos` (
  `id_pago` INT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) DEFAULT NULL,
  `correo` VARCHAR(255) DEFAULT NULL,
  `telefono` VARCHAR(255) DEFAULT NULL,
  `metodo_pago` VARCHAR(255) DEFAULT NULL,
  `direccion` VARCHAR(255) DEFAULT NULL,
  `fecha_emision` DATE DEFAULT NULL,
  PRIMARY KEY (`id_pago`)
) ENGINE=INNODB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `pqrs` */

DROP TABLE IF EXISTS `pqrs`;

CREATE TABLE `pqrs` (
  `id_pqrs` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `correo` VARCHAR(255) NOT NULL,
  `telefono` VARCHAR(255) DEFAULT NULL,
  `tipo` VARCHAR(255) NOT NULL,
  `estado` ENUM('PENDIENTE','RESUELTO') DEFAULT 'PENDIENTE',
  `mensaje` VARCHAR(1000) DEFAULT NULL,
  `respuesta` VARCHAR(1000) DEFAULT NULL,
  PRIMARY KEY (`id_pqrs`)
) ENGINE=INNODB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `privilegio` */

DROP TABLE IF EXISTS `privilegio`;

CREATE TABLE `privilegio` (
  `ID_PRIVILEGIO` INT(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`ID_PRIVILEGIO`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `privilegio_has_usuarios` */

DROP TABLE IF EXISTS `privilegio_has_usuarios`;

CREATE TABLE `privilegio_has_usuarios` (
  `privilegio_ID_privilegio` INT(11) NOT NULL,
  `usuarios_ID_USUARIO` INT(11) NOT NULL,
  PRIMARY KEY (`privilegio_ID_privilegio`,`usuarios_ID_USUARIO`),
  KEY `fk_privilegio_has_usuarios_usuarios` (`usuarios_ID_USUARIO`),
  CONSTRAINT `fk_privilegio_has_usuarios_privilegio` FOREIGN KEY (`privilegio_ID_privilegio`) REFERENCES `privilegio` (`ID_PRIVILEGIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_privilegio_has_usuarios_usuarios` FOREIGN KEY (`usuarios_ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `producto` */

DROP TABLE IF EXISTS `producto`;

CREATE TABLE `producto` (
  `ID_PRODUCTO` INT(11) NOT NULL AUTO_INCREMENT,
  `usuario_campesino` VARCHAR(255) DEFAULT NULL,
  `PRODUCTO_IMAGEN` VARCHAR(255) NOT NULL,
  `nombre_producto` VARCHAR(255) DEFAULT NULL,
  `descripcion` VARCHAR(255) DEFAULT NULL,
  `precio` DOUBLE DEFAULT NULL,
  `peso_kg` DOUBLE DEFAULT NULL,
  `STOCK` INT(11) NOT NULL,
  `FECHA_COSECHA` DATE DEFAULT NULL,
  `estado` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ID_PRODUCTO`)
) ENGINE=INNODB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `productores` */

DROP TABLE IF EXISTS `productores`;

CREATE TABLE `productores` (
  `id_productor` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_usuario` INT(11) NOT NULL,
  `nombre_finca` VARCHAR(255) DEFAULT NULL,
  `ubicacion` VARCHAR(255) NOT NULL,
  `area_cultivo` DECIMAL(38,2) DEFAULT NULL,
  `tipo_produccion` ENUM('Agrícola','Pecuaria','Mixta') NOT NULL,
  `productos` VARCHAR(255) DEFAULT NULL,
  `años_experiencia` INT(11) DEFAULT NULL,
  `capacidad_produccion` DECIMAL(38,2) DEFAULT NULL,
  `contacto_comercial` VARCHAR(255) DEFAULT NULL,
  `descripcion` VARCHAR(255) DEFAULT NULL,
  `estado_solicitud` ENUM('Pendiente','Aprobado','Rechazado') DEFAULT 'Pendiente',
  `fecha_registro` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `fecha_actualizacion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id_productor`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `productores_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`ID_USUARIO`)
) ENGINE=INNODB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `resenas` */

DROP TABLE IF EXISTS `resenas`;

CREATE TABLE `resenas` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(255) DEFAULT NULL,
  `correo` VARCHAR(255) DEFAULT NULL,
  `puntuacion` INT(11) DEFAULT NULL,
  `comentario` VARCHAR(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `reservas` */

DROP TABLE IF EXISTS `reservas`;

CREATE TABLE `reservas` (
  `id_reservas` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `usuario_cliente` VARCHAR(255) DEFAULT NULL,
  `usuario_documento` VARCHAR(255) DEFAULT NULL,
  `usuario_telefono` VARCHAR(255) DEFAULT NULL,
  `usuario_correo` VARCHAR(255) DEFAULT NULL,
  `producto` VARCHAR(255) DEFAULT NULL,
  `cantidad_kg` DOUBLE DEFAULT NULL,
  `metodo_pago` VARCHAR(255) DEFAULT NULL,
  `FECHA_RESERVA` DATE DEFAULT NULL,
  PRIMARY KEY (`id_reservas`),
  KEY `fk_reservas_producto` (`producto`)
) ENGINE=INNODB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `ID_ROL` INT(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE_ROL` ENUM('administrador','cliente','productor') NOT NULL,
  PRIMARY KEY (`ID_ROL`),
  UNIQUE KEY `NOMBRE_ROL` (`NOMBRE_ROL`)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sipsa_abastecimiento` */

DROP TABLE IF EXISTS `sipsa_abastecimiento`;

CREATE TABLE `sipsa_abastecimiento` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` INT(11) DEFAULT NULL,
  `producto` VARCHAR(255) NOT NULL,
  `fuente` VARCHAR(500) DEFAULT NULL,
  `cantidad_toneladas` DOUBLE DEFAULT NULL,
  `fecha_mes_inicio` DATETIME DEFAULT NULL,
  `fecha_consulta` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=147247 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `sipsa_datos_mensuales` */

DROP TABLE IF EXISTS `sipsa_datos_mensuales`;

CREATE TABLE `sipsa_datos_mensuales` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` INT(11) DEFAULT NULL,
  `producto` VARCHAR(255) NOT NULL,
  `fuente` VARCHAR(500) DEFAULT NULL,
  `promedio_kg` DOUBLE DEFAULT NULL,
  `minimo_kg` DOUBLE DEFAULT NULL,
  `maximo_kg` DOUBLE DEFAULT NULL,
  `fecha_mes_inicio` DATETIME DEFAULT NULL,
  `fecha_consulta` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=205147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `sipsa_datos_semanales` */

DROP TABLE IF EXISTS `sipsa_datos_semanales`;

CREATE TABLE `sipsa_datos_semanales` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` INT(11) DEFAULT NULL,
  `producto` VARCHAR(255) NOT NULL,
  `fuente` VARCHAR(500) DEFAULT NULL,
  `promedio_kg` DOUBLE DEFAULT NULL,
  `minimo_kg` DOUBLE DEFAULT NULL,
  `maximo_kg` DOUBLE DEFAULT NULL,
  `fecha_inicio_semana` DATETIME DEFAULT NULL,
  `fecha_consulta` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=243771 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `usuarios` */

DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `ID_USUARIO` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `usuario` VARCHAR(255) NOT NULL,
  `documento` VARCHAR(255) DEFAULT NULL,
  `DIRECCION` VARCHAR(255) DEFAULT NULL,
  `correo` VARCHAR(255) NOT NULL,
  `metodo_pago` VARCHAR(255) DEFAULT NULL,
  `FECHA_NACIMIENTO` DATE DEFAULT NULL,
  `rol` VARCHAR(255) NOT NULL,
  `roles_ID_roles` INT(11) NOT NULL DEFAULT 2,
  `CONTRASEÑA` VARCHAR(255) NOT NULL,
  `estado` VARCHAR(255) DEFAULT 'Habilitado',
  PRIMARY KEY (`ID_USUARIO`),
  UNIQUE KEY `CORREO` (`correo`),
  UNIQUE KEY `USUARIO` (`usuario`),
  KEY `roles_ID_roles` (`roles_ID_roles`)
) ENGINE=INNODB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `ventas` */

DROP TABLE IF EXISTS `ventas`;

CREATE TABLE `ventas` (
  `id_venta` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ID_Producto` INT(11) NOT NULL,
  `cantidad_kg` DOUBLE DEFAULT NULL,
  `FECHA_VENTA` DATE NOT NULL,
  `total_venta` DOUBLE DEFAULT NULL,
  `usuarios_ID_USUARIO` INT(11) NOT NULL,
  `usuarios_id_vendedor` BIGINT(20) DEFAULT NULL,
  `facturas_id_factura` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id_venta`),
  KEY `fk_ventas_producto` (`ID_Producto`),
  KEY `fk_ventas_cliente` (`usuarios_ID_USUARIO`),
  KEY `fk_ventas_vendedor` (`usuarios_id_vendedor`),
  CONSTRAINT `fk_ventas_cliente` FOREIGN KEY (`usuarios_ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ventas_producto` FOREIGN KEY (`ID_Producto`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE factura (
    id_factura BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(50) UNIQUE NOT NULL,
    ID_USUARIO BIGINT NOT NULL,
    id_pago BIGINT NOT NULL,
    fecha_emision DATE NOT NULL,
    subtotal DOUBLE NOT NULL,
    descuento DOUBLE DEFAULT 0.0,
    impuesto DOUBLE DEFAULT 0.0,
    total DOUBLE NOT NULL,
    estado_factura VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    detalle TEXT,
    fecha DATE,
    id_reserva BIGINT
);
CREATE TABLE detalle_factura (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_factura BIGINT NOT NULL,
    ID_PRODUCTO BIGINT NOT NULL,
    nombre_producto VARCHAR(255) NOT NULL,
    cantidad DOUBLE NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL
);
CREATE INDEX idx_factura_usuario ON factura(ID_USUARIO);
CREATE INDEX idx_factura_numero ON factura(numero_factura);
CREATE INDEX idx_factura_estado ON factura(estado_factura);
CREATE INDEX idx_factura_fecha ON factura(fecha_emision);
CREATE INDEX idx_detalle_factura ON detalle_factura(id_factura);
CREATE INDEX idx_detalle_producto ON detalle_factura(ID_PRODUCTO);

/* Trigger structure for table `usuarios` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `actualizar_idrol` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `actualizar_idrol` BEFORE UPDATE ON `usuarios` FOR EACH ROW BEGIN
    IF NEW.ROL = 'administrador' THEN
        SET NEW.roles_ID_roles = 1;
    ELSEIF NEW.ROL = 'cliente' THEN
        SET NEW.roles_ID_roles = 2;
    ELSEIF NEW.ROL = 'productor' THEN
        SET NEW.roles_ID_roles = 3;
    ELSE
        SET NEW.roles_ID_roles = NEW.roles_ID_roles; -- Si no coincide, mantiene el valor actual
    END IF;
END */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
