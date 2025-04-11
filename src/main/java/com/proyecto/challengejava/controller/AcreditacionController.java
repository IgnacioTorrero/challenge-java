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

@RestController
@RequestMapping("/api/acreditaciones")
public class AcreditacionController {

    private final AcreditacionService service;
    private final AcreditacionModelAssembler acreditacionAssembler;

    @Autowired
    public AcreditacionController(AcreditacionService service, AcreditacionModelAssembler acreditacionAssembler) {
        this.service = service;
        this.acreditacionAssembler = acreditacionAssembler;
    }

    /*
     * Metodo encargado de recibir la acreditacion para determinado idPuntoVenta, con su
     * respectivo importe, y luego almacenarlo en la BBDD.
     */
    @PostMapping
    public ResponseEntity<AcreditacionResponse> recibirAcreditacion(@RequestBody @Valid AcreditacionRequest request) {
        Acreditacion acreditacion = service.recibirAcreditacion(request.getImporte(), request.getIdPuntoVenta());
        AcreditacionResponse response = mapToResponse(acreditacion);
        return ResponseEntity.ok(acreditacionAssembler.toModel(response));
    }

    /*
     * Metodo encargado de obtener todas las acreditaciones disponibles en la BBDD.
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