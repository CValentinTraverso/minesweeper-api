package com.example.minesweep.repository;

import com.example.minesweep.domain.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findFirstByEmail(String email);
}
