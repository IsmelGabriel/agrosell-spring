package com.agrosellnova.Agrosellnova.controladores.apiSipsa;


import com.agrosellnova.Agrosellnova.modelo.apiSipsa.*;
import com.agrosellnova.Agrosellnova.servicio.apiSipsa.SipsaServiceCompleto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/private/informacion_mercados")
public class InformacionMercadosController {

    @Autowired
    private SipsaServiceCompleto sipsaService;

    private static final int REGISTROS_POR_PAGINA = 30;

    @GetMapping
    public String mostrarPaginaPrincipal(Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        configurarSesion(model, session);
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        Map<String, Object> estadisticas = calcularEstadisticasGenerales();
        model.addAttribute("estadisticas", estadisticas);

        return "private/informacion_mercados";
    }

    @GetMapping("/mensuales")
    public String mostrarMensuales(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        configurarSesion(model, session);

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        try {
            List<SipsaDatoMensual> datosMensuales = sipsaService.obtenerDatosMensuales()
                    .stream()
                    .sorted((a, b) -> {
                        if (a.getFechaMesInicio() == null) return 1;
                        if (b.getFechaMesInicio() == null) return -1;
                        return b.getFechaMesInicio().compareTo(a.getFechaMesInicio());
                    })
                    .limit(REGISTROS_POR_PAGINA)
                    .collect(Collectors.toList());

            model.addAttribute("datosMensuales", datosMensuales);
            model.addAttribute("stats", calcularEstadisticasMensuales(datosMensuales));

        } catch (Exception e) {
            System.err.println("Error cargando datos mensuales: " + e.getMessage());
            model.addAttribute("error", "Error al cargar datos: " + e.getMessage());
            model.addAttribute("datosMensuales", new ArrayList<>());
        }

        return "private/informacion_mercados_mensuales";
    }

    @PostMapping("/mensuales/consultar")
    public String consultarMensuales(RedirectAttributes redirectAttributes) {
        try {
            List<SipsaDatoMensual> datos = sipsaService.consultarYGuardarDatosMensuales();
            redirectAttributes.addFlashAttribute("mensaje",
                    "Datos mensuales actualizados exitosamente. " + datos.size() + " registros obtenidos.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error al consultar datos mensuales: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            e.printStackTrace();
        }
        return "redirect:/private/informacion_mercados/mensuales";
    }

    @GetMapping("/mensuales/buscar")
    public String buscarMensuales(
            @RequestParam(required = false) String producto,
            @RequestParam(required = false) String fuente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            Model model,
            HttpSession session, RedirectAttributes redirectAttributes) {

        configurarSesion(model, session);

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        List<SipsaDatoMensual> resultados = sipsaService.buscarDatosMensuales(producto, fuente, fechaDesde, fechaHasta)
                .stream()
                .limit(REGISTROS_POR_PAGINA)
                .collect(Collectors.toList());

        model.addAttribute("datosMensuales", resultados);
        model.addAttribute("filtroAplicado", true);
        model.addAttribute("filtroProducto", producto);
        model.addAttribute("filtroFuente", fuente);
        model.addAttribute("stats", calcularEstadisticasMensuales(resultados));

        return "private/informacion_mercados_mensuales";
    }

    @GetMapping("/semanales")
    public String mostrarSemanales(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        configurarSesion(model, session);

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        try {
            List<SipsaDatoSemanal> datosSemanales = sipsaService.obtenerDatosSemanales()
                    .stream()
                    .sorted((a, b) -> {
                        if (a.getFechaInicioSemana() == null) return 1;
                        if (b.getFechaInicioSemana() == null) return -1;
                        return b.getFechaInicioSemana().compareTo(a.getFechaInicioSemana());
                    })
                    .limit(REGISTROS_POR_PAGINA)
                    .collect(Collectors.toList());

            model.addAttribute("datosSemanales", datosSemanales);
            model.addAttribute("stats", calcularEstadisticasSemanales(datosSemanales));

        } catch (Exception e) {
            System.err.println("Error cargando datos semanales: " + e.getMessage());
            model.addAttribute("error", "Error al cargar datos: " + e.getMessage());
            model.addAttribute("datosSemanales", new ArrayList<>());
        }

        return "private/informacion_mercados_semanales";
    }

    @PostMapping("/semanales/consultar")
    public String consultarSemanales(RedirectAttributes redirectAttributes) {
        try {
            List<SipsaDatoSemanal> datos = sipsaService.consultarYGuardarDatosSemanales();
            redirectAttributes.addFlashAttribute("mensaje",
                    "Datos semanales actualizados exitosamente. " + datos.size() + " registros obtenidos.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error al consultar datos semanales: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            e.printStackTrace();
        }
        return "redirect:/private/informacion_mercados/semanales";
    }

    @GetMapping("/semanales/buscar")
    public String buscarSemanales(
            @RequestParam(required = false) String producto,
            @RequestParam(required = false) String fuente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            Model model,
            HttpSession session,RedirectAttributes redirectAttributes) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        configurarSesion(model, session);

        List<SipsaDatoSemanal> resultados = sipsaService.buscarDatosSemanales(producto, fuente, fechaDesde, fechaHasta)
                .stream()
                .limit(REGISTROS_POR_PAGINA)
                .collect(Collectors.toList());

        model.addAttribute("datosSemanales", resultados);
        model.addAttribute("filtroAplicado", true);
        model.addAttribute("filtroProducto", producto);
        model.addAttribute("filtroFuente", fuente);
        model.addAttribute("stats", calcularEstadisticasSemanales(resultados));

        return "private/informacion_mercados_semanales";
    }

    @GetMapping("/abastecimiento")
    public String mostrarAbastecimiento(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        configurarSesion(model, session);

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        try {
            List<SipsaAbastecimiento> abastecimiento = sipsaService.obtenerAbastecimiento()
                    .stream()
                    .sorted((a, b) -> {
                        if (a.getFechaMesInicio() == null) return 1;
                        if (b.getFechaMesInicio() == null) return -1;
                        return b.getFechaMesInicio().compareTo(a.getFechaMesInicio());
                    })
                    .limit(REGISTROS_POR_PAGINA)
                    .collect(Collectors.toList());

            model.addAttribute("abastecimiento", abastecimiento);
            model.addAttribute("stats", calcularEstadisticasAbastecimiento(abastecimiento));

        } catch (Exception e) {
            System.err.println("Error cargando abastecimiento: " + e.getMessage());
            model.addAttribute("error", "Error al cargar datos: " + e.getMessage());
            model.addAttribute("abastecimiento", new ArrayList<>());
        }

        return "private/informacion_mercados_abastecimiento";
    }

    @PostMapping("/abastecimiento/consultar")
    public String consultarAbastecimiento(RedirectAttributes redirectAttributes) {
        try {
            List<SipsaAbastecimiento> datos = sipsaService.consultarYGuardarAbastecimiento();
            redirectAttributes.addFlashAttribute("mensaje",
                    "Datos de abastecimiento actualizados exitosamente. " + datos.size() + " registros obtenidos.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error al consultar abastecimiento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            e.printStackTrace();
        }
        return "redirect:/private/informacion_mercados/abastecimiento";
    }

    @GetMapping("/abastecimiento/buscar")
    public String buscarAbastecimiento(
            @RequestParam(required = false) String producto,
            @RequestParam(required = false) String fuente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            Model model,
            HttpSession session, RedirectAttributes redirectAttributes) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"productor".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        configurarSesion(model, session);

        List<SipsaAbastecimiento> resultados = sipsaService.buscarAbastecimiento(producto, fuente, fechaDesde, fechaHasta)
                .stream()
                .limit(REGISTROS_POR_PAGINA)
                .collect(Collectors.toList());

        model.addAttribute("abastecimiento", resultados);
        model.addAttribute("filtroAplicado", true);
        model.addAttribute("filtroProducto", producto);
        model.addAttribute("filtroFuente", fuente);
        model.addAttribute("stats", calcularEstadisticasAbastecimiento(resultados));

        return "private/informacion_mercados_abastecimiento";
    }


    private void configurarSesion(Model model, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
    }

    private Map<String, Object> calcularEstadisticasMensuales(List<SipsaDatoMensual> datos) {
        Map<String, Object> stats = new HashMap<>();

        if (datos == null || datos.isEmpty()) {
            stats.put("total", 0);
            stats.put("promedioMax", 0.0);
            stats.put("promedioMin", 0.0);
            stats.put("promedioGeneral", 0.0);
            return stats;
        }

        double max = datos.stream()
                .filter(d -> d.getPromedioKg() != null)
                .mapToDouble(SipsaDatoMensual::getPromedioKg)
                .max()
                .orElse(0.0);

        double min = datos.stream()
                .filter(d -> d.getPromedioKg() != null)
                .mapToDouble(SipsaDatoMensual::getPromedioKg)
                .min()
                .orElse(0.0);

        double promedio = datos.stream()
                .filter(d -> d.getPromedioKg() != null)
                .mapToDouble(SipsaDatoMensual::getPromedioKg)
                .average()
                .orElse(0.0);

        stats.put("total", datos.size());
        stats.put("promedioMax", max);
        stats.put("promedioMin", min);
        stats.put("promedioGeneral", promedio);

        return stats;
    }

    private Map<String, Object> calcularEstadisticasSemanales(List<SipsaDatoSemanal> datos) {
        Map<String, Object> stats = new HashMap<>();

        if (datos == null || datos.isEmpty()) {
            stats.put("total", 0);
            stats.put("promedioMax", 0.0);
            stats.put("promedioMin", 0.0);
            stats.put("promedioGeneral", 0.0);
            return stats;
        }

        double max = datos.stream()
                .filter(d -> d.getPromedioKg() != null)
                .mapToDouble(SipsaDatoSemanal::getPromedioKg)
                .max()
                .orElse(0.0);

        double min = datos.stream()
                .filter(d -> d.getPromedioKg() != null)
                .mapToDouble(SipsaDatoSemanal::getPromedioKg)
                .min()
                .orElse(0.0);

        double promedio = datos.stream()
                .filter(d -> d.getPromedioKg() != null)
                .mapToDouble(SipsaDatoSemanal::getPromedioKg)
                .average()
                .orElse(0.0);

        stats.put("total", datos.size());
        stats.put("promedioMax", max);
        stats.put("promedioMin", min);
        stats.put("promedioGeneral", promedio);

        return stats;
    }

    private Map<String, Object> calcularEstadisticasAbastecimiento(List<SipsaAbastecimiento> datos) {
        Map<String, Object> stats = new HashMap<>();

        if (datos == null || datos.isEmpty()) {
            stats.put("total", 0);
            stats.put("cantidadMax", 0.0);
            stats.put("cantidadMin", 0.0);
            stats.put("cantidadTotal", 0.0);
            return stats;
        }

        double max = datos.stream()
                .filter(d -> d.getCantidadToneladas() != null)
                .mapToDouble(SipsaAbastecimiento::getCantidadToneladas)
                .max()
                .orElse(0.0);

        double min = datos.stream()
                .filter(d -> d.getCantidadToneladas() != null)
                .mapToDouble(SipsaAbastecimiento::getCantidadToneladas)
                .min()
                .orElse(0.0);

        double total = datos.stream()
                .filter(d -> d.getCantidadToneladas() != null)
                .mapToDouble(SipsaAbastecimiento::getCantidadToneladas)
                .sum();

        stats.put("total", datos.size());
        stats.put("cantidadMax", max);
        stats.put("cantidadMin", min);
        stats.put("cantidadTotal", total);

        return stats;
    }

    private Map<String, Object> calcularEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();

        try {

            List<SipsaDatoMensual> mensuales = sipsaService.obtenerDatosMensuales();
            estadisticas.put("totalMensuales", mensuales.size());

            if (!mensuales.isEmpty()) {

                LocalDateTime ultimaMensual = mensuales.stream()
                        .map(SipsaDatoMensual::getFechaConsulta)
                        .filter(f -> f != null)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);
                estadisticas.put("ultimaActualizacionMensual", ultimaMensual);


                mensuales.stream()
                        .max((a, b) -> Double.compare(
                                a.getPromedioKg() != null ? a.getPromedioKg() : 0.0,
                                b.getPromedioKg() != null ? b.getPromedioKg() : 0.0
                        ))
                        .ifPresent(m -> estadisticas.put("productoMayorPromedioMensual", m.getProducto()));
            } else {
                estadisticas.put("ultimaActualizacionMensual", null);
                estadisticas.put("productoMayorPromedioMensual", null);
            }


            List<SipsaDatoSemanal> semanales = sipsaService.obtenerDatosSemanales();
            estadisticas.put("totalSemanales", semanales.size());

            if (!semanales.isEmpty()) {
                LocalDateTime ultimaSemanal = semanales.stream()
                        .map(SipsaDatoSemanal::getFechaConsulta)
                        .filter(f -> f != null)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);
                estadisticas.put("ultimaActualizacionSemanal", ultimaSemanal);

                semanales.stream()
                        .max((a, b) -> Double.compare(
                                a.getPromedioKg() != null ? a.getPromedioKg() : 0.0,
                                b.getPromedioKg() != null ? b.getPromedioKg() : 0.0
                        ))
                        .ifPresent(s -> estadisticas.put("productoMayorPromedioSemanal", s.getProducto()));
            } else {
                estadisticas.put("ultimaActualizacionSemanal", null);
                estadisticas.put("productoMayorPromedioSemanal", null);
            }


            List<SipsaAbastecimiento> abastecimiento = sipsaService.obtenerAbastecimiento();
            estadisticas.put("totalAbastecimiento", abastecimiento.size());

            if (!abastecimiento.isEmpty()) {
                LocalDateTime ultimaAbastecimiento = abastecimiento.stream()
                        .map(SipsaAbastecimiento::getFechaConsulta)
                        .filter(f -> f != null)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);
                estadisticas.put("ultimaActualizacionAbastecimiento", ultimaAbastecimiento);

                abastecimiento.stream()
                        .max((a, b) -> Double.compare(
                                a.getCantidadToneladas() != null ? a.getCantidadToneladas() : 0.0,
                                b.getCantidadToneladas() != null ? b.getCantidadToneladas() : 0.0
                        ))
                        .ifPresent(a -> estadisticas.put("productoMayorAbastecimiento", a.getProducto()));
            } else {
                estadisticas.put("ultimaActualizacionAbastecimiento", null);
                estadisticas.put("productoMayorAbastecimiento", null);
            }


            int totalGeneral = mensuales.size() + semanales.size() + abastecimiento.size();
            estadisticas.put("totalGeneral", totalGeneral);

            Set<String> productosUnicos = new HashSet<>();
            mensuales.forEach(m -> {
                if (m.getProducto() != null) productosUnicos.add(m.getProducto());
            });
            semanales.forEach(s -> {
                if (s.getProducto() != null) productosUnicos.add(s.getProducto());
            });
            abastecimiento.forEach(a -> {
                if (a.getProducto() != null) productosUnicos.add(a.getProducto());
            });
            estadisticas.put("productosUnicos", productosUnicos.size());

        } catch (Exception e) {
            System.err.println("Error calculando estadísticas generales: " + e.getMessage());
            estadisticas.put("totalMensuales", 0);
            estadisticas.put("totalSemanales", 0);
            estadisticas.put("totalAbastecimiento", 0);
            estadisticas.put("totalGeneral", 0);
            estadisticas.put("productosUnicos", 0);
            estadisticas.put("ultimaActualizacionMensual", null);
            estadisticas.put("ultimaActualizacionSemanal", null);
            estadisticas.put("ultimaActualizacionAbastecimiento", null);
        }

        return estadisticas;
    }
}