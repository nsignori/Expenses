package budget;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class ActualGraphGUI extends GUI {
	int numOfDays = 0;
	ArrayList<Purchase> prices = null;


	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	
	private LineChart<String, Number> lineGraph = new LineChart<String,Number>(xAxis,yAxis);
	private PieChart pieGraph = new PieChart();

	public ActualGraphGUI(String view) {
		super(view, 900, 600, false);

		switch(view) {
		case "Week":
			numOfDays = 7;
			lineGraph.setTitle("Money Spent in the Last 7 Days");
			pieGraph.setTitle("Money Spent by Category in the Last 7 Days");
			break;
		case "Month":
			numOfDays = 31;
			lineGraph.setTitle("Money Spent in the Last 31 Days");
			pieGraph.setTitle("Money Spent by Category in the Last 31 Days");
			break;
		case "Year":
			numOfDays = 365;
			lineGraph.setTitle("Money Spent in the Last 365 Days");
			pieGraph.setTitle("Money Spent by Category in the Last 365 Days");
			break;
		case "All Time":
			numOfDays = Integer.MAX_VALUE;
			lineGraph.setTitle("All Money Spent");
			pieGraph.setTitle("All Money Spent by Category");
			break;
		default:
			System.out.println("Unknown case: " + view);
			break;
		}
		
		prices = Main.getPurchasesSince(numOfDays);
		prices.sort(new ComparatorDate());
		
		makePieChart();
		makeLineChart();

		gpMain.add(lineGraph, 0, 0);
		gpMain.add(pieGraph, 0, 1);

	}

	private void makePieChart() {
		pieGraph.setPrefWidth(850);

		HashMap<String, Double> catPrices = new HashMap<String, Double>();

		for(int i = 0; i < prices.size(); i++) {
			if(catPrices.containsKey(prices.get(i).getCategory())) {
				catPrices.replace(prices.get(i).getCategory(), catPrices.get(prices.get(i).getCategory()) + prices.get(i).getPrice());
			} else {
				catPrices.put(prices.get(i).getCategory(), prices.get(i).getPrice());
			}
		}

		String[] keys = catPrices.keySet().toArray(new String[catPrices.size()]);
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

		for(int i = 0; i < keys.length; i++) {
			data.add(new PieChart.Data(keys[i], catPrices.get(keys[i])));
		}

		pieGraph.getData().addAll(data);
	}

	private void makeLineChart() {
	lineGraph.setPrefWidth(850);
	
		ArrayList<Purchase> tempPrices = new ArrayList<Purchase>();
		while(prices.size() > 0) {
			LocalDate date = prices.get(0).getDate();
			double curPrice = prices.get(0).getPrice();

			for(int i = prices.size() - 1; i > 0; i--) {
				if(numOfDays <= 40) {
					if(prices.get(i).getDate().equals(date)) {
						curPrice += prices.get(i).getPrice();
						prices.remove(i);
					}
				} else {
					if(prices.get(i).getDate().getMonthValue() == date.getMonthValue() && prices.get(i).getDate().getYear() == date.getYear()) {
						curPrice += prices.get(i).getPrice();
						prices.remove(i);
					}
				}
			}
			prices.remove(0);

			tempPrices.add(new Purchase(date, null, curPrice));
		}
		prices = tempPrices;

		Series<String, Number> data = new XYChart.Series<String, Number>();
		data.setName("Spending");

		for(int i = 0; i < prices.size(); i++) {
			if(numOfDays <= 7) {
				data.getData().add(new Data<String, Number>(prices.get(i).getDate().getDayOfWeek().toString(), prices.get(i).getPrice()));
			} else if(numOfDays > 40) {
				data.getData().add(new Data<String, Number>(prices.get(i).getDate().getMonth().toString() + " " + prices.get(i).getDate().getYear(), prices.get(i).getPrice()));
			} else {
				data.getData().add(new Data<String, Number>(prices.get(i).getDate().getMonthValue() + "/" + prices.get(i).getDate().getDayOfMonth(), prices.get(i).getPrice()));
			}
		}

		((XYChart<String, Number>) lineGraph).getData().add(data);
	}
}
