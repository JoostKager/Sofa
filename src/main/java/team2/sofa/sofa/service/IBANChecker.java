package team2.sofa.sofa.service;

import java.math.BigInteger;

public class IBANChecker {

    public boolean checkControlNumberIBAN(String IBAN) {
        boolean check;
        int securityNumber = Integer.parseInt(Integer.toString(IBAN.charAt(2)) + Integer.toString(IBAN.charAt(3)));

        if (securityNumber < 2 || securityNumber > 98) {
            return check = false;
        } else {
            String longNumberString = bankCodeToString(IBAN) + bankAccountToString(IBAN) + countryCodeToString(IBAN)
                    + securityNumber;
            BigInteger longNumber = new BigInteger(longNumberString);
            BigInteger divider = new BigInteger("97");
            BigInteger value = longNumber.mod(divider);
            int valueInt = value.intValue();
            if (valueInt == 1) {
                return check = true;
            }
        }
        return check = false;
    }

    public String countryCodeToString(String IBAN) {
        int countryCode1 = IBAN.charAt(0) - 55;
        int countryCode2 = IBAN.charAt(1) - 55;
        String countryCode = Integer.toString(countryCode1) + Integer.toString(countryCode2);
        return countryCode;
    }

    public String bankCodeToString(String IBAN) {
        int bankCode1 = IBAN.charAt(4) - 55;
        int bankCode2 = IBAN.charAt(5) - 55;
        int bankCode3 = IBAN.charAt(6) - 55;
        int bankCode4 = IBAN.charAt(7) - 55;
        String bankCode = Integer.toString(bankCode1) + Integer.toString(bankCode2) + Integer.toString(bankCode3)
                + Integer.toString(bankCode4);
        return bankCode;
    }

    public String bankAccountToString(String IBAN) {
        String bankAccount = IBAN.substring(8);
        return bankAccount;
    }


}
