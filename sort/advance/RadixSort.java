package in.manishsingh.techpost.sorting;

import java.util.Scanner;

public class RadixSort {
	
	static int wordSize=0;

	public static void main(String[] args) {

		System.out.println("Radix sort algorithm\n\n");

		Scanner in = new Scanner(System.in);
		System.out.print("Enter the number of elements need to be sorted: ");
		int input_size = Integer.parseInt(in.nextLine());

		int[] numArray = new int[input_size];

		int counter = 0;

		while (counter != input_size) {
			System.out.print("\nEnter the number for position " + (counter + 1) + ": ");
			numArray[counter] = Integer.parseInt(in.nextLine());
			counter++;
		}

		in.close();

		radixSortexecute(numArray, input_size);
		printSortedArray(numArray, input_size);
		printComplexity(input_size);

	}

	static void radixSortexecute(int[] numArray, int size) {

		int endSort = 0;
		int multiplier = 1;
		int flag = 0;
		int tmp = 0;
		int leftDigit = 0, rightDigit = 0;

		

		while (endSort == 0) {

			//Flag to exit while
			flag = 0;
			wordSize++;
			
			//Sort the numbers

			for (int i = 1; i < size; i++) {

				rightDigit = (numArray[i] / multiplier) % 10;

				for (int j = 0; j < i; j++) {

					leftDigit = (numArray[j] / multiplier) % 10;

					if (leftDigit > 0 || rightDigit > 0) {

						flag = 1;
						

					}
					
					//Swap the numbers

					if (leftDigit > rightDigit) {

						tmp = numArray[j];
						numArray[j] = numArray[i];
						numArray[i] = tmp;

					}

				}

			}

			if (flag == 0) {
				endSort = 1;
			}

			multiplier = multiplier * 10;

		}
		wordSize--;
		

	}

	static void printSortedArray(int[] numArray, int size) {

		System.out.print("\n\nSorted Array is: ");

		for (int i = 0; i < size; i++) {
			System.out.print(numArray[i] + " ");
		}

	}

	static void printComplexity(int size) {

		System.out.println("\n\n\nTime complexity of this algorithm is:\n");
		System.out.println("Best case: " + (size+wordSize));
		System.out.println("Avergae case: " + (size*wordSize));
		System.out.println("Worst case: " + (size*wordSize));
		System.out.println("\n\nSpace complexity of this algorithm is: " + (size+wordSize));

	}

}