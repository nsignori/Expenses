package budget;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class ActualGraphGUI extends GUI {
	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	private LineChart<String,Number> graph = new LineChart<String,Number>(xAxis,yAxis);;

	private String view;
	private boolean byCategory;

	public ActualGraphGUI(String view, boolean byCategory) {
		super(view, 900, 600, false);

		this.view = view;
		this.byCategory = byCategory;

		updateGraph();
		gpMain.add(graph, 0, 0);
	}

	private void updateGraph() {
		int numOfDays = 0;
		HashMap<String, Double> prices = null;
		ArrayList<CatPurchase> catPrices = null;

		graph.setPrefWidth(850);

		switch(view) {
		case "Week":
			numOfDays = 7;
			xAxis.setLabel("Day of Week");
			graph.setTitle("Purchases in the Last 7 Days");
			break;
		case "Month":
			numOfDays = 31;
			xAxis.setLabel("Day");
			graph.setTitle("Purchases in the Last 31 Days");
			break;
		case "Year":
			numOfDays = 365;
			xAxis.setLabel("Month");
			graph.setTitle("Purchases in the Last 365 Days");
			break;
		default:
			System.out.println("Unknown case: " + view);
			break;
		}

		if(byCategory) {
			catPrices = Main.getCatPurchasesSince(numOfDays);
		} else {
			prices = Main.getPurchasesSince(numOfDays);
		}

		if(byCategory) {
			for(int i = 0; i < catPrices.size(); i++) {
				Series<String, Number> data = new XYChart.Series<String, Number>();

				data.setName(catPrices.get(i).getCategory());
				HashMap<String, Double> tempPrices = catPrices.get(i).getPrices();

				if(numOfDays <= 7) {
					List<String> sortedKeys=new ArrayList<String>(tempPrices.keySet());
					Collections.sort(sortedKeys);
					for(int k = 0; k < tempPrices.size(); k++) {
						data.getData().add(new Data<String, Number>(LocalDate.parse(sortedKeys.get(k)).getDayOfWeek().toString(), tempPrices.get(sortedKeys.get(k))));
					}
				} else if(numOfDays > 40) {
					HashMap<String, Double> temp = new HashMap<String, Double>();

					for(int k = 0; k < tempPrices.keySet().toArray().length; k++) {
						if(temp.containsKey(tempPrices.keySet().toArray()[k].toString().substring(0, 7))) {
							temp.put(tempPrices.keySet().toArray()[k].toString().substring(0, 7), tempPrices.get(tempPrices.keySet().toArray()[k]) + temp.get(tempPrices.keySet().toArray()[k].toString().substring(0, 7)));
						} else {
							temp.put(tempPrices.keySet().toArray()[k].toString().substring(0, 7), tempPrices.get(tempPrices.keySet().toArray()[k]));
						}
					}
					tempPrices = temp;
					
					List<String> sortedKeys=new ArrayList<String>(tempPrices.keySet());
					Collections.sort(sortedKeys);
					for(int k = 0; k < tempPrices.size(); k++) {
						data.getData().add(new Data<String, Number>(new DateFormatSymbols().getMonths()[Integer.parseInt(sortedKeys.get(k).substring(5)) - 1], tempPrices.get(sortedKeys.get(k))));
					}
				} else {
					List<String> sortedKeys=new ArrayList<String>(tempPrices.keySet());
					Collections.sort(sortedKeys);
					for(int k = 0; k < tempPrices.size(); k++) {
						data.getData().add(new Data<String, Number>(LocalDate.parse(sortedKeys.get(k)).getMonthValue() + "/" + LocalDate.parse(sortedKeys.get(k)).getDayOfMonth(), tempPrices.get(sortedKeys.get(k))));
					}
				}

				graph.getData().add(data);
			}
		} else {
			Series<String, Number> data = new XYChart.Series<String, Number>();
			data.setName("Spending");

			if(numOfDays <= 7) {
				List<String> sortedKeys=new ArrayList<String>(prices.keySet());
				Collections.sort(sortedKeys);
				for(int i = 0; i < prices.size(); i++) {
					data.getData().add(new Data<String, Number>(LocalDate.parse(sortedKeys.get(i)).getDayOfWeek().toString(), prices.get(sortedKeys.get(i))));
				}
			} else if(numOfDays > 40) {
				HashMap<String, Double> temp = new HashMap<String, Double>();

				for(int i = 0; i < prices.keySet().toArray().length; i++) {
					if(temp.containsKey(prices.keySet().toArray()[i].toString().substring(0, 7))) {
						temp.put(prices.keySet().toArray()[i].toString().substring(0, 7), prices.get(prices.keySet().toArray()[i]) + temp.get(prices.keySet().toArray()[i].toString().substring(0, 7)));
					} else {
						temp.put(prices.keySet().toArray()[i].toString().substring(0, 7), prices.get(prices.keySet().toArray()[i]));
					}
				}
				prices = temp;
				
				List<String> sortedKeys=new ArrayList<String>(prices.keySet());
				Collections.sort(sortedKeys);
				for(int i = 0; i < prices.size(); i++) {
					data.getData().add(new Data<String, Number>(new DateFormatSymbols().getMonths()[Integer.parseInt(sortedKeys.get(i).substring(5)) - 1], prices.get(sortedKeys.get(i))));
				}
			} else {
				List<String> sortedKeys=new ArrayList<String>(prices.keySet());
				Collections.sort(sortedKeys);
				for(int i = 0; i < prices.size(); i++) {
					data.getData().add(new Data<String, Number>(LocalDate.parse(sortedKeys.get(i)).getMonthValue() + "/" + LocalDate.parse(sortedKeys.get(i)).getDayOfMonth(), prices.get(sortedKeys.get(i))));
				}
			}

			graph.getData().add(data);
		}
	}
}
