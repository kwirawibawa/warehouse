package com.assesment.warehouse.repository;

import com.assesment.warehouse.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> { }
