import java.util.concurrent.Semaphore;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class Connection {
    private static Connection instance = new Connection();
    private int connections = 0;
    private Semaphore semaphore = new Semaphore(10);

    private Connection() {

    }

    public static Connection getInstance() {
        return instance;
    }

    public void connect() throws InterruptedException {
        try {
            semaphore.acquire();
            doConnect();
        } finally {
            semaphore.release();
        }
    }

    public void doConnect() throws InterruptedException {
        synchronized (this) {
            connections++;
            System.out.println("Current connections: " + connections);
        }
        Thread.sleep(2000);
        System.out.println("Connected: " + connections);
        synchronized (this) {
            connections--;
        }

    }
}
