package in.manishsingh.techpost.sorting;

import java.util.Scanner;

public class quickSort {

	public static void main(String[] args) {

		System.out.println("Quick Sort Algorithm\n\n");
		
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

		quickSortexecute(numArray, input_size);
		printSortedArray(numArray, input_size);
		printComplexity(input_size);

	}

	static void quickSortexecute(int[] numArray, int size) {

		quickSorting(numArray, 0, size - 1);
	}

	static void quickSorting(int[] numArray, int start, int end) {

		if (start == end || start > end) {
			return;
		}

		int pivot = numArray[start];
		int leftPointer = start + 1;
		int rightPointer = end;
		int tmp = 0;

		while (leftPointer <= rightPointer) {

			if (numArray[leftPointer] < pivot) {
				
				leftPointer++;

			} else {

				if (numArray[rightPointer] >= pivot) {
					rightPointer--;
				} else {

					tmp = numArray[leftPointer];
					numArray[leftPointer] = numArray[rightPointer];
					numArray[rightPointer] = tmp;
					leftPointer++;
					rightPointer--;

				}

			}
		}

		if (rightPointer != start) {

			if (leftPointer - 1 == rightPointer || leftPointer == rightPointer) {

				numArray[start] = numArray[rightPointer];
				numArray[rightPointer] = pivot;

			}

			quickSorting(numArray, start, rightPointer - 1);

		}

		quickSorting(numArray, rightPointer + 1, end);

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
		System.out.println("Worst case: " + (size * size));
		System.out.println("\n\nSpace complexity of this algorithm is: 1");

	}

}
