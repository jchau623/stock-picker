import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Justin on 5/1/2015.
 */
public class stockParser {

    /**
     * <p>Uses YQL to gather historical stock price for prior 365 days
     * and current information</p>
     *
     * @param input  stock symbol
     * @version 1.0
     * @since 2015-05-01
     */
    private static Stock readJSONHistorical(String input) throws IOException, JSONException {
        String ticker = input.toUpperCase();
        Stock stock = new Stock();

        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Calendar today = Calendar.getInstance();
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, 1);

        //historical:
        //block taken from Nathaniel Waisbrot, http://stackoverflow.com/questions/17494758/how-to-implement-yql-in-java/17495491#17495491
        //modified
        String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
        String hquery = "select * from yahoo.finance.historicaldata where symbol = \"" + ticker +"\" and startDate = \"" + format.format(lastYear) + "\" and endDate = \"" + format.format(today) + "\"";
        String fullUrlStr = baseUrl + URLEncoder.encode(hquery, "UTF-8") + "&diagnostics=true&env=store://datatables.org/alltableswithkeys&format=json";

        URL fullUrl = new URL(fullUrlStr);
        InputStream inputStream = fullUrl.openStream();

        JSONTokener tok = new JSONTokener(inputStream);
        JSONObject result = new JSONObject(tok);
        inputStream.close();

        //parse it
        JSONObject query = result.getJSONObject("query");
        JSONObject r = query.getJSONObject("results");
        JSONArray quotes = null;
        try {
            quotes = r.getJSONArray("quote");
        } catch (JSONException je) {
            System.out.println("Ticker not found");
            return null;
        }
        for (int i = 0; i < quotes.length(); i++) {
            JSONObject quote = quotes.getJSONObject(i);
            Double price = quote.getDouble("Close");
            stock.historicalPrice.add(price);
            int volume = quote.getInt("Volume");
            stock.historicalVolume.add(volume);
        }

        //current:
        String cquery = "select * from yahoo.finance.quotes where symbol = \"" + ticker + "\"";
        String fullUrlStr2 = baseUrl + URLEncoder.encode(cquery, "UTF-8") + "&diagnostics=true&env=store://datatables.org/alltableswithkeys&format=json";
        URL fullUrlCurrent = new URL(fullUrlStr2);
        InputStream inputStream1 = fullUrlCurrent.openStream();
        JSONTokener tok2 = new JSONTokener(inputStream1);
        JSONObject currentResult = new JSONObject(tok2);
        inputStream1.close();

        JSONObject currentResults = currentResult.getJSONObject("results");
        JSONObject currentQuote = currentResults.getJSONObject("quote");

        //treat closing price as "price"
        stock.symbol = currentQuote.getString("symbol");
        stock.price = currentQuote.getDouble("LastTradePriceOnly");

        return stock;
    }

}
