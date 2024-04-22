import java.text.DateFormat;
import java.util.Date;

public class TransferTransaction extends Transaction {

    private final Account fromAccount;
    private final Account toAccount;

    public TransferTransaction(Date transactionDate, int amount, Customer transactionOwner,
                               Account fromAccount, Account toAccount) {

        super(transactionDate, "Transfer", amount, transactionOwner);
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.fromAccount.addTransaction(this);

        synchronized (this.fromAccount) {
            if (amount <= this.fromAccount.getCurrentBalance()) {
                Transaction t1 = new WithdrawTransaction(transactionDate, amount, transactionOwner, fromAccount);
                Transaction t2 = new DepositTransaction(transactionDate, amount, transactionOwner, toAccount);
            }
        }

    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void print(Account account) {

        System.out.printf("\t%s  %10s $%-6d\tby %-10s\n",
                DateFormat.getDateInstance().format(this.getTransactionDate()),
                this.getDescription(),
                this.getTransactionAmount(),
                this.getTransactionOwner().getName());

    }

}
