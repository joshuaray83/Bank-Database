// Name         : Josh Heyer
// Class        : CSCI 1620-301
// Program #    : 6
// Due Date     : 7/5/2016
//
// Honor Pledge : On my honor as a student of the University
//                of Nebraska at Omaha, I have neither given nor received
//                unauthorized help on this homework assignment.
//
// Name         : Josh Heyer
// NUID         : 59530380
// Email        : jheyer@unomaha.edu
// Partners     : None
//
// Class holds an array of AccountRecordSerializable objects and has
// members methods for reading these records, writing array contents,
// reading from a transaction file and updating account info, printing
// accound, sorting accounts.
import java.util.Scanner;
import java.util.Formatter;
import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.util.FormatterClosedException;
import java.io.EOFException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;

public class AccountManager {

    private AccountRecordSerializable[] accounts;
    private int numRecords;
    private final int MAX_RECORDS = 100;

// Method Name      : AccountManager
// Parameters       : None
// Return value(s)  : None
// Partners         : N/A
// Description      : Initializes accounts array and numRecords to 0.
    public AccountManager () {

        accounts = new AccountRecordSerializable[MAX_RECORDS];
        numRecords = 0;
    }

// Method Name      : readRecords
// Parameters       : file:String
// Return value(s)  : None
// Partners         : N/A
// Description      : String passed is a .ser file. If file doesn't
// exist, message "No Existing Records." Otherwise, read objects.
    public void readRecords (String file) {

        int x = 0;
        AccountRecordSerializable temp;
        ObjectInputStream OIS = null;
        try {
            FileInputStream FIS = new FileInputStream(file);
            OIS = new ObjectInputStream(FIS);
            while (true) {
                temp = (AccountRecordSerializable)OIS.readObject();
                accounts[x] = temp;
                numRecords++;
                x++;
            }
        }
        catch (FileNotFoundException FNFE) {
            System.out.println ("No Existing Records.");
        }
        catch (IOException IOE) {
            /*System.err.println ("Error Reading File.");
            System.exit(1);*/
        }
        catch (ClassNotFoundException CNFE) {
            System.err.println ("Class Not Found.");
        }
        finally {
            if (OIS != null) {
                try{    
                    OIS.close();
                }
                catch (IOException IOE) {
                    System.out.println ("Error Closing OIS.");
                    System.exit(1);
                }
            }
        }
    }

// Method Name      : writeRecords
// Parameters       : file:String
// Return value(s)  : None
// Partners         : N/A
// Description      : String passed is .ser file. All objects written to file
    public void writeRecords (String file) {

        try {
            FileOutputStream FOS = new FileOutputStream(file);
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            for (int x = 0; x < numRecords; x++) {
                OOS.writeObject(accounts[x]);
            }
            OOS.close();
        }
        catch (IOException IOE) {
            System.out.println ("Error writing to file/closing OOS.");
            System.exit(1);
        }
    }

// Method Name      : printRecords
// Parameters       : None
// Return value(s)  : None
// Partners         : N/A
// Description      : Prints formatted header and all of current records
// in the account array in sorted order.
    public void printRecords () {

        sortRecords();
        System.out.println ("Account   First Name  Last Name        Balance");
        for (int x = 0; x < numRecords; x++) {
            System.out.printf ("%s", accounts[x].toString());
        }
    }

// Method Name      : addRecord
// Parameters       : accountNumber:int, balance:double
// Return value(s)  : None
// Partners         : N/A
// Description      : Prints message stating new account # found and prompts
// user for new account's first/last name, creates new account and adds
// it to array, then prints out the message "New account added: "
// and prints new account.
    public void addRecord (int accountNumber, double balance) {

        if (balance >= 0) {
            String fn; String ln;
            Scanner input = new Scanner(System.in);
            System.out.printf ("New account number %d found!\n", accountNumber);
            System.out.print ("Enter First Name: ");
            fn = input.next();
            System.out.print ("Enter Last Name: ");
            ln = input.next();
            accounts[numRecords] = new AccountRecordSerializable();
            accounts[numRecords].setAccount(accountNumber);
            accounts[numRecords].setBalance(balance);
            accounts[numRecords].setFirstName(fn);
            accounts[numRecords].setLastName(ln);
            System.out.printf ("New account added:\n%s\n", accounts[numRecords].toString());
            numRecords++;
        }
    }

// Method Name      : sortRecords
// Parameters       : None
// Return value(s)  : None
// Partners         : N/A
// Description      : Sorts all records by account #.
    public void sortRecords () {

        AccountRecordSerializable temp = new AccountRecordSerializable();
        for (int x = 0; x < numRecords; x++) {
            for (int y = 0; y < numRecords; y++) {
                if (accounts[x].getAccount() < accounts[y].getAccount()) {
                    temp = accounts[y];
                    accounts[y] = accounts[x];
                    accounts[x] = temp;
                }
            }
        }
    }

// Method Name      : findRecord
// Parameters       : accountNumber:int
// Return value(s)  : int
// Partners         : N/A
// Description      : Attempts to find a given account. Returns position.
    public int findRecord (int accountNumber) {

        int pos = -1;
        for (int x = 0; x < numRecords; x++) {
            if (accounts[x].getAccount() == accountNumber) {
                pos = x;
            }
        }
        return pos;
    }

// Method Name      : updateTransactions
// Parameters       : file:String
// Return value(s)  : None
// Partners         : N/A
// Description      : String pass is .txt file. Data read and appropriate
// accound are adjusted or added.
    public void updateTransactions (String file) {

        try {
            File file1 = new File(file);
            Scanner input = new Scanner(file1);
            while (input.hasNext()) {
                int acct = input.nextInt();
                double bal = input.nextDouble();
                if (findRecord(acct) == -1) {
                    addRecord(acct, bal);
                }
                else {
                    accounts[findRecord(acct)].addBalance(bal);
                }
            }
            input.close();
        }
        catch (FileNotFoundException FNF) {
            System.out.println ("Error updating transactions.");
            System.exit(1);
        }
    }
}
