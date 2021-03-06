import org.json.JSONException;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Justin on 5/1/2015.
 */
public class DMA implements Runnable{
    private BlockingQueue<String> bq;
    private ArrayList<Stock> stocks;

    public DMA(BlockingQueue<String> listofStocks) {
        bq = listofStocks;
        stocks = new ArrayList<Stock>();
    }

    public DMA() {

    }

    public double calculate30(ArrayList<Double> historicalPrice) {
        Double accumulatingTotal = 0.00;
        try {
            for (int i = 0; i < 30; i++) {
                accumulatingTotal += historicalPrice.get(i);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This stock has traded for less than 30 trading sessions");
            return Double.parseDouble(null);
        }
        return accumulatingTotal/30;
    }

    public double calculate60(ArrayList<Double> historicalPrice) {
        Double accumulatingTotal = 0.00;
        try {
            for (int i = 0; i < 60; i++) {
                accumulatingTotal += historicalPrice.get(i);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This stock has traded for less than 60 trading sessions");
            return Double.parseDouble(null);
        }
        return accumulatingTotal/60;
    }

    public double calculate120(ArrayList<Double> historicalPrice) {
        Double accumulatingTotal = 0.00;
        try {
            for(int i = 0; i<120; i++) {
                accumulatingTotal += historicalPrice.get(i);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This stock has traded for less than 120 trading sessions");
            return Double.parseDouble(null);
        }

        return accumulatingTotal/120;
    }

    public Double[] calculateWeek30(ArrayList<Double> historicalPrice) {
        Double[] accumulatingTotals = new Double[7];
        try {
            for (int i = 0; i < 7; i++) {
                accumulatingTotals[i] = 0.00;
                for (int p = 0; p < 30; p++) {
                    accumulatingTotals[i] += historicalPrice.get(p+i);
                }
                accumulatingTotals[i] = accumulatingTotals[i]/30;
            }
        } catch (IndexOutOfBoundsException e) {
            //System.out.println("This stock has traded for less than 37 trading sessions");
            return null;
        }
        return accumulatingTotals;
    }

    public Double[] calculate12030(ArrayList<Double> historicalPrice) {
        Double[] accumulatingTotals = new Double[7];
        try {
            for (int i = 0; i < 7; i++) {
                accumulatingTotals[i] = 0.00;
                for (int p = 0; p < 120; p++) {
                    accumulatingTotals[i] += historicalPrice.get(p+i);
                }
                accumulatingTotals[i] = accumulatingTotals[i]/120;
            }
        } catch (IndexOutOfBoundsException e) {
            //System.out.println("This stock has traded for less than 37 trading sessions");
            return null;
        }
        return accumulatingTotals;
    }

    /**
     * <p>Takes 30DMA and 120DMA that have been tracked for 7 days of all stocks, and
     * returns a list of stocks in which the 30DMA has surpassed the 120DMA at least 5 days ago,
     * and the gap between the 30DMA and 120DMA has increased each day since the crossover</p>
     *
     * A few lines were taken from http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
     *
     * @param   symbol
     * @since 2015-05-02
     */
    public void crossover30And120(String symbol) throws IOException, JSONException {
            Stock stock;
            stockParser sp = new stockParser();
        stock = sp.readJSONHistorical(symbol);
        //calculateWeek30 and calculate12030 can be done concurrently
        Double[] sevenThirty = null;
        Double[] oneTwentyThirty = null;
        if (stock != null) {
            if (stock.historicalPrice != null) {
                sevenThirty = calculateWeek30(stock.historicalPrice);
                oneTwentyThirty = calculate12030(stock.historicalPrice);
            }
            if (sevenThirty != null && oneTwentyThirty != null) {
                if (crossoverChecker(sevenThirty, oneTwentyThirty)) {
                    stocks.add(stock);
                }
            }
        }

    }

    //thirty[4,5,6] MUST be greater than oneTwenty[4,5,6]
    private boolean crossoverChecker(Double[] thirty, Double[] oneTwenty) {
        int days = 7-1;
        //if the last day thirty[]>oneTwenty[] then there is no detectable crossover
        //we can change the numbers to accomodate larger arrays later
        if (thirty[days] > oneTwenty[days]) {
            return false;
        }
        //check if 30DMA<120DMA for the first five days, if it is then the condition is not met
        for (int i = 0; i<5; i++) {
            if (thirty[i] - oneTwenty[i] <= 0) {
                return false;
            }
        }

        //find the crossover event, and see if DMA difference has increased each day since then
        for (int i = 5; i<days; i++) {
            if (thirty[i] < oneTwenty[i]) {
                double dayAfter = 0;
                double dayBefore = 0;
                for (int p = i-2; p >= 0; p--) {
                    //don't bother checking the first day
                    if (p == i-2) {
                        continue;
                    }
                    //first day onwards
                    dayAfter = thirty[p] - oneTwenty[p];
                    if (dayAfter < dayBefore) {
                        return false;
                    }
                    dayBefore = dayAfter;
                }
                break;
            }
        }
        return true;


    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(250);
            while (!bq.isEmpty()) {
                try {
                    crossover30And120(bq.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    //e.printStackTrace();
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
