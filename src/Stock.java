import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by Justin on 4/30/2015.
 */
public class Stock {
    String symbol;
    Double price;
    ArrayList<Double> historicalPrice = new ArrayList<Double>();
    ArrayList<Integer> historicalVolume = new ArrayList<Integer>();

    public void printInfo() {
        DecimalFormat twodp = new DecimalFormat("#.00");
        System.out.println("Ticker: " + symbol);
        System.out.println("Price: " + twodp.format(price));
        System.out.println("Daily Change: " + twodp.format(getDailyChange()) + "%");
        System.out.println("Weekly Change: " + twodp.format(getWeeklyChange()) + "%");
        System.out.println("Monthly Change: " + twodp.format(getMonthlyChange()) + "%");
    }

    private double getDailyChange() {
        Double dailyChange = 100*((price-historicalPrice.get(1))/(historicalPrice.get(1)));
        return dailyChange;
    }

    private double getWeeklyChange() {
        //where 1 week is defined as 5 trading days
        Double weeklyChange = 100*((price-historicalPrice.get(5))/historicalPrice.get(5));
        return weeklyChange;
    }

    private double getMonthlyChange() {
        //where 1 month is defined as 22 trading days
        Double monthlyChange = 100*((price-historicalPrice.get(22))/historicalPrice.get(22));
        return monthlyChange;
    }

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