import java.util.ArrayList;


/**
 * Created by Justin on 4/30/2015.
 */
public class Stock {
    String symbol;
    Double price;
    ArrayList<Double> historicalPrice;
    ArrayList<Integer> historicalVolume;




    private double getPrice() {
        return price;
    }

    private String getSymbol() {
        return symbol;
    }

    private ArrayList<Double> getHistoricalPriceList() {
        return historicalPrice;
    }

    private ArrayList<Integer> getHistoricalVolumeList() {
        return historicalVolume;
    }

}