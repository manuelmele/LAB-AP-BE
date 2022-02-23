package core.wefix.lab.repository;


import core.wefix.lab.entity.PaymentInfo;
import core.wefix.lab.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

    PaymentInfo findByPaypalPaymentId(String paymentId);
}