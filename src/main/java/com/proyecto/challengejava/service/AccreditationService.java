package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Accreditation;

public interface AccreditationService {
    Accreditation receiveAccreditation(Double amount, Long idPointSale);
    Iterable<Accreditation> getAccreditations();
}
