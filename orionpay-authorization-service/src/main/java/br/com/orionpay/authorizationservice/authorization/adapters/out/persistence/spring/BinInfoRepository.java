package br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.spring;


import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.BinInfoEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BinInfoRepository extends JpaRepository<BinInfoEntity, String> {

    @Cacheable("bins")
    @Query(value = "SELECT * FROM bin_info WHERE ?1 LIKE CONCAT(bin_range, '%') ORDER BY LENGTH(bin_range) DESC LIMIT 1", nativeQuery = true)
    Optional<BinInfoEntity> findBestMatch(String cardNumber);
}