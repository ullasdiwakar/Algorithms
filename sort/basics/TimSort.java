package in.manishsingh.techpost.sorting;

import java.util.Scanner;

public class TimSort {

	public static void main(String[] args) {

		System.out.println("Tim sort algorithm\n\n");

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

		timSortexecute(numArray, input_size);
		printSortedArray(numArray, input_size);
		printComplexity(input_size);

	}

	static void timSortexecute(int[] numArray, int size) {

		int run = 32;
		int blocks = size / run;
		int tmpNum = 0;
		int blockSize = 0;
		int startBlock = 0;
		int endBlock = 0;

		for (int i = 0; i <= blocks; i++) {

			if (i == blocks) {
				blockSize = (size - i * run);
			} else {
				blockSize = run;
			}

			startBlock = (i * run);
			endBlock = blockSize + startBlock;

			for (int j = startBlock + 1; j < endBlock; j++) {
				for (int k = j; k > startBlock; k--) {

					if (numArray[k] < numArray[k - 1]) {
						tmpNum = numArray[k - 1];
						numArray[k - 1] = numArray[k];
						numArray[k] = tmpNum;
					}

				}
			}
		}

		// Sub-blocks sorting finished. Will now merge the blocks. If blocks=0 then no
		// need to merge

		if (blocks != 0) {

			int[] tmpLeftArray = null;
			int[] tmpRightArray = null;
			int sizeLeftArray = 0, sizeRightArray = 0;
			int numArrayPointer = 0;
			;
			int leftBlock = 0, rightBlock = 0;

			for (int g = 1; g <= blocks; g++) {

				numArrayPointer = 0;
				leftBlock = 0;
				rightBlock = 0;

				sizeLeftArray = g * run;
				tmpLeftArray = new int[sizeLeftArray];

				if (g == blocks) {
					sizeRightArray = size - (g * run);
					tmpRightArray = new int[(sizeRightArray)];

				} else {
					tmpRightArray = new int[run];
					sizeRightArray = run;
				}

				for (int c = 0; c < sizeLeftArray; c++) {
					tmpLeftArray[c] = numArray[numArrayPointer];
					numArrayPointer++;
				}

				for (int c = 0; c < sizeRightArray; c++) {
					tmpRightArray[c] = numArray[numArrayPointer];
					numArrayPointer++;
				}

				numArrayPointer = 0;

				while (leftBlock < sizeLeftArray && rightBlock < sizeRightArray) {

					if (tmpLeftArray[leftBlock] < tmpRightArray[rightBlock]) {
						numArray[numArrayPointer] = tmpLeftArray[leftBlock];
						leftBlock++;
					} else {
						numArray[numArrayPointer] = tmpRightArray[rightBlock];
						rightBlock++;
					}

					numArrayPointer++;

				}

				while (leftBlock < sizeLeftArray) {
					numArray[numArrayPointer] = tmpLeftArray[leftBlock];
					leftBlock++;
					numArrayPointer++;
				}

				while (rightBlock < sizeRightArray) {
					numArray[numArrayPointer] = tmpRightArray[rightBlock];
					rightBlock++;
					numArrayPointer++;
				}

			}

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
		System.out.println("Best case: " + size);
		System.out.println("Avergae case: " + (size * Math.log(size)));
		System.out.println("Worst case: " + (size * Math.log(size)));
		System.out.println("\n\nSpace complexity of this algorithm is: " + size);

	}

}
