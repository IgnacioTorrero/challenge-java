package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PointSale;
import java.util.List;

public interface PointSaleService {
    List<PointSale> getAllPointSale();
    void addPointSale(String name);
    void updatePointSale(Long id, String name);
    void deletePointSale(Long id);
}
