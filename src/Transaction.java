import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class Transaction {

    private final Date transactionDate;
    private final UUID transactionId;

    private String description;

    private final int transactionAmount;

    private final Customer transactionOwner;


    private int endingBalance;

    public Transaction(Date transactionDate, String description, int transactionAmount, Customer transactionOwner) {
        this.transactionId = UUID.randomUUID();
        this.transactionDate = transactionDate;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.transactionOwner = transactionOwner;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public int getTransactionAmount() {
        return this.transactionAmount;
    }

    public Customer getTransactionOwner() {
        return this.transactionOwner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(int endingBalance) {
        this.endingBalance = endingBalance;
    }



    public void print(Account account) {
        if (account != null) {
            System.out.printf("\t%s  Account: %s\t%10s $%-6d\tby %-10s Running Balance $%d\n",
                    DateFormat.getDateInstance().format(this.transactionDate),
                    account.getAccountId(),
                    this.description,
                    this.transactionAmount,
                    this.transactionOwner.getName(),
                    this.getEndingBalance());
        }
        else {
            System.out.printf("\t%s  %10s $%-6d\tby %-10s\n",
                    DateFormat.getDateInstance().format(this.transactionDate),
                    this.description,
                    this.transactionAmount,
                    this.transactionOwner.getName());
        }

    }

}


