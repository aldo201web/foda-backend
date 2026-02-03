package mx.uv.foda.foda_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
//Levantado o no
    @GetMapping("/api/ping")
    public String ping() {
        return "Conexion a MySQL OK";
    }
}