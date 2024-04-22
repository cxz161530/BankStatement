import enumTypes.TransactionType;
import enumTypes.AccountType;
import  exceptions.BankException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class Bank {

    private final Map<String, Customer> customerMap;
    private final Map<String, Account> accountMap;

    private static Bank bankInstance;

    private Bank() {
        customerMap = new HashMap<>();
        accountMap  = new HashMap<>();
    }

    public static synchronized Bank getBankInstance() {
        if (bankInstance == null) {
            bankInstance = new Bank();
        }
        return bankInstance;
    }

    public synchronized Customer createCustomer(String customerName, String customerId, Date registrationDate)
            throws BankException {
        //check if customerId exist
        if(customerMap.containsKey(customerId)){
            throw new BankException("Customer with ID"+ customerId + "already exists" );
        }
        //otherwise put thia nww info into customerMap
        Customer newCustomer = new Customer(customerName, customerId, registrationDate);
        customerMap.put(customerId, newCustomer);
        return newCustomer;
    }

    public synchronized Customer lookupCustomer(String customerId)

            throws BankException {
        //process check if this customerId not in key
        if(!customerMap.containsKey(customerId)){
            throw new BankException("Customer with ID" + customerId +"does not exist");
        }
        //grab the customer match the customerId
        return customerMap.get(customerId);
    }

    public synchronized Account createAccount(String customerId, AccountType accountType,
                                              String accountId, Date openDate, int initialAmount)
            throws BankException {
        // Check if Account exists
        if (accountMap.containsKey(accountId)){
            throw new BankException("Account with ID " + accountId + " does not exist");
        }

        Account newAccount;
        if (accountType == AccountType.Checking) {
            newAccount = new CheckingAccount(lookupCustomer(customerId), accountId, openDate, initialAmount);
        } else if (accountType == AccountType.Savings){
            newAccount = new SavingsAccount(lookupCustomer(customerId), accountId, openDate, initialAmount);
        } else {
            throw new BankException("Unsupported account type");
        }

        accountMap.put(accountId, newAccount);

        return newAccount;
    }


    public synchronized Account lookupAccount(String accountId)
        throws BankException {
            //process check if account is in key
        if(!accountMap.containsKey(accountId)){
            throw new BankException("Account with ID" + accountId + "is not exist");
        }
        return accountMap.get(accountId);
    }

    public synchronized void setJointOwner(String accountId, String primaryOwnerId,
                                           String jointOwnerId, Date jointOwnershipDate) {

        Account account = this.lookupAccount(accountId);
        Customer primaryOwner = this.lookupCustomer(primaryOwnerId);
        Customer jointOwner   = this.lookupCustomer(jointOwnerId);

        if ((account != null) && (primaryOwner != null) && (jointOwner != null)) {
            if ( (account.getPrimaryOwner() == primaryOwner) && (account.getJointOwner() == null) ) {
                account.setJointOwner(jointOwner, jointOwnershipDate);
            }
        }


    }

    public synchronized void createTransaction(TransactionType transactionType, Date date, int amount,
                                               String customerId, String sourceAccountId,
                                               String destinationAccountId) {

        /*
         - Based on the transactionType, invoke makeDeposit(...), or makeWithdrawal(...), or makeTransfer(...)
         - The destinationAccountId is only applicable if this is a Transfer request
         */
        switch (transactionType) {

            case Deposit -> {
                makeDeposit(date,amount,customerId,sourceAccountId);

                break;
            }
            case Withdraw -> {
                makeWithdrawal(date,amount,customerId,sourceAccountId);

                break;
            }
            case Transfer -> {
                makeTransfer(date,amount,customerId,sourceAccountId,destinationAccountId);


            }
        }
    }

    private synchronized void makeDeposit(Date date, int amount, String customerId, String accountId) {
        Customer customer = this.lookupCustomer(customerId);
        Account account = this.lookupAccount(accountId);
        new DepositTransaction(date, amount, customer, account);
    }

    private synchronized void makeWithdrawal(Date date, int amount, String customerId, String accountId) {
        Customer customer = this.lookupCustomer(customerId);
        Account account = this.lookupAccount(accountId);
        if (account != null &&
                ((account.getPrimaryOwner() == customer) ||
                        (account.getJointOwner() != null && account.getJointOwner() == customer)) ) {
            new WithdrawTransaction(date, amount, customer, account);
        } else
            new BankException("Customer is not owner or joint owner");
    }

    private synchronized void makeTransfer(Date date, int amount, String customerId, String fromAccountId, String toAccountId) {
        Customer customer = this.lookupCustomer(customerId);
        Account fromAccount = this.lookupAccount(fromAccountId);
        Account toAccount = this.lookupAccount(toAccountId);
        if (fromAccount != null &&
                ((fromAccount.getPrimaryOwner() == customer) ||
                        (fromAccount.getJointOwner() != null && fromAccount.getJointOwner() == customer)) ){
            new TransferTransaction(date, amount, customer, fromAccount, toAccount);
        } else
            new BankException("Customer is not owner or joint owner");
    }

    public synchronized void printStatement(String customerId, Date toDate) {
        Customer customer = this.lookupCustomer(customerId);
        if (customer != null)
         customer.printStatement(toDate);
    }

}
