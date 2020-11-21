import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class PracticeM11Tester {

	public static void main(String[] args) {
		List<Client> clientList = new ArrayList<Client>();
		fillList(clientList);
		// see a sample of the list
		for(Client client: clientList) {
			System.out.println(client);
		}
		
		
		// Stream supplier
		Supplier<Stream<Client>> streamSupplier = ( () -> {
			return clientList.stream();
		});
		// Average age of the clients
		IntSummaryStatistics summary = streamSupplier.get().
										collect(Collectors.summarizingInt(client -> {
											return client.getAge();
										}));
		int averageAge = (int)summary.getAverage();
		System.out.println("Average age of all the clients: " + averageAge);
		
		// Clients who have not spent any money
		System.out.println("\nName of clients who have not spend any money: ");
		streamSupplier.get()
				.filter(client -> client.getOrders().size() == 0)
				.map( client -> client.getFirstName() + " " + client.getLastName())
				.forEach(System.out::println);
		// Map that contains key: state, value: list of clients in each state
		Map<String, List<Client>> clientStateMap = streamSupplier.get()
				.collect(Collectors.groupingBy(client -> client.getAddress().getState()));
		
		// States where the clients are located
		System.out.println("States where the clients exist: " + clientStateMap.keySet());
		
		// Clients in New York
		System.out.println("Clients in New York: ");
		streamSupplier.get()
		.filter(client -> client.getAddress().getState().equalsIgnoreCase("NY"))
							.forEach(client -> System.out.println(client.getFirstName() + " " + client.getLastName()));
				
	}

	private static void fillList(List<Client> clientList) {
		try (Scanner clientFileScan = new Scanner(new FileReader(new File("/Users/prakash/eclipse-workspace/Streams/bin/ClientData.csv")));
				Scanner orderFileScan = new Scanner(new FileReader(new File("/Users/prakash/eclipse-workspace/Streams/bin/OrderData.csv")))) {

			/* create a list of orders */
			List<Order> orderList = new ArrayList<Order>();
			while (orderFileScan.hasNext()) {
				String orderLine = orderFileScan.nextLine();
				Scanner orderLineScan = new Scanner(orderLine);
				orderLineScan.useDelimiter(",");
				String allOrderString = orderLineScan.next();
				double total = Double.parseDouble(orderLineScan.next());
				
				List<String> itemList = new ArrayList<String>();
				Scanner allOrderStringScan = new Scanner(allOrderString);
				allOrderStringScan.useDelimiter(";");
				while (allOrderStringScan.hasNext()) {
					itemList.add(allOrderStringScan.next());
				}
				Order order = new Order(itemList, total);
				orderList.add(order);
			}
			int orderNum = 0;

			while (clientFileScan.hasNext()) {
				String clientLine = clientFileScan.nextLine();
				Scanner clientLineScan = new Scanner(clientLine);
				clientLineScan.useDelimiter(",");
				String firstName = clientLineScan.next();
				String lastName = clientLineScan.next();
				int age = Integer.parseInt(clientLineScan.next());
				String genderString = clientLineScan.next();
				Gender gender;
				if (genderString.equalsIgnoreCase("M")) {
					gender = Gender.MALE;
				} else if (genderString.equalsIgnoreCase("F")) {
					gender = Gender.FEMALE;
				} else {
					gender = Gender.OTHER_OR_UNSPECIFIED;
				}
				String streetNumber = clientLineScan.next();
				String street = clientLineScan.next();
				String city = clientLineScan.next();
				String state = clientLineScan.next();
				String zip = clientLineScan.next();
				int numOrders = Integer.parseInt(clientLineScan.next());
				Client c = new Client(firstName, lastName, age, gender,
						new Address(streetNumber, street, city, state, zip));
				clientList.add(c);
				for (int i = 0; i < numOrders; i++) {
					c.addOrder(orderList.get(orderNum));
					orderNum = (orderNum + 1) % orderList.size();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
}
