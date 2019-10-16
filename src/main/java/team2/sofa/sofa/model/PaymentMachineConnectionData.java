package team2.sofa.sofa.model;

public class PaymentMachineConnectionData {
    private String account;
    private String fiveDigitCode;

    public PaymentMachineConnectionData(String account, String fiveDigitCode) {
        this.account = account;
        this.fiveDigitCode = fiveDigitCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFiveDigitCode() {
        return fiveDigitCode;
    }

    public void setFiveDigitCode(String fiveDigitCode) {
        this.fiveDigitCode = fiveDigitCode;
    }



}
