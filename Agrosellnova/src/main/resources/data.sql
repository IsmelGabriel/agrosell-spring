INSERT INTO `roles` (`ID_ROL`, `NOMBRE_ROL`) VALUES
(1, 'administrador'),
(2, 'cliente'),
(3, 'productor');

INSERT INTO `usuarios` (`nombre`, `usuario`, `documento`, `DIRECCION`, `correo`, `metodo_pago`, `FECHA_NACIMIENTO`, `rol`, `roles_ID_roles`, `CONTRASEÑA`, `estado`) VALUES
('Administrador Central', 'admin', NULL, NULL, 'admin@agrosell.com', NULL, NULL, 'administrador', 1, '$2a$10$NyWipJZLBttLLsq4okAade5xd8yhcrhJTyutiS0WS38tIeKArLJf6', 'Habilitado');
