import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Justin on 4/30/2015.
 */
public class main {
    public static void main (String[] args) throws IOException {
        updateList updateList = new updateList();
        updateList.updateLocalFile();
        File NASDAQ = new File("NASDAQ.txt");
        getData(NASDAQ);
    }

    private static void getData(File list) {

    }
}
