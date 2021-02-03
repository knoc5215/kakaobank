package me.jumen.kakaobank.owner.repository;

import me.jumen.kakaobank.owner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByName(String name);
}
