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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` int(11) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `comentario` varchar(255) DEFAULT NULL,
  `calificacion` int(11) DEFAULT NULL,
  `fecha_creacion` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_calificacion_producto` (`producto_id`),
  CONSTRAINT `fk_calificacion_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `consultas_sipsa` */

DROP TABLE IF EXISTS `consultas_sipsa`;

CREATE TABLE `consultas_sipsa` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad_kg` double DEFAULT NULL,
  `ciudad` varchar(255) DEFAULT NULL,
  `codigo_producto` int(11) DEFAULT NULL,
  `fecha_captura` datetime(6) DEFAULT NULL,
  `fecha_consulta` datetime(6) DEFAULT NULL,
  `fuente` varchar(255) DEFAULT NULL,
  `precio_promedio` double DEFAULT NULL,
  `producto` varchar(255) DEFAULT NULL,
  `tipo_consulta` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `facturas` */

DROP TABLE IF EXISTS `facturas`;

CREATE TABLE `facturas` (
  `id_factura` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_factura` bigint(20) NOT NULL,
  `ID_USUARIO` int(11) NOT NULL,
  `id_pago` int(11) NOT NULL,
  `fecha_emision` datetime DEFAULT current_timestamp(),
  `subtotal` decimal(10,2) NOT NULL,
  `descuento` decimal(10,2) DEFAULT 0.00,
  `impuesto` decimal(10,2) DEFAULT 0.00,
  `total` double DEFAULT NULL,
  `estado_factura` enum('emitida','pagada','anulada') DEFAULT 'emitida',
  `detalle` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`detalle`)),
  `fecha` date DEFAULT NULL,
  `id_reserva` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_factura`),
  UNIQUE KEY `numero_factura` (`numero_factura`),
  KEY `fk_facturas_usuario` (`ID_USUARIO`),
  KEY `fk_facturas_pagos` (`id_pago`),
  KEY `FK9fcidtcs4sx1jex4t0ytr9txc` (`id_reserva`),
  CONSTRAINT `FK9fcidtcs4sx1jex4t0ytr9txc` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id_reservas`),
  CONSTRAINT `fk_facturas_pagos` FOREIGN KEY (`id_pago`) REFERENCES `pagos` (`id_pago`),
  CONSTRAINT `fk_facturas_usuario` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `inventario` */

DROP TABLE IF EXISTS `inventario`;

CREATE TABLE `inventario` (
  `ID_inventario` int(11) NOT NULL AUTO_INCREMENT,
  `ID_producto` int(11) NOT NULL,
  `Nombre_producto` varchar(120) NOT NULL,
  `productor` varchar(120) NOT NULL,
  `Precio` decimal(10,0) NOT NULL,
  `Fecha_cosecha` date NOT NULL,
  `Peso_kg` decimal(10,0) NOT NULL,
  `stock` int(11) NOT NULL,
  `Descripcion` varchar(100) NOT NULL,
  `Numero_referencia` varchar(120) NOT NULL,
  PRIMARY KEY (`ID_inventario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `metodo_pago` */

DROP TABLE IF EXISTS `metodo_pago`;

CREATE TABLE `metodo_pago` (
  `id_metodo` int(11) NOT NULL AUTO_INCREMENT,
  `metodo` varchar(50) NOT NULL,
  PRIMARY KEY (`id_metodo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `ofertas_productos` */

DROP TABLE IF EXISTS `ofertas_productos`;

CREATE TABLE `ofertas_productos` (
  `ID_OFERTA_PRODUCTO` int(11) NOT NULL AUTO_INCREMENT,
  `USUARIO_CAMPESINO` varchar(50) NOT NULL,
  `FECHA_INICIO_OFERTA` date NOT NULL,
  `FECHA_FIN_OFERTA` date NOT NULL,
  `ID_PRODUCTO` int(11) NOT NULL,
  `precio_oferta` decimal(10,2) NOT NULL,
  `descripcion_oferta` text NOT NULL,
  PRIMARY KEY (`ID_OFERTA_PRODUCTO`),
  KEY `idx_ID_PRODUCTO` (`ID_PRODUCTO`),
  CONSTRAINT `fk_ID_PRODUCTO` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `pagos` */

DROP TABLE IF EXISTS `pagos`;

CREATE TABLE `pagos` (
  `id_pago` int(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_emision` date DEFAULT NULL,
  PRIMARY KEY (`id_pago`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Table structure for table `pqrs` */

DROP TABLE IF EXISTS `pqrs`;

CREATE TABLE `pqrs` (
  `id_pqrs` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) NOT NULL,
  `estado` enum('PENDIENTE','RESUELTO') DEFAULT 'PENDIENTE',
  `mensaje` varchar(1000) DEFAULT NULL,
  `respuesta` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id_pqrs`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `privilegio` */

DROP TABLE IF EXISTS `privilegio`;

CREATE TABLE `privilegio` (
  `ID_PRIVILEGIO` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_PRIVILEGIO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `privilegio_has_usuarios` */

DROP TABLE IF EXISTS `privilegio_has_usuarios`;

CREATE TABLE `privilegio_has_usuarios` (
  `privilegio_ID_privilegio` int(11) NOT NULL,
  `usuarios_ID_USUARIO` int(11) NOT NULL,
  PRIMARY KEY (`privilegio_ID_privilegio`,`usuarios_ID_USUARIO`),
  KEY `fk_privilegio_has_usuarios_usuarios` (`usuarios_ID_USUARIO`),
  CONSTRAINT `fk_privilegio_has_usuarios_privilegio` FOREIGN KEY (`privilegio_ID_privilegio`) REFERENCES `privilegio` (`ID_PRIVILEGIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_privilegio_has_usuarios_usuarios` FOREIGN KEY (`usuarios_ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `producto` */

DROP TABLE IF EXISTS `producto`;

CREATE TABLE `producto` (
  `ID_PRODUCTO` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_campesino` varchar(255) DEFAULT NULL,
  `PRODUCTO_IMAGEN` varchar(255) NOT NULL,
  `nombre_producto` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `peso_kg` double DEFAULT NULL,
  `STOCK` int(11) NOT NULL,
  `FECHA_COSECHA` date DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_PRODUCTO`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `productores` */

DROP TABLE IF EXISTS `productores`;

CREATE TABLE `productores` (
  `id_productor` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `nombre_finca` varchar(255) DEFAULT NULL,
  `ubicacion` varchar(255) NOT NULL,
  `area_cultivo` decimal(38,2) DEFAULT NULL,
  `tipo_produccion` enum('Agrícola','Pecuaria','Mixta') NOT NULL,
  `productos` varchar(255) DEFAULT NULL,
  `años_experiencia` int(11) DEFAULT NULL,
  `capacidad_produccion` decimal(38,2) DEFAULT NULL,
  `contacto_comercial` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado_solicitud` enum('Pendiente','Aprobado','Rechazado') DEFAULT 'Pendiente',
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id_productor`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `productores_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`ID_USUARIO`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `resenas` */

DROP TABLE IF EXISTS `resenas`;

CREATE TABLE `resenas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `puntuacion` int(11) DEFAULT NULL,
  `comentario` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `reservas` */

DROP TABLE IF EXISTS `reservas`;

CREATE TABLE `reservas` (
  `id_reservas` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario_cliente` varchar(255) DEFAULT NULL,
  `usuario_documento` varchar(255) DEFAULT NULL,
  `usuario_telefono` varchar(255) DEFAULT NULL,
  `usuario_correo` varchar(255) DEFAULT NULL,
  `producto` varchar(255) DEFAULT NULL,
  `cantidad_kg` double DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `FECHA_RESERVA` date DEFAULT NULL,
  PRIMARY KEY (`id_reservas`),
  KEY `fk_reservas_producto` (`producto`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `ID_ROL` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE_ROL` enum('administrador','cliente','productor') NOT NULL,
  PRIMARY KEY (`ID_ROL`),
  UNIQUE KEY `NOMBRE_ROL` (`NOMBRE_ROL`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sipsa_abastecimiento` */

DROP TABLE IF EXISTS `sipsa_abastecimiento`;

CREATE TABLE `sipsa_abastecimiento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` int(11) DEFAULT NULL,
  `producto` varchar(255) NOT NULL,
  `fuente` varchar(500) DEFAULT NULL,
  `cantidad_toneladas` double DEFAULT NULL,
  `fecha_mes_inicio` datetime DEFAULT NULL,
  `fecha_consulta` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147247 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `sipsa_datos_mensuales` */

DROP TABLE IF EXISTS `sipsa_datos_mensuales`;

CREATE TABLE `sipsa_datos_mensuales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` int(11) DEFAULT NULL,
  `producto` varchar(255) NOT NULL,
  `fuente` varchar(500) DEFAULT NULL,
  `promedio_kg` double DEFAULT NULL,
  `minimo_kg` double DEFAULT NULL,
  `maximo_kg` double DEFAULT NULL,
  `fecha_mes_inicio` datetime DEFAULT NULL,
  `fecha_consulta` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=205147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `sipsa_datos_semanales` */

DROP TABLE IF EXISTS `sipsa_datos_semanales`;

CREATE TABLE `sipsa_datos_semanales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` int(11) DEFAULT NULL,
  `producto` varchar(255) NOT NULL,
  `fuente` varchar(500) DEFAULT NULL,
  `promedio_kg` double DEFAULT NULL,
  `minimo_kg` double DEFAULT NULL,
  `maximo_kg` double DEFAULT NULL,
  `fecha_inicio_semana` datetime DEFAULT NULL,
  `fecha_consulta` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=243771 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `usuarios` */

DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `ID_USUARIO` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `usuario` varchar(255) NOT NULL,
  `documento` varchar(255) DEFAULT NULL,
  `DIRECCION` varchar(255) DEFAULT NULL,
  `correo` varchar(255) NOT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `FECHA_NACIMIENTO` date DEFAULT NULL,
  `rol` varchar(255) NOT NULL,
  `roles_ID_roles` int(11) NOT NULL DEFAULT 2,
  `CONTRASEÑA` varchar(255) NOT NULL,
  `estado` varchar(255) DEFAULT 'Habilitado',
  PRIMARY KEY (`ID_USUARIO`),
  UNIQUE KEY `CORREO` (`correo`),
  UNIQUE KEY `USUARIO` (`usuario`),
  KEY `roles_ID_roles` (`roles_ID_roles`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `ventas` */

DROP TABLE IF EXISTS `ventas`;

CREATE TABLE `ventas` (
  `id_venta` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_Producto` int(11) NOT NULL,
  `cantidad_kg` double DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `total_venta` double DEFAULT NULL,
  `usuarios_ID_USUARIO` int(11) NOT NULL,
  `usuarios_id_vendedor` bigint(20) DEFAULT NULL,
  `facturas_id_factura` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_venta`),
  KEY `fk_ventas_producto` (`ID_Producto`),
  KEY `fk_ventas_cliente` (`usuarios_ID_USUARIO`),
  KEY `fk_ventas_vendedor` (`usuarios_id_vendedor`),
  CONSTRAINT `fk_ventas_cliente` FOREIGN KEY (`usuarios_ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ventas_producto` FOREIGN KEY (`ID_Producto`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
