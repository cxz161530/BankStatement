import java.util.Date;

class CheckingAccount extends Account {

    public CheckingAccount(Customer primaryOwner, String accountId, Date openDate, int initialAmount) {
        super(primaryOwner, accountId, openDate);
        Transaction t = new DepositTransaction(openDate, initialAmount, primaryOwner, this);
    }

}
