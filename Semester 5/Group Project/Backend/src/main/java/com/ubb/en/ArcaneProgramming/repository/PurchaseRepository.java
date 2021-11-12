package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
