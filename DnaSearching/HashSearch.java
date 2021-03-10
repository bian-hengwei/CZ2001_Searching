import java.io.IOException;
import java.lang.Character;
import java.util.HashMap;
import java.util.Map;

// Search algorithm designed for the genome sequence searching problem
// O(m + n)
public class HashSearch
{
	public static void main(String[] args) throws IOException
	{
		FileReader reader = new FileReader();
		String sequence = reader.read("COVID-19.fna");
		String target = "TTTA";
		hashSearch(sequence, target);
	}

	private static Map<Character, Integer> map = new HashMap<>();
	private static int nCount = 0;


	// Compare using hash value of sequence and target
	/*
	 * @param seq: text string
	 * @param tar: target string
	 */
	public static void hashSearch(String seq, String tar)
	{
		
							
		map.put('A', 0);
		map.put('C', 1);
		map.put('G', 2);
		map.put('T', 3);
		map.put('U', 3);

		// The length of the target string can not be larger than 16 characters
		// Due to the calculation and storage limit of long data type
		if (tar.length() > 32)
		{
			System.out.println("Maximum target length is 32 characters");
			return;
		}

		int tLen = tar.length(), sLen = seq.length(), count = 0;
		long tHash = 0, sHash = 0;

		// Base is the largest power of 4 in conversion
		long base = (long)Math.pow(4, tLen - 1);

		// Loop through every element of the text
		for (int i = 0; i <= sLen; i++)
		{
			// Build initial hash value first
			if (i < tLen)
			{
				tHash = hash(tar.charAt(i), 'A', tHash, base);
				sHash = hash(seq.charAt(i), 'A', sHash, base);
			}
			else
			{
				// Compares the hash value
				if (nCount == 0 && tHash == sHash)
				{
					count ++;
					System.out.printf("Occurrence %d found at %d\n", count, i - tLen + 1);
				}

				// Cuts the first digit and append a new last digit
				if (i != sLen) sHash = hash(seq.charAt(i), seq.charAt(i - tLen), sHash, base);
			}
		}
		// Checks if never found any occurrence
		if (count == 0) System.out.println("No occurrence found");
	}

	// Find the base 10 conversion of
	// Base 4 number converted by a string
	// By converting A to 0, C to 1, G to 2, T or U to 3
	// This method removes the most significant digit
	// And appends a least significant digit
	/*
	 * @param newChar: the character to be append at the end of hashValue
	 * @param oldChar: the character to be removed from the start of hashValue
	 * @param hashValue: current hash value of the sequence
	 * @param base: the power of 4 base to be subtracted from hashValue
	 *
	 * @return: new hashValue with oldChar removed and newChar append at the end
	 */
	public static long hash(char newChar, char oldChar, long hashValue, long base)
	{
		int newValue = 0, oldValue = 0;
		oldValue = map.getOrDefault(oldChar, -1);
		newValue = map.getOrDefault(newChar, -1);
		if (oldValue == -1) nCount --;
		if (newValue == -1) nCount ++;
		return (hashValue - oldValue * base) * 4 + newValue;
	}
}
