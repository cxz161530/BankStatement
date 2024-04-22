import java.util.Date;

public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction(Date transactionDate, int amount, Customer transactionOwner, Account fromAccount) {
        super(transactionDate, "Withdraw", amount, transactionOwner);
        if (fromAccount != null)
            fromAccount.addTransaction(this);
    }
}
