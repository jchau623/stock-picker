import java.util.ArrayList;

/**
 * Created by Justin on 5/1/2015.
 */
public class DMA {

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
}
