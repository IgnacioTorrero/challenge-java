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
 * REST controller for handling operations related to accreditations.
 */
@RestController
@RequestMapping("/api/acreditaciones")
public class AccreditationController {

    private final AcreditacionService service;
    private final AcreditacionModelAssembler acreditacionAssembler;

    /**
     * Constructor that injects the required dependencies.
     *
     * @param service               Service responsible for the business logic of accreditations.
     * @param acreditacionAssembler Assembler to convert responses into HATEOAS models.
     */
    @Autowired
    public AccreditationController(AcreditacionService service, AcreditacionModelAssembler acreditacionAssembler) {
        this.service = service;
        this.acreditacionAssembler = acreditacionAssembler;
    }

    /**
     * Endpoint to receive an accreditation and store it in the database.
     *
     * @param request Object containing the accreditation data, including amount and sales point ID.
     * @return HTTP response with the HATEOAS model of the created accreditation.
     */
    @PostMapping
    public ResponseEntity<AcreditacionResponse> recibirAcreditacion(@RequestBody @Valid AcreditacionRequest request) {
        Acreditacion acreditacion = service.recibirAcreditacion(request.getImporte(), request.getIdPuntoVenta());
        AcreditacionResponse response = mapToResponse(acreditacion);
        return ResponseEntity.ok(acreditacionAssembler.toModel(response));
    }

    /**
     * Endpoint to retrieve all accreditations stored in the database.
     *
     * @return HTTP response with a collection of HATEOAS models of accreditations.
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
