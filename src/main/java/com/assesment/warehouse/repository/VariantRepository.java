package com.assesment.warehouse.repository;

import com.assesment.warehouse.model.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    Optional<Variant> findBySku(String sku);
    boolean existsBySkuIgnoreCase(String sku);
}
