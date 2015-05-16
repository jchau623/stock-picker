import java.io.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by j_ch on 5/16/15.
 */
public class listPopulator implements Runnable {
    private final BlockingQueue bq;
    private final File NASDAQ;

    public listPopulator(BlockingQueue listofStocks, File NASDAQ) {
        bq = listofStocks;
        this.NASDAQ = NASDAQ;
    }

    @Override
    public void run() {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(NASDAQ))) {
                for (String line; (line = br.readLine()) != null;) {
                    bq.put(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        System.out.println("*** DONE LIST POPULATOR ***");
    }
}
