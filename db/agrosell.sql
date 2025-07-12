-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-07-2025 a las 15:13:03
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `agrosell`
--
CREATE DATABASE IF NOT EXISTS `agrosell` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `agrosell`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `facturas`
--

CREATE TABLE `facturas` (
  `ID_factura` int(11) NOT NULL,
  `cliente` varchar(100) NOT NULL,
  `direccion` varchar(120) NOT NULL,
  `metodo_pago` varchar(120) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `telefono` int(20) NOT NULL,
  `valor_compra` int(120) NOT NULL,
  `descuento` decimal(50,0) DEFAULT NULL,
  `productos` varchar(255) NOT NULL,
  `cantidad` int(50) NOT NULL,
  `fecha` date NOT NULL,
  `impuesto` decimal(50,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario`
--

CREATE TABLE `inventario` (
  `ID_inventario` int(11) NOT NULL,
  `ID_producto` int(11) NOT NULL,
  `Nombre_producto` varchar(120) NOT NULL,
  `productor` varchar(120) NOT NULL,
  `Precio` decimal(10,0) NOT NULL,
  `Fecha_cosecha` date NOT NULL,
  `Peso_kg` decimal(10,0) NOT NULL,
  `stock` int(11) NOT NULL,
  `Descripcion` varchar(100) NOT NULL,
  `Numero_referencia` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `metodo_pago`
--

CREATE TABLE `metodo_pago` (
  `id_metodo` int(11) NOT NULL,
  `metodo` enum('no definido','efectivo','tranferencia','tarjeta','PSE') NOT NULL DEFAULT 'no definido',
  `id_usuario` int(11) NOT NULL,
  `facturas_ID_factura` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ofertas_productos`
--

CREATE TABLE `ofertas_productos` (
  `ID_OFERTA_PRODUCTO` int(11) NOT NULL,
  `USUARIO_CAMPESINO` varchar(50) NOT NULL,
  `FECHA_INICIO_OFERTA` date NOT NULL,
  `FECHA_FIN_OFERTA` date NOT NULL,
  `ID_PRODUCTO` int(11) NOT NULL,
  `precio_oferta` decimal(10,2) NOT NULL,
  `descripcion_oferta` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ofertas_productos_has_producto`
--

CREATE TABLE `ofertas_productos_has_producto` (
  `oferta_ID` int(11) NOT NULL,
  `producto_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

CREATE TABLE `pagos` (
  `id_pago` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_emision` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `pagos`
--

INSERT INTO `pagos` (`id_pago`, `nombre`, `correo`, `telefono`, `metodo_pago`, `direccion`, `fecha_emision`) VALUES
(1, 'Alicia Sandra Betancur Escobar', 'asdasd@alskdlas.com', '1231231232', 'PayPal', 'Calle 106a 22', '2025-04-10'),
(2, 'ismel salazar', 'ssismel28@gmail.com', '2147483647', 'Nequi', 'Calle 81f 40', '2025-04-10'),
(3, 'ismel salazar', 'ssismel28@gmail.com', '2147483647', 'Nequi', 'Calle 81f 40', '2025-04-10'),
(4, 'Juan Manuel Salazar', 'manu_sa@gmail.com', '1233132123', 'Tarjeta de Credito', 'calle 93 norte', '2025-06-29'),
(5, 'Marcela Marquez', 'marce@gmail.com', '3218465823', 'Tarjeta de Credito', 'Cra. 61g #10 25-sur', '2025-07-04'),
(6, 'Juan Manuel Salazar', 'manu_sa@gmail.com', '3218478565', 'Tarjeta de Credito', 'Cra. 81g #73f 40-sur, Bosa Laureles', '2025-07-11');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pqrs`
--

CREATE TABLE `pqrs` (
  `id_pqrs` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `mensaje` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `pqrs`
--

INSERT INTO `pqrs` (`id_pqrs`, `nombre`, `correo`, `telefono`, `mensaje`) VALUES
(10, 'Ismel Gabriel Salazar Suniaga', 'ssismel28@gmail.com', '3208572565', 'hola me quiero poner en contacto con ustedes'),
(11, 'Ismel Salazar', 'ssismel28@gmail.com', '3208512565', 'holaaaaaaaaaa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `privilegio`
--

CREATE TABLE `privilegio` (
  `ID_PRIVILEGIO` int(11) NOT NULL,
  `DESCRIPCION` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `privilegio_has_usuarios`
--

CREATE TABLE `privilegio_has_usuarios` (
  `privilegio_ID_privilegio` int(11) NOT NULL,
  `usuarios_ID_USUARIO` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `ID_PRODUCTO` int(11) NOT NULL,
  `usuario_campesino` varchar(255) DEFAULT NULL,
  `PRODUCTO_IMAGEN` varchar(255) NOT NULL,
  `nombre_producto` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `peso_kg` double DEFAULT NULL,
  `STOCK` int(11) NOT NULL,
  `FECHA_COSECHA` date DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`ID_PRODUCTO`, `usuario_campesino`, `PRODUCTO_IMAGEN`, `nombre_producto`, `descripcion`, `precio`, `peso_kg`, `STOCK`, `FECHA_COSECHA`, `imagen`, `nombre`) VALUES
(7, 'gabriel', '../img/67f69c4626b0a_arroz.jpg', 'Arroz', 'Arroz blanco', 3500, 1, 40, NULL, NULL, NULL),
(8, 'gabriel', '../img/67f6a38b9d67d_ahuyama.jpg', 'Ahuyama', 'Ahuyama en venta', 14000, 1, 30, NULL, NULL, NULL),
(9, 'gabriel', '../img/67f6a5a04babf_apio.jpg', 'Apio', 'Apios recién cosechados', 4600, 1, 40, NULL, NULL, NULL),
(10, 'gabriel', '../img/67f6ad1aeadd9_arverja.jpg', 'Arverja', 'Alverjas verdes por kg', 3600, 1, 83, NULL, NULL, NULL),
(11, 'gabriel', '../img/67f6b1fb40801_banano.jpg', 'Banano', 'Bananos listos para la venta', 3800, 5, 75, NULL, NULL, NULL),
(12, 'gabriel', '../img/67f6b2bf9ced8_berenjena.jpg', 'Berenjena', 'Berenjenas recién cultivadas', 6700, 4, 46, NULL, NULL, NULL),
(13, 'gabriel', '../img/67f6b397b4f57_brevas.jpg', 'Brevas', 'Brevas listas para la venta y el consumo', 22000, 8, 86, NULL, NULL, NULL),
(14, 'gabriel', '../img/67f6b49a28adb_cebolla.jpg', 'Cebolla', 'Cebollas cabezonas', 3700, 12, 102, NULL, NULL, NULL),
(15, 'gabriel', '../img/67f6b5280cae2_cuajada.jpg', 'Cuajada', 'Queso cuajada en colombia', 16000, 4, 64, NULL, NULL, NULL),
(16, 'gabriel', '../img/67f6bc6b1d747_dragon_fruit.jpg', 'Pitahaya', 'Pitahaya de cascarón espinoso y sabor dulce', 2300, 4, 90, NULL, NULL, NULL),
(17, 'gabriel', '../img/67f6bdfca6951_fresas.jpg', 'Fresas', 'Fresas rojas y jugosas', 12000, 6, 47, NULL, NULL, NULL),
(18, 'gabriel', '../img/67f6befbe3578_frijol.jpg', 'Frijoles', 'Frijoles rojos en venta', 6500, 3, 36, NULL, NULL, NULL),
(19, 'gabriel', '../img/67f6c1e2f0f2c_garbanzo.jpg', 'Garbanzo', 'GARBANZO EN GRANO', 12650, 5, 90, NULL, NULL, NULL),
(20, 'gabriel', '../img/67f6ca341e846_leche.jpg', 'Leche', 'Leche entera por lt', 5200, 10, 100, NULL, NULL, NULL),
(21, 'gabriel', '../img/67f6ca82d62d7_lechuga.jpg', 'Lechuga', 'Lechuga fresca', 1200, 3, 42, NULL, NULL, NULL),
(22, 'gabriel', '../img/67f6cfabcc0b9_lenteja.jpg', 'Lentejas', 'Una fuente abundante de fibra, ácido fólico y potasio', 5200, 2, 28, NULL, NULL, NULL),
(23, 'gabriel', '../img/67f6d028733a3_mandarina.jpg', 'Mandarinas', 'Deliciosas mandarinas jugosas y dulces', 4350, 7, 35, NULL, NULL, NULL),
(24, 'gabriel', '../img/67f6d15b2a5d8_mango.jpg', 'Mango', 'Mango tommy pintón', 6700, 5, 35, NULL, NULL, NULL),
(25, 'gabriel', '../img/67f6d25622799_mantequilla.jpg', 'Mantequilla', 'Mantequilla en venta', 15000, 1, 50, NULL, NULL, NULL),
(26, 'gabriel', '../img/67f6f262592b1_papaya.jpg', 'Papaya', 'Deliciosas papayas en venta', 2550, 4, 20, NULL, NULL, NULL),
(27, 'gabriel', '../img/67f7e37f57526_uva.jpg', 'Uva', 'Uvas deliciosas', 5400, 2, 35, NULL, NULL, NULL),
(28, 'gabriel', '../img/67f7e547c6240_pera.jpg', 'Pera', 'Peras en oferta', 9200, 2, 11, NULL, NULL, NULL),
(29, 'gabriel', '../img/67f7e5949f48b_pimenton.jpg', 'Pimenton', 'Pimentón a buen precio', 8600, 1, 63, NULL, NULL, NULL),
(30, 'karol bur24', '../img/67f861509da47_piña.jpg', 'Piña', 'Piñas jugosas y dulces', 7200, 1, 70, NULL, NULL, NULL),
(34, 'karol bur24', '../img/51f815df-904c-4ffc-8e49-8ccea5c8b768_papas.png', 'Papas', 'papapara', 2300, 5, 9, '2025-07-11', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resenas`
--

CREATE TABLE `resenas` (
  `id` bigint(20) NOT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `puntuacion` int(11) DEFAULT NULL,
  `comentario` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `resenas`
--

INSERT INTO `resenas` (`id`, `usuario`, `correo`, `puntuacion`, `comentario`) VALUES
(1, 'gabriel', 'gabriel@gmail.com', 3, 'BIEN'),
(2, 'karol', 'karol@gmail.com', 3, 'Muy buenas comunicación pero falta de profesionalismo'),
(5, 'Ismel Salazar', 'ssimel28@gmail.com', 0, 'muy malos'),
(6, 'Ismel Salazar', 'ssismel28@gmail.com', 0, 'muy mala personas'),
(7, 'Ismel Gabriel Salazar Suniaga', 'ssismel28@gmail.com', 0, 'Son los mejores del mundo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reservas`
--

CREATE TABLE `reservas` (
  `ID_Reservas` int(11) NOT NULL,
  `usuario_cliente` varchar(255) DEFAULT NULL,
  `usuario_documento` varchar(255) DEFAULT NULL,
  `usuario_telefono` varchar(255) DEFAULT NULL,
  `usuario_correo` varchar(255) DEFAULT NULL,
  `producto` varchar(255) DEFAULT NULL,
  `cantidad_kg` double DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `FECHA_RESERVA` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `reservas`
--

INSERT INTO `reservas` (`ID_Reservas`, `usuario_cliente`, `usuario_documento`, `usuario_telefono`, `usuario_correo`, `producto`, `cantidad_kg`, `metodo_pago`, `FECHA_RESERVA`) VALUES
(1, 'ismel gabriel salazar suniaga', '5979141', '2147483647', 'ssismel28@gmail.com', 'producto2', 2, 'EFECTIVO', NULL),
(2, 'Laura Lopez', '542903', '2147483647', 'laura_0711@gmail.com', 'Mango', 15, '', NULL),
(3, 'Karol Estela', '98232323', '2147483647', 'karolestela@gmail.com', 'Mango', 2, 'EFECTIVO', NULL),
(4, 'Karol Estela', '98232323', '2147483647', 'karolestela@gmail.com', 'Mango', 2, 'EFECTIVO', NULL),
(5, 'Laura Lopez', '98232323', '2147483647', 'laura_0711@gmail.com', 'Manzana', 5, 'TARJETA', NULL),
(6, 'manuel', '987654321', '2147483647', 'manu_sa@gmail.com', 'Tomate', 30, 'TARJETA', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reservas_has_producto`
--

CREATE TABLE `reservas_has_producto` (
  `reservas_ID_Reservas` int(11) NOT NULL,
  `producto_ID_producto` int(11) NOT NULL,
  `cantidad` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `ID_ROL` int(11) NOT NULL,
  `NOMBRE_ROL` enum('administrador','cliente','productor') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`ID_ROL`, `NOMBRE_ROL`) VALUES
(1, 'administrador'),
(2, 'cliente'),
(3, 'productor');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `ID_USUARIO` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `documento` varchar(255) DEFAULT NULL,
  `DIRECCION` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `FECHA_NACIMIENTO` date DEFAULT NULL,
  `rol` varchar(255) DEFAULT NULL,
  `roles_ID_roles` int(11) DEFAULT 2,
  `CONTRASEÑA` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `contraseñas` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`ID_USUARIO`, `nombre`, `usuario`, `documento`, `DIRECCION`, `correo`, `metodo_pago`, `FECHA_NACIMIENTO`, `rol`, `roles_ID_roles`, `CONTRASEÑA`, `password`, `contraseñas`) VALUES
(1, 'Ismel Gabriel Salazar Suniaga', 'ismel salazar', '59791412', 'Carrera 81g #73f-40sur', 'ssismel28@gmail.com', 'Transferencia', '2024-10-17', 'administrador', 1, '$2a$10$3w9OtGiwSm6HKSiScW83KOQSrAx4HfeGd3vb/OemnHrSPlRIkh4te', NULL, NULL),
(2, 'gabriel ismel suniaga salazar', 'gabriel', '7825451647', 'Calle 72 #105 a 26', 'gabriel@gmail.com', 'Efectivo', '2002-07-10', 'productor', 3, '$2b$10$JlRT64H.x4yew4XedUBTNuB7K/60Z9qVwfJqRWPBT9WuEt3NGb4pS', NULL, NULL),
(3, 'Karol Estela Burbano Lopez', 'karol bur24', '2832352123', 'Calle 106a 22', 'esletabur24@gmail.com', 'Nequi', '1998-10-14', 'productor', 3, '$2a$10$ryZbbs84WuzcLBb2/xbZ..mRl5CgkfL11PYs64is84lA5itng6qZC', NULL, NULL),
(5, 'Juan Manuel Salazar', 'manuel', '4658156478', '', 'manu_sa@gmail.com', 'Tarjeta de credito', '1992-07-14', 'cliente', 2, '$2y$10$OUYUQGklFAfss9.g3Wk7xumFMrSxe2bGZsEL8s0vokuwYvxjihVqq', NULL, NULL),
(6, 'administrador', 'admin', '3454545458', 'Carrera 15 #45', 'admin@gmail.com', 'Efectivo', '1994-06-21', 'administrador', 1, '$2y$10$HZj47J1WDzcE3yiP9bpZCu.LDoARwFMAceGELYp.YxbN6F8piT4yq', NULL, NULL),
(7, 'Productor', 'productor', NULL, NULL, 'productor@gmail.com', NULL, NULL, 'productor', 3, '$2a$10$sF.OjciEmfsqovelT/6GnuBq2glBWWVOSym8bVZ8el1gpu8x8VaKW', NULL, NULL),
(8, 'Cliente', 'cliente', NULL, NULL, 'cliente@gmail.com', NULL, NULL, 'cliente', 2, '$2y$10$1B2iIdXn22r.b5qwm/atxufPbUI55UrfXCBLo0.du.wJ0M6xSgyWm', NULL, NULL),
(10, 'Marcela Marquez', 'marcela', NULL, NULL, 'marce@gmail.com', NULL, NULL, 'cliente', 2, '$2a$10$NvddrKovlL2eqcXLBgZQ3uO6OO4FlKbhvzLWrxGNbkDEQgjFr8DX6', NULL, NULL);

--
-- Disparadores `usuarios`
--
DELIMITER $$
CREATE TRIGGER `actualizar_idrol` BEFORE UPDATE ON `usuarios` FOR EACH ROW BEGIN
    IF NEW.ROL = 'administrador' THEN
        SET NEW.roles_ID_roles = 1;
    ELSEIF NEW.ROL = 'cliente' THEN
        SET NEW.roles_ID_roles = 2;
    ELSEIF NEW.ROL = 'productor' THEN
        SET NEW.roles_ID_roles = 3;
    ELSE
        SET NEW.roles_ID_roles = NEW.roles_ID_roles; -- Si no coincide, mantiene el valor actual
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id_venta` bigint(20) NOT NULL,
  `ID_Producto` int(11) NOT NULL,
  `cantidad_kg` double DEFAULT NULL,
  `FECHA_VENTA` date NOT NULL,
  `total_venta` double DEFAULT NULL,
  `usuarios_ID_USUARIO` int(11) NOT NULL,
  `usuarios_id_vendedor` bigint(20) DEFAULT NULL,
  `facturas_ID_factura` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `ventas`
--

INSERT INTO `ventas` (`id_venta`, `ID_Producto`, `cantidad_kg`, `FECHA_VENTA`, `total_venta`, `usuarios_ID_USUARIO`, `usuarios_id_vendedor`, `facturas_ID_factura`) VALUES
(1, 30, 2, '2025-06-27', 14400, 8, 3, NULL),
(2, 30, 2, '2025-06-29', 14400, 5, 3, NULL),
(3, 24, 2, '2025-06-29', 13400, 5, 2, NULL),
(4, 27, 4, '2025-06-29', 21600, 5, 2, NULL),
(5, 22, 3, '2025-06-29', 15600, 5, 2, NULL),
(6, 29, 3, '2025-06-29', 25800, 5, 2, NULL),
(7, 27, 1, '2025-06-29', 5400, 5, 2, NULL),
(8, 24, 2, '2025-06-29', 13400, 5, 2, NULL),
(9, 12, 3, '2025-06-29', 20100, 5, 2, NULL),
(10, 23, 2, '2025-06-29', 8700, 5, 2, NULL),
(11, 25, 3, '2025-06-29', 45000, 5, 2, NULL),
(12, 18, 2, '2025-06-29', 13000, 5, 2, NULL),
(13, 28, 5, '2025-07-04', 46000, 10, 2, NULL),
(14, 26, 4, '2025-07-04', 10200, 10, 2, NULL),
(15, 28, 15, '2025-07-11', 138000, 5, 2, NULL),
(16, 34, 5, '2025-07-11', 11500, 5, 3, NULL),
(17, 24, 10, '2025-07-11', 67000, 5, 2, NULL),
(18, 23, 7, '2025-07-11', 30450, 5, 2, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD PRIMARY KEY (`ID_factura`);

--
-- Indices de la tabla `inventario`
--
ALTER TABLE `inventario`
  ADD PRIMARY KEY (`ID_inventario`);

--
-- Indices de la tabla `metodo_pago`
--
ALTER TABLE `metodo_pago`
  ADD PRIMARY KEY (`id_metodo`),
  ADD KEY `fk_metodo_pago_facturas1` (`facturas_ID_factura`);

--
-- Indices de la tabla `ofertas_productos`
--
ALTER TABLE `ofertas_productos`
  ADD PRIMARY KEY (`ID_OFERTA_PRODUCTO`),
  ADD KEY `idx_ID_PRODUCTO` (`ID_PRODUCTO`);

--
-- Indices de la tabla `ofertas_productos_has_producto`
--
ALTER TABLE `ofertas_productos_has_producto`
  ADD PRIMARY KEY (`oferta_ID`,`producto_ID`),
  ADD KEY `fk_producto_oferta` (`producto_ID`);

--
-- Indices de la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD PRIMARY KEY (`id_pago`);

--
-- Indices de la tabla `pqrs`
--
ALTER TABLE `pqrs`
  ADD PRIMARY KEY (`id_pqrs`);

--
-- Indices de la tabla `privilegio`
--
ALTER TABLE `privilegio`
  ADD PRIMARY KEY (`ID_PRIVILEGIO`);

--
-- Indices de la tabla `privilegio_has_usuarios`
--
ALTER TABLE `privilegio_has_usuarios`
  ADD PRIMARY KEY (`privilegio_ID_privilegio`,`usuarios_ID_USUARIO`),
  ADD KEY `fk_privilegio_has_usuarios_usuarios` (`usuarios_ID_USUARIO`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`ID_PRODUCTO`);

--
-- Indices de la tabla `resenas`
--
ALTER TABLE `resenas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD PRIMARY KEY (`ID_Reservas`),
  ADD KEY `fk_reservas_producto` (`producto`);

--
-- Indices de la tabla `reservas_has_producto`
--
ALTER TABLE `reservas_has_producto`
  ADD PRIMARY KEY (`reservas_ID_Reservas`,`producto_ID_producto`),
  ADD KEY `fk_reservas_has_producto_producto` (`producto_ID_producto`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`ID_ROL`),
  ADD UNIQUE KEY `NOMBRE_ROL` (`NOMBRE_ROL`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`ID_USUARIO`),
  ADD UNIQUE KEY `CORREO` (`correo`),
  ADD UNIQUE KEY `NOMBRE` (`nombre`),
  ADD UNIQUE KEY `USUARIO` (`usuario`),
  ADD UNIQUE KEY `DOCUMENTO` (`documento`),
  ADD KEY `roles_ID_roles` (`roles_ID_roles`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id_venta`),
  ADD KEY `fk_ventas_producto` (`ID_Producto`),
  ADD KEY `fk_ventas_cliente` (`usuarios_ID_USUARIO`),
  ADD KEY `fk_ventas_vendedor` (`usuarios_id_vendedor`),
  ADD KEY `fk_ventas_facturas` (`facturas_ID_factura`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `facturas`
--
ALTER TABLE `facturas`
  MODIFY `ID_factura` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `inventario`
--
ALTER TABLE `inventario`
  MODIFY `ID_inventario` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `metodo_pago`
--
ALTER TABLE `metodo_pago`
  MODIFY `id_metodo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ofertas_productos`
--
ALTER TABLE `ofertas_productos`
  MODIFY `ID_OFERTA_PRODUCTO` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pagos`
--
ALTER TABLE `pagos`
  MODIFY `id_pago` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `pqrs`
--
ALTER TABLE `pqrs`
  MODIFY `id_pqrs` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `privilegio`
--
ALTER TABLE `privilegio`
  MODIFY `ID_PRIVILEGIO` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `ID_PRODUCTO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT de la tabla `resenas`
--
ALTER TABLE `resenas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `reservas`
--
ALTER TABLE `reservas`
  MODIFY `ID_Reservas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `ID_ROL` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `ID_USUARIO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `id_venta` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `metodo_pago`
--
ALTER TABLE `metodo_pago`
  ADD CONSTRAINT `fk_metodo_pago_facturas1` FOREIGN KEY (`facturas_ID_factura`) REFERENCES `facturas` (`ID_factura`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `ofertas_productos`
--
ALTER TABLE `ofertas_productos`
  ADD CONSTRAINT `fk_ID_PRODUCTO` FOREIGN KEY (`ID_PRODUCTO`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ofertas_productos_has_producto`
--
ALTER TABLE `ofertas_productos_has_producto`
  ADD CONSTRAINT `fk_oferta_producto` FOREIGN KEY (`oferta_ID`) REFERENCES `ofertas_productos` (`ID_OFERTA_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_producto_oferta` FOREIGN KEY (`producto_ID`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `privilegio_has_usuarios`
--
ALTER TABLE `privilegio_has_usuarios`
  ADD CONSTRAINT `fk_privilegio_has_usuarios_privilegio` FOREIGN KEY (`privilegio_ID_privilegio`) REFERENCES `privilegio` (`ID_PRIVILEGIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_privilegio_has_usuarios_usuarios` FOREIGN KEY (`usuarios_ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `reservas_has_producto`
--
ALTER TABLE `reservas_has_producto`
  ADD CONSTRAINT `fk_reservas_has_producto_producto` FOREIGN KEY (`producto_ID_producto`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_reservas_has_producto_reservas` FOREIGN KEY (`reservas_ID_Reservas`) REFERENCES `reservas` (`ID_Reservas`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD CONSTRAINT `fk_ventas_cliente` FOREIGN KEY (`usuarios_ID_USUARIO`) REFERENCES `usuarios` (`ID_USUARIO`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ventas_facturas` FOREIGN KEY (`facturas_ID_factura`) REFERENCES `facturas` (`ID_factura`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ventas_producto` FOREIGN KEY (`ID_Producto`) REFERENCES `producto` (`ID_PRODUCTO`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
