package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Accreditation;
import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.exception.PointSaleNotFoundException;
import com.proyecto.challengejava.repository.AccreditationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.proyecto.challengejava.constants.Constants.*;

/**
 * Implementation of the accreditation service.
 * Responsible for registering new accreditations and querying existing ones.
 */
@Service
public class AccreditationServiceImpl implements AccreditationService {

    private final AccreditationRepository repository;
    private final PointSaleService pointSaleService;

    /**
     * Constructor that injects the required repositories and services.
     *
     * @param repository        Repository for accreditations.
     * @param pointSaleService Service for managing sales points.
     */
    public AccreditationServiceImpl(AccreditationRepository repository, PointSaleService pointSaleService) {
        this.repository = repository;
        this.pointSaleService = pointSaleService;
    }

    /**
     * Registers a new accreditation for a specific sales point.
     *
     * @param amount        Amount received.
     * @param idPointSale   ID of the sales point receiving the accreditation.
     * @return {@link Accreditation} object persisted in the database.
     * @throws PointSaleNotFoundException if the sales point does not exist.
     */
    public Accreditation receiveAccreditation(Double amount, Long idPointSale) {
        if (!pointSaleExists(idPointSale)) {
            throw new PointSaleNotFoundException(POINT_OF_SALE_NOT_FOUND + ": " + idPointSale);
        }

        String nombrePuntoVenta = pointSaleService.getAllPointSale().stream()
                .filter(p -> p.getId().equals(idPointSale))
                .map(PointSale::getName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(POINT_OF_SALE_NOT_FOUND));

        Accreditation accreditation = new Accreditation();
        accreditation.setAmount(amount);
        accreditation.setIdPointSale(idPointSale);
        accreditation.setPointSaleName(nombrePuntoVenta);
        accreditation.setDateReception(LocalDate.now());

        return repository.save(accreditation);
    }

    /**
     * Checks whether a sales point with the specified ID exists.
     *
     * @param id ID of the sales point to verify.
     * @return {@code true} if it exists, {@code false} otherwise.
     */
    private boolean pointSaleExists(Long id) {
        return pointSaleService.getAllPointSale().stream().anyMatch(p -> p.getId().equals(id));
    }

    /**
     * Retrieves all accreditations stored in the database.
     *
     * @return Iterable of {@link Accreditation} objects.
     */
    public Iterable<Accreditation> getAccreditations() {
        return repository.findAll();
    }
}