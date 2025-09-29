package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.dto.DashboardDTO;

public interface DashboardService {
    DashboardDTO obtenerMetricasAdmin();
    DashboardDTO obtenerMetricasCampesino(Long idCampesino);
    DashboardDTO obtenerMetricasCliente(Long idCliente);

}
