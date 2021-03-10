import java.io.IOException;

public class BruteForceAlgorithm {
    public static void main(String[] args) throws IOException {

        FileReader fileReader = new FileReader();
        String input = fileReader.read("COVID-19.fna"); 
        String target = "ACGTGCTCGTACGTGGCTTTGGAGACTC";
        naiveStringSearch(input, target);
        System.out.println(System.nanoTime());
    }

    public static void naiveStringSearch(String input, String pattern){
        long startTime = System.nanoTime();
        boolean match;
        int found = 0;
        for (int i = 0; i<input.length()-pattern.length()+1; i++){
            match = true;
            for (int j = 0; j<pattern.length(); j++){
                if (input.charAt(i+j) != pattern.charAt(j)){
                    match = false;
                    break;
                }
            }
            if (match){
                found++;
                System.out.printf("Occurrence %d found at %d\n", found, i + 1);
            }
        }
        if (found == 0){
        	System.out.println("Pattern not found.");
        }

        long timeTaken = System.nanoTime() - startTime;
        System.out.printf("Time taken to run naiveSearch: " + timeTaken +"ns\n");
    }
}
