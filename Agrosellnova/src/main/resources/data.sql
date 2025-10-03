INSERT INTO `roles` (`ID_ROL`, `NOMBRE_ROL`) VALUES
(1, 'administrador'),
(2, 'cliente'),
(3, 'productor');


INSERT INTO `productores` (`id_productor`, `id_usuario`, `nombre_finca`, `ubicacion`, `area_cultivo`, `tipo_produccion`, `productos`, `años_experiencia`, `capacidad_produccion`, `contacto_comercial`, `descripcion`, `estado_solicitud`, `fecha_registro`, `fecha_actualizacion`) VALUES
(4, 16, 'Agrosell Nova Central', 'Ubicación ficticia', 55.00, 'Mixta', 'Papa, Maíz, Leche, Cafe', 20, 100.00, 'Productor@agrosell.com', 'Finca insignia con producción diversificada agrícola y pecuaria para distribución nacional.', 'Pendiente', '2025-10-01 20:12:56', '2025-10-01 20:12:56'),
(5, 18, 'Fina La Esperanza', 'Fusagasuga Cundinamarca', 15.00, 'Agrícola', 'Tomate, pimentón, maíz', 10, 20.00, 'pedro@agrosell.test', 'Cultivos de hortalizas frescas con prácticas sostenibles.', 'Pendiente', '2025-10-01 20:15:26', '2025-10-01 20:15:26'),
(6, 21, 'Hacienda El Roble', 'Palmira, Valle del Cauca', 25.00, 'Pecuaria', 'Leche, queso, carne bovina', 12, 35.00, 'luisa@agrosell.com', 'Ganadería especializada en lácteos de alta calidad.', 'Rechazado', '2025-10-01 20:17:35', '2025-10-01 20:23:04'),
(7, 26, 'AgroCampo San Jorge', 'Pereira, Risaralda', 12.00, 'Agrícola', 'Fresas, aguacates, cítricos', 5, 18.00, 'Mauricio@agrosell.com', 'Especializados en frutas frescas y de exportación.', 'Rechazado', '2025-10-01 20:26:16', '2025-10-01 20:34:57'),
(8, 30, 'Finca Tierra Fértil', 'Villavicencio, Meta', 40.00, 'Agrícola', 'Yuca, plátano, maíz', 15, 60.00, 'mauricio@agrosell.com', 'Cultivo extensivo de productos básicos para mercado regional.', 'Pendiente', '2025-10-01 20:28:20', '2025-10-01 20:28:20'),
(9, 32, 'la finca de una estupida iguana', 'vereda de la estupida iguana', 12.50, 'Pecuaria', 'bolsos pantuflas y recordatorios de que es una iguana estupida', 40, 2.00, 'asfsgdf@gmial.com', 'tradicion familiar de cosehsha de estupidas iguanas', 'Aprobado', '2025-10-02 20:01:38', '2025-10-02 20:03:30');

INSERT INTO `usuarios` (`ID_USUARIO`, `nombre`, `usuario`, `documento`, `DIRECCION`, `correo`, `metodo_pago`, `FECHA_NACIMIENTO`, `rol`, `roles_ID_roles`, `CONTRASEÑA`, `estado`) VALUES
(15, 'Administrador Central', 'admin', NULL, NULL, 'admin@agrosell.com', NULL, NULL, 'administrador', 1, '$2a$10$NyWipJZLBttLLsq4okAade5xd8yhcrhJTyutiS0WS38tIeKArLJf6', 'Habilitado'),
(16, 'Productor Central', 'productor', NULL, NULL, 'productor@agrosell.com', NULL, NULL, 'productor', 3, '$2a$10$ks5//9HOniKhXTibt8W3.uGOXCy5p/Nph1rsUCtHh6gilwHZOcJ2.', 'Habilitado'),
(17, 'Cliente Central', 'cliente', NULL, NULL, 'cliente@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$icWlsHLYah5znTYyLK9foeL0OcqmxZlqj21NHukT2WVSHk9OQktye', 'Habilitado'),
(18, 'Pedro Gonzales', 'pedro', NULL, NULL, 'pedro@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$c0f2gYSYfX8x61xn9987c.uDHMgtuqb2tT2FM3naY0zzfbArA0LH6', 'Habilitado'),
(19, 'Maria Rodriguez', 'maria', NULL, NULL, 'maria@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$vl4pQwpp7fRVEj03rNVXquZv7uQnTHJlosZIAFXwAhM0/cwch2bHm', 'Habilitado'),
(20, 'Juan Perez', 'juan', NULL, NULL, 'juan@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$af7Bt4EPz/BKbKOnsuTyfeFRuneypGO0YoSV8Ja41hFYpCCZDJRe6', 'Habilitado'),
(21, 'Luisa Martinez', 'luisa', NULL, NULL, 'luisa@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$aPo7bsQFJLvkD1kCXt3kVuCkZUMcNHQE5BHi2DYafSOYKf/JUqKyG', 'Habilitado'),
(22, 'Carlos Herrera', 'carlos', NULL, NULL, 'carlos@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$49iY/VTSwx7X3es9Yt/I1ewHmj9Cmbd4eRg.ya/tUBg6ZfxPiTNsm', 'Habilitado'),
(23, 'Andra Lopez', 'andrea', NULL, NULL, 'andrea@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$Fd8SjpHirw3ktjEogzpTNeJrjPgOkVEaBX5/CQEjthnuBl97AUuQO', 'Habilitado'),
(24, 'Diego Ramirez', 'diego', NULL, NULL, 'diego@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$wO6fweceuA808RBvL2.HCefxum.ShuoWfkgpXv1sHU9d0aY8FO7im', 'Habilitado'),
(25, 'Natalia Ruiz', 'natalia', NULL, NULL, 'natalia@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$hPd/w0inQs1eH/cae3145eGgNZywxnAgPsI5mqyUN4am48rgCCm2y', 'Habilitado'),
(26, 'Andres Moreno', 'andres', NULL, NULL, 'andres@gmail.com', NULL, NULL, 'cliente', 2, '$2a$10$UAw/uVtXfSQTo4zemNo4c.4px4JJ8RkLBpfkB4KGv/ZLwes5V8IwO', 'Habilitado'),
(27, 'Paula Castro', 'paula', NULL, NULL, 'paula@gmail.com', NULL, NULL, 'cliente', 2, '$2a$10$cqSMYpGoewlRPAHLODN/HeN9QgjZOm96/LvR6f/Jsdp2YNQfWNgZi', 'Habilitado'),
(28, 'Jorge Ramirez', 'jorge', NULL, NULL, 'jorge@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$NxszKLpbRmPKHMtNWlYXauA3d/Sq/dhGpybxjf/AAdmFdoTJIycHG', 'Habilitado'),
(29, 'Elena Gomez', 'elena', NULL, NULL, 'elana@agrosell.com', NULL, NULL, 'cliente', 2, '$2a$10$lFnNVUIwtndc5Ow0oyw6POx99DBQDe1gM8w.R5qOSCK2aYF.ndOha', 'Habilitado'),
(30, 'Mauricio Vasquez', 'mauricio', NULL, NULL, 'mauricio@gmail.com', NULL, NULL, 'cliente', 2, '$2a$10$eQ26zeiTEcHx3BgcRlA8jO7kqpdWeFf1s9HOkDwNDYbGHmsb8wMsq', 'Habilitado'),
(31, 'Karla Sanchez', 'karla', NULL, NULL, 'karla@gmail.com', NULL, NULL, 'cliente', 2, '$2a$10$rGDKr9kiSci4NmOXiklLm.dCuWD5zzG4XxLf1O2l7zsKdRoHZF3Wm', 'Habilitado'),
(32, 'Luis Edaurdo Prieto Peña', 'Luis', NULL, NULL, 'arad23tp@gmail.com', NULL, NULL, 'productor', 3, '$2a$10$e4Y3AY46I10DltrmaSq/FuaoaUd6HU.ZRAl/mbI7GOOGh7qqtDatm', 'Habilitado'),
(33, 'Luis Edaurdo Prieto Peña1', 'test', NULL, NULL, 'arad23tp23@gmail.com', NULL, NULL, 'administrador', 1, '$2a$10$Zj9hfwefMNlr54pIdneRgO7EgCwKumh3HPbkffFFpZiZgH3g/EWai', 'Habilitado');

