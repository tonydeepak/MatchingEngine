package org.simulator.client;

import org.simulator.entity.StockOrder;
import org.simulator.entity.Trade;
import org.simulator.orderbook.SimpleOrderBookFactory;
import org.simulator.utility.utility.Logger;
import org.simulator.utility.utility.OrderUtility;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderMonitorClient {
    static SimpleOrderBookFactory simpleOrderBookFactory;
    static String outputPath = "C:\\MyDrive\\Software\\MatchingEngine\\src\\main\\resources\\output.csv";
    static FileWriter outputCSV;
    static String delimitter = ",";

    public static void main(String... args) throws Exception {
        //readFile("C:\\MyDrive\\Software\\MatchingEngine\\src\\main\\resources\\SampleG.csv");
        Logger.log("simulator starting");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String path;
        try {
            while ((path = reader.readLine()) != null) {
                path = path.trim();

                switch (path) {
                    case "help":
                        Logger.log("Use this tool to enter path of file and do matching internally.");
                        Logger.log("C:/test.csv");
                        Logger.log("#OrderID,Symbol,Price,Side,OrderQuantity");
                        Logger.log("Order1,0700.HK,610,Sell,20000");
                        Logger.log("Order2,0700.HK,610,Sell,10000");
                        Logger.log("Order3,0700.HK,610,Buy, 10000");
                    default: // order
                        readFile(path);
                        break;
                }
            }
            Logger.log("simulator stopping");
        } catch (Exception e) {
            Logger.log("Exception in OrderMonitorClient:" + e);
        }
    }

    public static void readFile(String path) {
        simpleOrderBookFactory = new SimpleOrderBookFactory();

        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            outputCSV = new FileWriter(outputPath);
            List<List<String>> orders = lines.skip(1).map(line -> Arrays.asList(line.split(","))).collect(Collectors.toList());
            for (List<String> order : orders) {
                StockOrder stockOrder = OrderUtility.parse(order);
                simpleOrderBookFactory.getOrderBook(stockOrder.getSymbol()).addOrder(stockOrder);
            }

            for (String key : simpleOrderBookFactory.getOrderBooks().keySet()) {
                simpleOrderBookFactory.getOrderBook(key).doInternalMatch();
            }

            outputCSV.write("#ActionType,OrderID,Symbol,Price,Side,OrderQuantity,FillPrice,FillQuantity");
            outputCSV.write(System.lineSeparator());

            for (String key : simpleOrderBookFactory.getOrderBooks().keySet()) {
                Logger.log("BOOOK: " + key);
                for (Trade trade : simpleOrderBookFactory.getOrderBook(key).getExecutions()) {
                    outputCSV.write(trade.toCSV(delimitter));
                    outputCSV.write(System.lineSeparator());
                    Logger.log(trade.toString());
                }
                Logger.log("----------------------------------------------------------------");
            }
            outputCSV.flush();
        } catch (Exception e) {
            Logger.log("Exception in parser:" + e);
        }
    }
}

