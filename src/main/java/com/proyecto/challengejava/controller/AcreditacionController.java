package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AcreditacionRequest;
import com.proyecto.challengejava.dto.AcreditacionResponse;
import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.hateoas.AcreditacionModelAssembler;
import com.proyecto.challengejava.mapper.AcreditacionMapper;
import com.proyecto.challengejava.service.AcreditacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.proyecto.challengejava.mapper.AcreditacionMapper.mapToResponse;

/**
 * Controlador REST para manejar operaciones relacionadas con acreditaciones.
 */
@RestController
@RequestMapping("/api/acreditaciones")
public class AcreditacionController {

    private final AcreditacionService service;
    private final AcreditacionModelAssembler acreditacionAssembler;

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param service               Servicio encargado de la lógica de negocio para acreditaciones.
     * @param acreditacionAssembler Ensamblador para convertir respuestas en modelos HATEOAS.
     */
    @Autowired
    public AcreditacionController(AcreditacionService service, AcreditacionModelAssembler acreditacionAssembler) {
        this.service = service;
        this.acreditacionAssembler = acreditacionAssembler;
    }

    /**
     * Endpoint para recibir una acreditación y almacenarla en la base de datos.
     *
     * @param request Objeto con los datos de la acreditación, incluyendo importe e ID del punto de venta.
     * @return Respuesta HTTP con el modelo HATEOAS de la acreditación creada.
     */
    @PostMapping
    public ResponseEntity<AcreditacionResponse> recibirAcreditacion(@RequestBody @Valid AcreditacionRequest request) {
        Acreditacion acreditacion = service.recibirAcreditacion(request.getImporte(), request.getIdPuntoVenta());
        AcreditacionResponse response = mapToResponse(acreditacion);
        return ResponseEntity.ok(acreditacionAssembler.toModel(response));
    }

    /**
     * Endpoint para obtener todas las acreditaciones registradas en la base de datos.
     *
     * @return Respuesta HTTP con una colección de modelos HATEOAS de acreditaciones.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<AcreditacionResponse>> obtenerAcreditaciones() {
        List<AcreditacionResponse> responses = StreamSupport
                .stream(service.obtenerAcreditaciones().spliterator(), false)
                .map(AcreditacionMapper::mapToResponse)
                .map(acreditacionAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(responses));
    }
}