package cz2001;

import java.io.IOException;
import java.util.Scanner;


public class Main {

    static String fileName;
    static String hospitalNodeFileName;
    static String destinationFileName;
    static boolean createHospitalFile = false, createTestFile = false;
    // control paramters for testing
    static int numHospitals = -1, numOfNodes = -1;

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("------- CZ2001 Group 4 -------");
        System.out.println("1. Perform task A with multisource BFS, with h in complexity");
        System.out.println("2. Perform task B with BFS, without h in complexity (visualisation for random graph < 25 edges)");
        System.out.println("3. Perform task C with BFS, k=2");
        System.out.println("4. Perform task D with BFS with custom input k");
        System.out.println("5. Perform task D with multisource BFS with custom input k, dependent on h in complexity but much faster");
        System.out.println("Enter option: ");
        Scanner s = new Scanner(System.in);
        int input = s.nextInt();
        if (input < 1 || input > 5)
            System.out.println("input not recognized, exiting");
            System.exit(0);
        s.nextLine(); // read in \n
        query(s);
        int k = 0;

        switch(input) {

            case 1:
                Bfs.search(input, fileName, hospitalNodeFileName, destinationFileName, createTestFile, 
                            createHospitalFile, numOfNodes, numHospitals, 1);
                break;

            case 2:
                Bfs.search(input, fileName, hospitalNodeFileName, destinationFileName, createTestFile, 
                            createHospitalFile, numOfNodes, numHospitals, 1);
                break;

            case 3:
                Bfs.search(input, fileName, hospitalNodeFileName, destinationFileName, createTestFile, 
                            createHospitalFile, numOfNodes, numHospitals, 2);
                break;

            case 4:
                System.out.println("Enter the num of paths to search for, k: ");
                k = s.nextInt();
                Bfs.search(input, fileName, hospitalNodeFileName, destinationFileName, createTestFile, 
                            createHospitalFile, numOfNodes, numHospitals, k);
                break;

            case 5:
                System.out.println("Enter the num of paths to search for, k: ");
                k = s.nextInt();
                Bfs.search(input, fileName, hospitalNodeFileName, destinationFileName, createTestFile, 
                            createHospitalFile, numOfNodes, numHospitals, k);
                break;

            default:
                System.out.println("input not recognized, exiting");

        }
        s.close();

    }

    // retrieve the control parameters
    public static void query(Scanner s) {

        System.out.println("Would you like to use a randomly generated test file? y/n");
        char ans = Character.toLowerCase(s.nextLine().charAt(0));
        while (ans != 'y' && ans != 'n') {
            ans = Character.toLowerCase(s.nextLine().charAt(0));
        }
        if (ans == 'n') {
            System.out.println("Enter the test file path (relative to the working directory or absolute path), must include .txt.gz as the extension: ");
            System.out.println("File must be compressed with extension .txt.gz format.");
            fileName = s.nextLine();
            if (!fileName.endsWith(".txt.gz"))
                fileName += ".txt.gz";
        } else {
            System.out.println("Enter the test file name to be created, must include .txt.gz as the extension: ");
            System.out.println("File will be placed in the working directory.");
            fileName = s.nextLine();
            if (!fileName.endsWith(".txt.gz"))
                fileName += ".txt.gz";
            createTestFile = true;
            System.out.println(
                    "Enter the number of random edges to create, duplicates may occur resulting in fewer nodes: ");
            numOfNodes = s.nextInt();
            s.nextLine(); // read in \n
        }

        System.out.println("Would you like to use a randomly generated hospital test file? y/n");
        ans = Character.toLowerCase(s.nextLine().charAt(0));
        while (ans != 'y' && ans != 'n') {
            ans = Character.toLowerCase(s.nextLine().charAt(0));
        }
        if (ans == 'n') {
            System.out.println("Enter the hospital file path (relative to the working directory or absolute path): ");
            System.out.println("File must be compressed with extension .txt.gz format.");
            hospitalNodeFileName = s.nextLine();
            createHospitalFile = false;
            if (!hospitalNodeFileName.endsWith(".txt.gz"))
                hospitalNodeFileName += ".txt.gz";
        } else {
            System.out.println("Enter the hospital file name to be created, must include .txt.gz as the extension: ");
            System.out.println("File will be placed in the working directory.");
            hospitalNodeFileName = s.nextLine();
            if (!hospitalNodeFileName.endsWith(".txt.gz"))
                hospitalNodeFileName += ".txt.gz";
            createHospitalFile = true;
            System.out.println("Enter the number of hospital nodes to randomly select: ");
            numHospitals = s.nextInt();
            s.nextLine(); // read in \n
        }

        System.out.println("Enter output file name, must include .txt.gz as the extension: ");
        System.out.println("File will be placed in the working directory.");
        destinationFileName = s.nextLine();
        if (!destinationFileName.endsWith(".txt.gz"))
            destinationFileName += ".txt.gz";
    }

}