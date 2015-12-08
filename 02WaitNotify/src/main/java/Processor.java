import java.util.Scanner;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class Processor {
    public void produce() throws InterruptedException {
        Thread.sleep(2000);
        synchronized (this) {
            System.out.println("Producer thread running");
            wait();
            System.out.println("Producer resumed");
        }
    }

    public void consume() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Thread.sleep(2000);
        synchronized (this) {
            System.out.println("Waiting for return key");
            scanner.nextLine();
            System.out.println("Return key pressed");
            notify();
            Thread.sleep(5000);
            System.out.println("consumer Sleeping");
        }
    }

}
