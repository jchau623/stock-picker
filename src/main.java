import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Justin on 4/30/2015.
 */
public class main {
    public static void main (String[] args) throws IOException, JSONException {
        System.out.println("stock-picker written by Justin Chau, v0.0.1");
        //System.out.println("Enter ticker symbol below, or type \"update\" to update the list of valid NASDAQ tickers.");
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Enter ticker symbol below, or type \"update\" to update the list of valid NASDAQ tickers. Type exit to quit.");
            String input = scanner.nextLine();
            if (input.equals("update")) {
                System.out.println("Updating list...");
                updateNASDAQList();
                System.out.println("List updated.");
            }
            else if (input.equals("exit")) {
                System.exit(0);
            }
            else {
                stockParser sp = new stockParser();
                Stock stock;
                System.out.println("Getting information on " + input);
                System.out.println();
                stock = sp.readJSONHistorical(input);
                stock.printInfo();
                System.out.println();
            }
        }



        //File NASDAQ = new File("NASDAQ.txt");
        //getData(NASDAQ);
    }

    private static void updateNASDAQList() throws IOException {
        updateList updateList = new updateList();
        updateList.updateLocalFile();
    }

    private static void getData(File list) {

    }
}
