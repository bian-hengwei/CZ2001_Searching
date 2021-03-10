import java.io.IOException;

// KMP Search Class
// Time Complexity: O(m+n)
public class KMPSearch
{
	public static void main(String[] args) throws IOException
	{
		// Reads input file with FileReader helper class
		FileReader reader = new FileReader();
		String sequence = reader.read("Staphylococcus.fna");
		String target = "ACAATGTTCGAACAC";
		kmpSearch(sequence, target);
	}

	// Core algorithm for KMP Search
	// Finds any pattern in target array that matches its start
	/*
	 * @param target: target string
	 * @param next: int array to store the result
	 */
	public static void getNextArray(String target, int[] next)
	{
		// Next array stores which index to shift to
		// While the character at the index is not found
		for(int i = 0; i < target.length(); i++)
		{

			// Default for 0 and 1
			if (i == 0 || i == 1) next[i] = i - 1;
			// If the previous character continues matching the start
			// Increment the longest match length
			else if (target.charAt(i - 1) == target.charAt(next[i - 1]))
					next[i] = next[i - 1] + 1;
			// If it is a new match for the first character
			else if (target.charAt(i - 1) == target.charAt(0))
					next[i] = 1;
			// Set to 0 if no common substring
			else next[i] = 0;
		}
	}

	// KMP Search method
	// Generate a next array for the target pattern
	// Avoids any possible repeated comparison
	// Then prints all occurrences
	/*
	 * @param sequence: the genome sequence to search from
	 * @param target: the query sequence to be found
	 */
	public static void kmpSearch(String sequence, String target)
	{
        long startTime = System.nanoTime();
		int[] next = new int[target.length()];
		getNextArray(target, next);

		// Loop through sequence and try to match target
		// If failed to match, move target according to next array
		// If matched, print the sequence index
		int taridx = 0, seqidx = 0, found = 0;
		while (seqidx < sequence.length())
		{
			// If taridx == -1 last comparison of first character failed
			// Or if characters at current indices matches
			if (taridx == -1 || target.charAt(taridx) == sequence.charAt(seqidx))
			{
				// If matching finished
				// Print the index and reset taridx
				if (taridx == target.length() - 1)
				{
					found++;
					System.out.printf("Occurrence %d found at %d\n", found, seqidx - taridx + 1);
					taridx = next[taridx];
				}
				// Else increment target and sequence index
				else
				{
					taridx ++;
					seqidx ++;
				}
			}
			// Else return target index
			else taridx = next[taridx];
		}
        if (found == 0) System.out.println("No occurrence found");
        long timeTaken = System.nanoTime() - startTime;
        System.out.printf("Time taken to run kmpSearch: " + timeTaken +"ns\n");
	}
}
