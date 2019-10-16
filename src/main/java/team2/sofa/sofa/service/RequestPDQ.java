package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.model.Pdq;
import team2.sofa.sofa.model.dao.AccountDao;
import team2.sofa.sofa.model.dao.PdqDao;

import java.util.Random;

@Service
public class RequestPDQ {

    @Autowired
    AccountDao accountDao;
    @Autowired
    PdqDao pdqDao;

    public Account getAccount(String iban) {
        return accountDao.findAccountByIban(iban);
    }

    public String generateFiveDigit() {
        Random random = new Random();
        int num = random.nextInt(100000);
        String fiveDigitCode;
        do {fiveDigitCode = String.format("%05d", num);}
        while (pdqDao.existsPdqByFiveDigitcode(fiveDigitCode));
        return fiveDigitCode;
    }

    public String generateEightDigit() {
        Random random = new Random();
        int num = random.nextInt(100000000);
        String eightDigitcode = String.format("%08d", num);
        return eightDigitcode;
    }
    public String storePdq(Account account){
        String code = generateFiveDigit();
        String eightcode = generateEightDigit();
        Pdq temp = new Pdq();
        temp.setFiveDigitcode(code);
        temp.setEightDigitcode(eightcode);
        temp.setCoupledAccount(account);
        pdqDao.save(temp);
        return code;
    }

}