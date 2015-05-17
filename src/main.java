import org.json.JSONException;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Justin on 4/30/2015.
 */
public class main {
    public static void main (String[] args) throws IOException, JSONException, InterruptedException {
        System.out.println("stock-picker written by Justin Chau, v0.0.3");
        Date date = new Date();
        System.out.println(date.toString());
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Enter ticker symbol below, or type \"update\" to update the list of valid NASDAQ tickers. Type help for additional commands.");
            String input = scanner.nextLine();
            if (input.equals("help")) {
                System.out.println("Additional commands:");
                System.out.println("input - description");
                System.out.println("(1). crossover - Recommends stocks that satisfy the Moving Average Crossover condition");
                System.out.println("(0). exit - Exit program");
                continue;
            }
            if (input.equals("update")) {
                System.out.println("Updating list...");
                updateNASDAQList();
                System.out.println("List updated.");
            } else if (input.equals("crossover")) {
                File NASDAQ = new File("NASDAQ.txt");
                BlockingQueue listofStocks = new ArrayBlockingQueue(1024);
                listPopulator listPopulator = new listPopulator(listofStocks, NASDAQ);
                DMA[] DMAs = new DMA[25];
                Thread[] DMAThreads = new Thread[DMAs.length];
                for (int i = 0; i < DMAs.length; i++) {
                    DMAs[i] = new DMA(listofStocks);
                    DMAThreads[i] = new Thread(DMAs[i]);
                    DMAThreads[i].start();
                }
                Thread lpThread = new Thread(listPopulator);
                lpThread.start();

                lpThread.join();
                for (int i = 0; i < DMAs.length; i++) {
                    DMAThreads[i].join();
                }

                for (int i = 0; i < DMAs.length; i++) {
                    for (int p = 0; p < DMAs[i].getStocks().size(); p++) {
                        DMAs[i].getStocks().get(p).printInfo();
                    }
                }
                Date date2 = new Date();
                System.out.println(date2.toString());
            }
            else if (input.equals("exit")) {
                System.exit(0);
            }
            else {
                DecimalFormat twodp = new DecimalFormat("#.00");
                stockParser sp = new stockParser();
                Stock stock;
                System.out.println("Getting information on " + input);
                System.out.println();
                try {
                    stock = sp.readJSONHistorical(input);

                    while (true) {
                        System.out.println("Specify information to show. Type \'help\' to see all available options.");
                        String input2 = scanner.nextLine();

                        if (input2.equals("help")) {
                            System.out.println("input - description");
                            System.out.println("(1). 30dma - Print out the 30-day moving average");
                            System.out.println("(2). 60dma - Print out the 60-day moving average");
                            System.out.println("(3). 120dma - Print out the 120-day moving average");
                            System.out.println("(4). price - Show price information");
                            System.out.println("(9). back - Go back to stock input menu");
                            System.out.println("(0). exit - Exit program");
                            continue;
                        }
                        else if (input2.equals("price")) {
                            stock.printInfo();
                            System.out.println();
                            continue;
                        } else if (input2.equals("30dma")) {
                            DMA tDMA = new DMA();
                            try {
                                System.out.println(twodp.format(tDMA.calculate30(stock.historicalPrice)));
                            } catch (NullPointerException e) {
                                continue;
                            }
                            continue;
                        } else if (input2.equals("60dma")) {
                            DMA tDMA = new DMA();
                            try {
                                System.out.println(twodp.format(tDMA.calculate60(stock.historicalPrice)));
                            } catch (NullPointerException e) {
                                continue;
                            }
                            continue;
                        } else if (input2.equals("120dma")) {
                            DMA tDMA = new DMA();
                            try {
                                System.out.println(twodp.format(tDMA.calculate120(stock.historicalPrice)));
                            } catch (NullPointerException e) {
                                continue;
                            }
                            continue;
                        } else if (input2.equals("back")) {
                            break;
                        } else if (input2.equals("exit")) {
                            System.exit(0);
                        }
                        else {
                            System.out.println("Invalid input. Please try again, or refer to options by typing \'help\'.");
                        }

                    }
                } catch (NullPointerException ne) {
                    System.out.println("Ticker does not exist or stock has not been recently traded.");
                }
            }
        }
    }

    private static void updateNASDAQList() throws IOException {
        updateList updateList = new updateList();
        updateList.updateLocalFile();
    }


}