import java.util.Random;
import java.util.stream.*;

public class LearningStream {
	
	public static void main(String[]args) {
		
		
		// infinite stream
		Stream<Integer> randomIntegerStream = Stream.generate(
				() -> {
					Random generator = new Random();
					return generator.nextInt(91);
				});
		
		//randomIntStream.limit(10).forEach(System.out::println);
		/*
		randomIntStream.filter( (num) ->{
			return num%2==0;
		}).limit(20).forEach(System.out::println);;
		*/
		/*
		
		int max = randomIntegerStream.mapToInt(myIntegerObj -> myIntegerObj.intValue() )
							.limit(10)
							.max().getAsInt();
		
		System.out.println(max);
		*/
		
		double ave = randomIntegerStream.mapToInt( integerObject -> integerObject.intValue())
				.limit(20)
				.average().getAsDouble();
		System.out.println(ave);
		
		// infinite stream of odd positive numbers
		/*
		Stream<Integer> oddPosStream = Stream.iterate(1, 
				(num) -> {
					return num + 2;
				});
		oddPosStream.limit(10).forEach(System.out::println);
		*/
	}

}
