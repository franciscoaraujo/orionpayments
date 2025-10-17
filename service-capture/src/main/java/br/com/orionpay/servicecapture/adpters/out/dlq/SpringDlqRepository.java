package br.com.orionpay.servicecapture.adpters.out.dlq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SpringDlqRepository extends JpaRepository<DlqMessageEntity, UUID> {}