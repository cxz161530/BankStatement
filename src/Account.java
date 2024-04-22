import enumTypes.AccountStatus;
import  exceptions.BankException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public abstract class Account {

    private final String accountId;
    private final Date openDate;
    private Date closeDate;

    private final Customer primaryOwner;
    private Customer jointOwner;

    private AccountStatus accountStatus;

    private Date jointOwnershipDate;

    private int currentBalance;

    private final List<Transaction> transactionList;

    public Account(Customer primaryOwner, String accountId, Date openDate) {
        this.accountId = accountId;
        this.openDate = openDate;
        this.accountStatus = AccountStatus.Open;
        this.currentBalance = 0;
        this.transactionList = new ArrayList<>();
        this.primaryOwner = primaryOwner;
        primaryOwner.addAccount(this);
    }

    public Customer getPrimaryOwner() {
        return this.primaryOwner;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public Date getOpenDate() {
        return this.openDate;
    }

    public AccountStatus getAccountStatus() {
        return this.accountStatus;
    }

    public int getCurrentBalance() {
        return this.currentBalance;
    }

    public void setJointOwner(Customer jointOwner, Date jointOwnershipDate) {
        this.jointOwner = jointOwner;
        this.jointOwnershipDate = jointOwnershipDate;
        jointOwner.addAccount(this);
    }

    public Customer getJointOwner() {
        return this.jointOwner;
    }

    public Date getJointOwnershipDate() {
        return this.jointOwnershipDate;
    }

    protected synchronized void addTransaction(Transaction t) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        if (this.accountStatus == AccountStatus.Close)
            throw new BankException("Account " + this.getAccountId() + "closed... Transaction not allowed");

        if (t instanceof DepositTransaction) {
            DepositTransaction depositTransaction = (DepositTransaction) t;

            int amount = depositTransaction.getTransactionAmount(); // Assuming this method exists in DepositTransaction
            deposit(amount);
            depositTransaction.setEndingBalance(this.currentBalance);

            this.transactionList.add(depositTransaction);
            // Print deposit transaction details
//            System.out.println(dateFormat.format(depositTransaction.getTransactionDate())+ " "
//                    + this.getAccountId()+" Deposit: $" + amount+" by " +this.primaryOwner.getName() + "  Running Balance  "
//                    + this.currentBalance );
            t.print(this);



        } else if (t instanceof WithdrawTransaction) {
            WithdrawTransaction withdrawTransaction = (WithdrawTransaction) t;
            int amount = withdrawTransaction.getTransactionAmount();
            if (amount <= this.currentBalance) {
                withdraw(amount);
                withdrawTransaction.setEndingBalance(this.currentBalance);
            } else {
                withdrawTransaction.setDescription("Insufficient balance");
            }
            this.transactionList.add(withdrawTransaction);



        } else if (t instanceof TransferTransaction) {

            TransferTransaction tr = (TransferTransaction) t;

            if (tr.getToAccount().accountStatus == AccountStatus.Close)
                throw new BankException("Account " + this.getAccountId() + " closed... Transaction not allowed");

            tr.setDescription("Transfer from " + this.getAccountId() + " to " + tr.getToAccount().getAccountId());
            this.transactionList.add(tr);
            tr.getToAccount().transactionList.add(tr);
            tr.print(null);
        }

    }

    private synchronized void deposit(int amount) {
        this.currentBalance += amount;
    }

    private synchronized void withdraw(int amount)  {
        this.currentBalance -= amount;
    }

    public synchronized void closeAccount(Date closeDate) {
        this.accountStatus = AccountStatus.Close;
        this.closeDate = closeDate;
    }

    public void printStatement(Date toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        System.out.println("\n\tTransactions for Account " + this.accountId + " Primary Owner: " + this.primaryOwner.getName() + "\n");
        for (Transaction transaction : transactionList) {
            if (transaction.getTransactionDate().compareTo(toDate) <= 0) {
                transaction.print(this);
            }
        }
    }

}


