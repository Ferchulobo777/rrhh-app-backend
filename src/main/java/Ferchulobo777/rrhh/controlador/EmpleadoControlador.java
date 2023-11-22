package Ferchulobo777.rrhh.controlador;



import Ferchulobo777.rrhh.excepcion.RecursoNoEncontradoExepcion;
import Ferchulobo777.rrhh.modelo.Empleado;
import Ferchulobo777.rrhh.servicio.EmpleadoServicio;
import Ferchulobo777.rrhh.servicio.IEmpleadoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//http://localhost:8080/rh-app/
@RequestMapping("rh-app")
@CrossOrigin(value = "http://localhost:5173")
public class EmpleadoControlador {

    private static final Logger logger =
            LoggerFactory.getLogger(EmpleadoControlador.class);

    @Autowired
    private IEmpleadoServicio EmpleadoServicio;

    // http://localhost:8080/rh-app/empleados
    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleados(){
        var empleados = EmpleadoServicio.listarEmpleados();
        empleados.forEach((empleado -> logger.info(empleado.toString())));
        return empleados;
    }
    @PostMapping("/empleados")
    public Empleado agregarEmpleado(@RequestBody Empleado empleado) {
        logger.info("Empleado a agregar: " + empleado);
        return EmpleadoServicio.guardarEmpleado(empleado);
    }

    @GetMapping("empleados/{id}")
    public ResponseEntity<Empleado>
       obtenerEmpleadoPorId(@PathVariable Integer id){
        Empleado empleado = EmpleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null)
            throw new RecursoNoEncontradoExepcion(("No se encontro el id: " + id));
        return ResponseEntity.ok(empleado);
    }
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado>
    actualizarEmpleado(@PathVariable Integer id,
                       @RequestBody Empleado empleadoRecibido){
       Empleado empleado = EmpleadoServicio.buscarEmpleadoPorId(id);
       if (empleado == null )
           throw new RecursoNoEncontradoExepcion("El id recibido no existe: " + id);
       empleado.setNombre(empleadoRecibido.getNombre());
       empleado.setApellido(empleadoRecibido.getApellido());
       empleado.setFecha_nac(empleadoRecibido.getFecha_nac());
       empleado.setCorreo_electronico(empleadoRecibido.getCorreo_electronico());
       empleado.setDepartamento(empleadoRecibido.getDepartamento());
       empleado.setArea(empleadoRecibido.getArea());
       empleado.setSueldo(empleadoRecibido.getSueldo());
       EmpleadoServicio.guardarEmpleado(empleado);
       return ResponseEntity.ok(empleado);
    }
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String, Boolean>>
           eliminarEmpleado(@PathVariable Integer id){
        Empleado empleado = EmpleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null )
            throw new RecursoNoEncontradoExepcion("El id recibido no existe: " + id);
        EmpleadoServicio.eliminarEmpleado(empleado);
        // Json {"eliminado": "true"}
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }

}

