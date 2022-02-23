package core.wefix.lab.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import core.wefix.lab.entity.PaymentInfo;
import core.wefix.lab.repository.PaymentInfoRepository;
import core.wefix.lab.utils.object.staticvalues.CurrencyPayPal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})

public class PayPalService {

    private final APIContext apiContext;
    private final PaymentInfoRepository paymentInfoRepository;

    public Payment createPayment(  // Allows specifying details of payment
            Double total,
            CurrencyPayPal currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency.toString());
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //amount.setTotal(String.format("%.0f", total));
        amount.setTotal(total.toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        payment = payment.create(apiContext);

        insertPayment(new PaymentInfo(LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1),
                total,
                currency,
                payment.getId(),
                payment.getPayer().getPaymentMethod()));

        return payment;

    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public PaymentInfo insertPayment(PaymentInfo paymentInfo){
        return paymentInfoRepository.save(paymentInfo);
    }

    public PaymentInfo completePayment(Long accountId, String paymentId){
        PaymentInfo paymentInfo = paymentInfoRepository.findByPaypalPaymentId(paymentId);
        paymentInfo.setPayerId(accountId);
        paymentInfo.setIsValid(true);
        return paymentInfoRepository.save(paymentInfo);
    }

}
