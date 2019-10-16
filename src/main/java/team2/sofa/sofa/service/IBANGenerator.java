package team2.sofa.sofa.service;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class IBANGenerator {

    private static final int LOW = 10000000;
    private static final int HIGH = 100000000;
    private final String BANKCODE = "SOFA";
    private final String COUNTRYCODE = "NL";
    private final int ASCIITOBANKCODE = 55;
    private String safetyNumber;
    private String bankAccountNumber;
    private String IBAN;

    public IBANGenerator() {
        IBAN = ibanGenerator();
    }

    public String ibanGenerator() {
        this.bankAccountNumber = bankAccountNumberGenerator();
        this.safetyNumber = SafetyNumber(this.bankAccountNumber);
        String iban = COUNTRYCODE + safetyNumber + BANKCODE + bankAccountNumber;
        return iban;
    }

    private String bankAccountNumberGenerator() {
        Random r = new Random();
        int result = r.nextInt(HIGH - LOW) + LOW;
        String b = String.format("%010d", result);
        return b;
    }

    private String SafetyNumber(String bankAccountNumber) {
        String bankNumber1 = Integer.toString(((int) BANKCODE.charAt(0)) - ASCIITOBANKCODE);
        String bankNumber2 = Integer.toString(((int) BANKCODE.charAt(1)) - ASCIITOBANKCODE);
        String bankNumber3 = Integer.toString(((int) BANKCODE.charAt(2)) - ASCIITOBANKCODE);
        String bankNumber4 = Integer.toString(((int) BANKCODE.charAt(3)) - ASCIITOBANKCODE);
        String countryNumber1 = Integer.toString(((int) COUNTRYCODE.charAt(0)) - ASCIITOBANKCODE);
        String countryNumber2 = Integer.toString(((int) COUNTRYCODE.charAt(1)) - ASCIITOBANKCODE);
        String longNumberString = bankNumber1 + bankNumber2 + bankNumber3 + bankNumber4 + bankAccountNumber + countryNumber1 + countryNumber2;
        BigInteger longNumber = new BigInteger(longNumberString);
        BigInteger divider = new BigInteger("97");
        BigInteger value = longNumber.mod(divider);
        int valueInt = value.intValue();
        int safetyNumber = 98 - valueInt;
        String paddedSafetyNumber = String.format("%02d", safetyNumber);
        return paddedSafetyNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        bankAccountNumber = bankAccountNumber;
    }

    public String getIBAN() {
        return IBAN;
    }

    public Deque<String> ibanStack(int count) {
        Deque<String> ibanStack = new ArrayDeque<>();
        for (int i = 0; i < count; i++) {
            String iban = ibanGenerator();
            if (!ibanStack.contains(iban)){
                ibanStack.push(ibanGenerator());
            }

        }
        return ibanStack;
    }

    @Override
    public String toString() {
        return "IBANCODE = " + COUNTRYCODE + safetyNumber + BANKCODE + bankAccountNumber;
    }
}
