import java.util.Date;

public class DepositTransaction extends Transaction {

    public DepositTransaction(Date transactionDate, int amount, Customer transactionOwner, Account toAccount) {
        super(transactionDate, "Deposit", amount, transactionOwner);
        if (toAccount != null)
            toAccount.addTransaction(this);
    }
}
