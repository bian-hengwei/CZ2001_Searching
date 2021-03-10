import java.io.IOException;
import java.util.Scanner;

// Client class for three searching algorithms
public class SearchGenome
{
	public static void main(String[] args)
	{
		// Initialize variables
		final String ANSI_RESET = "\u001B[0m";
		final String ANSI_RED   = "\u001B[31m";
		Scanner scan = new Scanner(System.in);
		FileReader fileReader = new FileReader();
		String sequence, target;
		long startTime, endTime, duration;

		// Allows multiple search for easier testing of the algorithms
		while (true)
		{
			System.out.println("=======================================");
			System.out.println("|| Genome Sequence Searching Program ||");
			System.out.println("=======================================");
			System.out.println();

			// Asks user for a valid .fna file as input
			while (true)
			{
				System.out.println("Enter the file name of the sequence to search from");
				System.out.println("Or click enter to quit");
				System.out.print("Genome sequence file (.fna): ");
				String fileName = scan.nextLine();
				if (fileName.equals(""))
				{
					System.out.println("Exiting program...");
					System.exit(0);
				}
				try
				{
					sequence = fileReader.read(fileName);
					break;
				}
				catch (IOException ioe)
				{
					System.out.println("Genome sequence file not found. Please try again.");
					System.out.println();
				}
			}
			System.out.println("Genome sequence file read successfully.");
			System.out.println();

			// Asks user to enter a target string
			System.out.println("Enter the target string that you want to search for");
			System.out.println("Or click enter to quit");
			System.out.print  ("Target string: ");
			target = scan.nextLine().toUpperCase();
			if (target.equals(""))
			{
				System.out.println("Exiting program...");
				System.exit(0);
			}
			System.out.println();
			boolean validHashSearch = target.length() <= 32;

			// Allows selection of different algorithms
			System.out.println("Select a searching algorithm:");
			System.out.println("1. Brute-force search");
			System.out.println("2. KMP search");
			// Show in red if input is larger than 32 characters
			// In which case Hash Search cannot be used
			if (validHashSearch)
			{
				System.out.println("3. Hash Search");
			}
			else
			{
				System.out.print(ANSI_RED);
				System.out.print("3. Hash Search ");
				System.out.print("(target string exceeds max target length 32)");
				System.out.println(ANSI_RESET);
			}
			System.out.println("4. Quit");
			boolean invalid;

			// Asks for a valid selection and runs the searching
			do
			{
				System.out.println();
				System.out.print("Selection: ");
				String selection = scan.nextLine();
				switch (selection)
				{
					case "1":
						startTime = System.nanoTime();
						BruteForceAlgorithm.naiveStringSearch(sequence, target);
						invalid = false;
						endTime = System.nanoTime();
						duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
						System.out.printf("BRUTEFORCE SEARCH TIME TAKEN (MS): %d \n", duration);
						break;
					case "2":
						startTime = System.nanoTime();
						KMPSearch.kmpSearch(sequence, target);
						invalid = false;
						endTime = System.nanoTime();
						duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
						System.out.printf("KMP SEARCH TIME TAKEN (MS): %d \n", duration);
						break;
					case "3":
						if (validHashSearch)
						{
							try {
								startTime = System.nanoTime();
								HashSearch.hashSearch(sequence, target);
								endTime = System.nanoTime();
								duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
								System.out.printf("HASH SEARCH TIME TAKEN (MS): %d \n", duration);
							} catch (NullPointerException exception) {
								System.out.println("According to Lab Project 1's requirement: Input file contains characters other than A, C, G, T, U which is not handled by hash search.");
							}
							
							invalid = false;
							
						}
						else
						{
							System.out.println("Invalid selection");
							invalid = true;
						}
						break;
					case "4":
						System.out.println("Exiting program...");
						System.exit(0);
					default:
						System.out.println("Invalid selection");
						invalid = true;
				}
			}
			while (invalid);

			System.out.println("Genome sequence searching done.");
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}
}