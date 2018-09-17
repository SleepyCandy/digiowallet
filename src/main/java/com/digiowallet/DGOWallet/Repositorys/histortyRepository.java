package com.digiowallet.DGOWallet.Repositorys;

import com.digiowallet.DGOWallet.Entitys.accountEntity;
import com.digiowallet.DGOWallet.Entitys.historyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface histortyRepository extends JpaRepository<historyEntity, Timestamp> {
    @Query(value = "SELECT * FROM digio_db_wallet.historypayment where username = ?1 and datetime between ?2 and ?3 ",nativeQuery = true)
    List<historyEntity> findAllByDatetimeBetween(String user, LocalDateTime start, LocalDateTime ended);
}
