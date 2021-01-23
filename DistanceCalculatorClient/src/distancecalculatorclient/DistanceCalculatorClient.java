package distancecalculatorclient;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import distancecalculator.spring.DistanceCalculatorBean;

public class DistanceCalculatorClient {

	/**
	 * @param args
	 */
	public String type;
	public String name;
    public double longitude;
    public double latitude;
    
    public DistanceCalculatorClient(String type, String name, String latitude, String longitude) {
    	this.type = type;
        this.name = name;
        // do validation - optional
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
    }

	public static void main(String[] args) {
		
		DecimalFormat df = new DecimalFormat("0.00");
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		DistanceCalculatorBean distanceCalculatorBean = (DistanceCalculatorBean) context.getBean("distanceCalculatorBean");

		Scanner scanner = new Scanner(System.in);
        double lat2 = 0;
        double lon2 = 0;
        char unit = 'K';
        
        ArrayList<DistanceCalculatorClient> shopArrayList = new ArrayList<>();
        try {
            File myObj = new File("shop list.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String x = myReader.nextLine();
           // example
              List<String> data = new ArrayList<>();
              data = Arrays.asList(x.split(","));
              // do validation - optional
              shopArrayList.add(new DistanceCalculatorClient(data.get(0), data.get(1), data.get(2), data.get(3)));
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
		catch (Exception e) {
			System.out.println("could not find file");
		}
        System.out.println("Enter you current latitude: (3.0 - 3.5)");
        double lat1 = scanner.nextDouble();
        System.out.println("Enter you current longitude: (100 - 100.3)");
        double lon1 = scanner.nextDouble();
        System.out.println("Enter the type of shop you want to go (Barber, Groceries, Laundry, Restaurant, Workshop)");
        String destination = scanner.next();
        ArrayList<Double> distances = new ArrayList<>();
        double max = Integer.MAX_VALUE;
        String nearest = "";
        for(DistanceCalculatorClient i : shopArrayList) {	
        	if (i.type.equalsIgnoreCase(destination)) {
        		lat2 = i.latitude;
        		lon2 = i.longitude;
        		double dist = distanceCalculatorBean.distance(lat1, lon1, lat2, lon2, unit);
        		distances.add(dist);
                System.out.println("The distance from your current location to "+ i.name + " is " + df.format(dist)+" km");
                for (int j =0; j<distances.size(); j++) {
                    if (distances.get(j)<max) {
                        max=distances.get(j);
                        nearest = i.name;
                    }
                }
        	}
        }
        System.out.println("Nearest is " + nearest);
	}
}