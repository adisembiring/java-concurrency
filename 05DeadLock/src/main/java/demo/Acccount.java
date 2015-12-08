package demo;
/**
 * Created by adisembiring on 12/8/2015.
 */
public class Acccount {
    public int balance = 1000;

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public static void transfer(Acccount acc1, Acccount acc2, int amount) {
        acc1.withdraw(amount);
        acc2.deposit(amount);
    }

    public int getBalance() {
        return balance;
    }
}
