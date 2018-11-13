package in.manishsingh.techpost;

import java.util.Scanner;

public class insertionSort {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		System.out.print("Enter the number of elements need to be sorted: ");
		int input_size = Integer.parseInt(in.nextLine());

		// in.close();
		int[] numArray = new int[input_size];

		int counter = 0;

		while (counter != input_size) {
			System.out.print("\nEnter the number for position " + (counter + 1) + ": ");
			numArray[counter] = Integer.parseInt(in.nextLine());
			counter++;
		}
		
		in.close();

		insertionSortexecute(numArray, input_size);
		printSortedArray(numArray, input_size);

	}

	static void insertionSortexecute(int[] numArray, int size) {

		int tmpNum = 0;

		for (int i = 1; i < size; i++) {
			for (int j = 0; j < i; j++) {
				if (numArray[i] < numArray[j]) {
					tmpNum = numArray[i];
					numArray[i] = numArray[j];
					numArray[j] = tmpNum;
				}
			}
		}

	}

	static void printSortedArray(int[] numArray, int size) {

		System.out.print("\n\nThe sorted array is: ");
		for (int i = 0; i < size; i++) {
			System.out.print(numArray[i] + " ");
		}

	}

}
