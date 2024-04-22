import enumTypes.AccountStatus;
import  exceptions.BankException;
import enumTypes.TransactionType;
import enumTypes.AccountType;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;


public class App {

    public static void main(String[] args) throws ParseException {

        System.out.println("\nSample Outputs ... Chaozheng\n");

        Bank bankApp = Bank.getBankInstance();

        String aliceId = "011-11-1111";

        bankApp.createCustomer("Alice", aliceId, getDate("June 1, 2022"));

        bankApp.createAccount(aliceId, AccountType.Checking,
                "01-001",  getDate("June 1, 2022"), 1000);

        String bobId = "022-22-2222";

        bankApp.createCustomer("Bob", bobId, getDate("June 1, 2022"));

        bankApp.createTransaction(TransactionType.Deposit, getDate("June 3, 2022"),
                200, aliceId,  "01-001", null);

        bankApp.setJointOwner("01-001", "011-11-1111", "022-22-2222",
                getDate("June 6, 2022"));

        bankApp.createTransaction(TransactionType.Withdraw, getDate("June 7, 2022"),
                2000, bobId, "01-001", null);

        String charlieId = "033-33-3333";


        bankApp.createCustomer("Charlie", charlieId,
                getDate("June 7, 2022"));

        bankApp.createAccount(charlieId, AccountType.Savings,
                "02-001",  getDate("June 7, 2022"), 3000);

        bankApp.setJointOwner("02-001", charlieId, bobId,
                getDate("June 7, 2022"));


        bankApp.printStatement("022-22-2222", getDate("June 8, 2022"));

        bankApp.createTransaction(TransactionType.Deposit, getDate("June 9, 2022"),
                100, bobId, "01-001", null);


        bankApp.createTransaction(TransactionType.Transfer, getDate("June 9, 2022"),
                700, bobId,  "02-001", "01-001");

        bankApp.createTransaction(TransactionType.Withdraw, getDate("June 10, 2022"),
                2000, bobId, "01-001", null);


        bankApp.printStatement("011-11-1111", getDate("June 15, 2022"));

        bankApp.printStatement("033-33-3333", getDate("June 15, 2022"));

    }

    private static Date getDate(String dateStr) {
        try {
            return DateFormat.getDateInstance().parse(dateStr);
        } catch (ParseException e) {
            throw new BankException("Invalid Date... " + e.getMessage());
        }

    }
}
