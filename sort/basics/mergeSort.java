package in.manishsingh.techpost.sorting;

import java.util.Scanner;

public class mergeSort {

	public static void main(String[] args) {

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

		mergeSortexecute(numArray, input_size);
		printSortedArray(numArray, input_size);
		printComplexity(input_size);

	}

	static void mergeSortexecute(int[] numArray, int size) {
		mergeSorting(numArray, 1, size);

	}

	static void mergeSorting(int[] numArray, int start, int end) {

		if ((end - start) > 1) {

			mergeSorting(numArray, start, ((end + start) / 2));

			mergeSorting(numArray, (end + start) / 2 + 1, end);

		}

		int i = (((start + end) / 2) - start) + 1;
		int j = (end - ((start + end) / 2));

		int[] tmpLeftNumArray = new int[i];
		int[] tmpRightNumArray = new int[j];

		for (int counter = 0; counter < i; counter++) {
			tmpLeftNumArray[counter] = numArray[counter + (start - 1)];
		}

		int k = ((start + end) / 2);

		for (int counter = 0; counter < j; counter++) {
			tmpRightNumArray[counter] = numArray[counter + k];
		}

		int l = start - 1;
		int n = 0, m = 0;

		while (n < i && m < j) {
			if (tmpLeftNumArray[n] < tmpRightNumArray[m]) {
				numArray[l] = tmpLeftNumArray[n];
				n++;
			} else {
				numArray[l] = tmpRightNumArray[m];
				m++;
			}
			l++;
		}

		while (n < i) {
			numArray[l] = tmpLeftNumArray[n];
			n++;
			l++;
		}

		while (m < j) {
			numArray[l] = tmpRightNumArray[m];
			m++;
			l++;
		}

	}

	static void printSortedArray(int[] numArray, int size) {

		System.out.print("\n\nSorted Array is: ");

		for (int i = 0; i < size; i++) {
			System.out.print(numArray[i] + " ");
		}

	}

	static void printComplexity(int size) {

		System.out.println("\n\n\nTime complexity of this algorithm is:\n");
		System.out.println("Best case: " + (size * Math.log(size)));
		System.out.println("Avergae case: " + (size * Math.log(size)));
		System.out.println("Worst case: " + (size * Math.log(size)));
		System.out.println("\n\nSpace complexity of this algorithm is:" + size);

	}

}
