package com.backend.recMeuble.repository;

import com.backend.recMeuble.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
