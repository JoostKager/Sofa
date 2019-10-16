package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.*;
import team2.sofa.sofa.model.dao.AccountDao;
import team2.sofa.sofa.model.dao.BusinessAccountDao;
import team2.sofa.sofa.model.dao.PdqDao;
import team2.sofa.sofa.model.dao.TransactionDao;

@Service
public class PdqInteractionService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    BusinessAccountDao businessAccountDao;

    @Autowired
    PdqDao pdqDao;

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    FundTransfer fundTransfer;

    public Integer doPdqTransaction(PaymentData paymentData) throws Exception {
        Account creditAccount = accountDao.findAccountByIban(paymentData.getCreditAccount());
        Account debitAccount = accountDao.findAccountByIban(paymentData.getDebitAccount());
        if (creditAccount == null || debitAccount == null) {
            throw new Exception("ongeldig rekeningnummer");
        } else if (fundTransfer.insufficientBalance(paymentData.getAmount(), debitAccount)) {
            throw new Exception("balans onvoldoende");
        } else {
            try {
                Transaction transaction = new Transaction(paymentData.getAmount(), paymentData.getDescription(),
                        String.valueOf(paymentData.getDate()), creditAccount, debitAccount);
                return fundTransfer.storeTransaction(debitAccount, creditAccount, transaction);
            } catch (Exception e) {
                throw new Exception("intern probleem bij de bank");
            }
        }
    }

    public String doPdqCoupling(PaymentMachineConnectionData paymentMachineConnectionData) throws Exception {
        try {
            Pdq pdq = pdqDao.findPdqByFiveDigitcode(paymentMachineConnectionData.getFiveDigitCode());
            if (pdq.getFiveDigitcode().equals(paymentMachineConnectionData.getFiveDigitCode()) ||
                    pdq.getCoupledAccount().getIban().equals(paymentMachineConnectionData.getAccount())){
                return pdq.getEightDigitcode();
            } else {
                throw new Exception("invalid code combination");
            }
        } catch (Exception e){
            throw  new Exception("five-digit-code not valid: " + e);
        }
    }




}
