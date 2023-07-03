package co.com.bancolombia.savebackup;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Details {

    private final String description;
    private final String beforeValue;
    private final String afterValue;
    private final String transactionResultCode;
    private final String transactionResultDescription;

    public String getDescription() {
        return description;
    }

    public String getBeforeValue() {
        return beforeValue;
    }

    public String getAfterValue() {
        return afterValue;
    }

    public String getTransactionResultCode() {
        return transactionResultCode;
    }

    public String getTransactionResultDescription() {
        return transactionResultDescription;
    }
}
