import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Justin on 4/30/2015.
 */
public class updateList {

    private static URLConnection getFile()
    {
        try {
            URL url = new URL("ftp://ftp.nasdaqtrader.com/symboldirectory/nasdaqlisted.txt");
            URLConnection uConn = url.openConnection();
            return uConn;
        } catch (MalformedURLException m) {
            m.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public void updateLocalFile() throws IOException {
        URLConnection input = getFile();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input.getInputStream()));

        //create/overwrite text file
        PrintWriter printLines = new PrintWriter("NASDAQ.txt", "UTF-8");

        //read file line-by-line and output only the stock ticker symbol
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split("\\|");
            String ticker = s[0];
            if (!ticker.contains("ZJZZT") && !ticker.contains("ZVZZT")  && !ticker.contains("ZWZZT") && !ticker.contains("ZXZZT") && !ticker.contains("ZXYZ.A") && !ticker.contains("Symbol") && !(ticker.contains("File Creation Time"))) {
                //get rid of non-valid lines, there's probably a better way to do it
                printLines.println(ticker);
            }
        }
        printLines.close();
        reader.close();




    }
}
