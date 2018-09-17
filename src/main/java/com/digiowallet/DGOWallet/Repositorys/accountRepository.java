package com.digiowallet.DGOWallet.Repositorys;

import com.digiowallet.DGOWallet.Entitys.accountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface accountRepository extends JpaRepository<accountEntity, Long> {
@Query(value = "SELECT * FROM digio_db_wallet.account where username = ?1",nativeQuery = true)
accountEntity findUsername(String usertarket);
}