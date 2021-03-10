package cz2001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class Io {

    /*
     * build a set of strings containing nodes to identify as hospital nodes
     * @param   filePath        a relative path to the file containing hospital nodes id
     * @return  hospitalNodes   a set to populate the info into
     */
    public static Set<String> buildHospitalSet(String filePath) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
        String str = null;
        System.out.println(br.readLine()); // read out the first line
        Set<String> set = new HashSet<>();
        while((str = br.readLine()) != null) {
            String hospitalNode = str.trim();
            set.add(hospitalNode);
        }
        br.close();
        return set;
    }

    /*
     * build a map of nodes from a gz file
     * @param   filePath    a relative path to the file containing node information
     * @param   debug       a boolean to indicate whether to skip first four lines of file
     * @return  nodesMap    map of nodes to populate the info into
     */
    public static Map<String, Set<String>> buildGraph(String filePath) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
        Map<String, Set<String>> nodesMap = new HashMap<>();
        // reading in first 4 lines in the test set data, headings and columns, not data
        for (int i=0; i<4; i++) {
            System.out.println(br.readLine());
        }
        String str = null;
        while((str = br.readLine()) != null) {
            String[] nodes = str.split("\t");
            String node1 = nodes[0].trim();
            String node2 = nodes[1].trim();
            // get each node's neighbour set, add one into the other
            Set<String> node1Neighbours = nodesMap.getOrDefault(node1, new HashSet<String>());
            Set<String> node2Neighbours = nodesMap.getOrDefault(node2, new HashSet<String>());
            node1Neighbours.add(node2);
            node2Neighbours.add(node1);
            nodesMap.put(node1, node1Neighbours);
            nodesMap.put(node2, node2Neighbours);
        }
        br.close();
        return nodesMap;
    }

    /*
     * helper method to create randomised hospital nodes
     * @param num       number of hospital nodes to create
     * @param fileName  string containing name of file to write to
     * @param nodeMap   map of nodes to select hospital nodes
     */
    public static void writeHospitalNodesTestSet(int num, String fileName, Set<String> nodes) throws IOException {
        File f = new File(fileName);
        if (f.createNewFile()) {
            System.out.println(String.format("%s created successfully.", fileName));
        } else {
            System.out.println(String.format("%s already exists. Using old file.", fileName));
        }
        FileOutputStream output = new FileOutputStream(fileName);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("# %d\n", num));
        int i = 0;
        Set<String> set = new HashSet<>();
        Iterator<String> nodesIt = nodes.iterator();
        while (i < num && nodesIt.hasNext()) {
            String hospitalNode = nodesIt.next();
            if (set.add(hospitalNode)) {
                i++;
                sb.append(String.format("%s\n", hospitalNode));
            }
        }
        try {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), StandardCharsets.UTF_8);
            try {
                writer.write(sb.toString());
            } finally {
                writer.close();
            }
        } finally {
            output.close();
        }
        System.out.println(String.format("Created and populated %s successfully", fileName));
    }

    /*
     * helper method to create a randomised graph for testing
     * @param  num      number of edges from [0, num-1]
     * @param  fileName string containing name of file to write to
    */
    public static void writeTestSet(int num, String fileName) throws IOException {
        File f = new File(fileName);
        if (f.createNewFile()) {
            System.out.println(String.format("%s created successfully.", fileName));
        } else {
            System.out.println(String.format("%s already exists. Using old file.", fileName));
        }
        FileOutputStream output = new FileOutputStream(fileName);
        StringBuilder sb = new StringBuilder();
        sb.append("Randomly generated test set.\n\n\nFrom Node #\tTo Node #\n"); // first four lines are filler lines
        for (int i=0; i < num; i++) {
            // randomly generate 2 node indexes
            // value of nodes can range from [0, num], note: num inclusive
            int node1 = ThreadLocalRandom.current().nextInt(num+1);
            int node2 = ThreadLocalRandom.current().nextInt(num+1);
            sb.append(String.format("%s\t%s\n", String.valueOf(node1), String.valueOf(node2)));
        }
        try {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), StandardCharsets.UTF_8);
            try {
                writer.write(sb.toString());
            } finally {
                writer.close();
            }
        } finally {
            output.close();
        }
        System.out.println(String.format("Created and populated %s successfully", fileName));
    }

    public static StringBuilder metadataString(String testFileName, String hospitalNodeFileName) {
        StringBuilder sb = new StringBuilder();
        // testcase meta data
        sb.append(String.format("Results of shortest path, nodes provided by file: %s, hospital nodes provided by: %s \n\n", testFileName, hospitalNodeFileName));
        return sb;
    }

    // writes a path to a file, *** to be extended for list of paths
    public static StringBuilder pathString(Stack<String> path, String startNodeIndex) {
        StringBuilder sb = new StringBuilder();
        if (path == null) {
            sb.append(String.format("No possible path found for node %s", startNodeIndex));
        }
        else {
            sb.append(path.get(0));
            for (int i=1; i < path.size(); i++) {
                sb.append(String.format(" -> %s", String.valueOf(path.get(i))));
            }
            sb.append(String.format("\t (distance: %d)", path.size() - 1));
        }
        sb.append("\n");
        return sb;
    }

    public static StringBuilder hospitalsString(Stack<String[]> hospitals, String node, int k) {
        StringBuilder sb = new StringBuilder();
        if (hospitals.empty()) {
            sb.append(String.format("No possible path found for node %s", node));
        }
        else {
            if (hospitals.size() < k)
                sb.append(" NOT ENOUGH HOSPITALS FOUND ");

            sb.append(String.format("Node %s:", node));
            while (!hospitals.isEmpty()) {
                String[] hospDist = hospitals.pop();
                sb.append(String.format(" hospital: %s (distance: %s) ", hospDist[0], hospDist[1]));
            }
        }
        sb.append("\n");
        return sb;
    }

    public static void writeToOutputFile(StringBuilder sb, String destinationFileName) throws IOException {
        File f = new File(destinationFileName);
        if (f.createNewFile()) {
            System.out.println(String.format("%s created successfully.", destinationFileName));
        } else {
            System.out.println(String.format("%s already exists. Using old file.", destinationFileName));
        }
        FileOutputStream output = new FileOutputStream(destinationFileName);
        try {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), StandardCharsets.UTF_8);
            try {
                writer.write(sb.toString());
            } finally {
                writer.close();
            }
        } finally {
            output.close();
        }
        System.out.println(String.format("Created and populated %s successfully", destinationFileName));
    }

    
    public static void hsPathWriteToOutputFile(StringBuilder sb, String destinationFileName, Map<String, Stack<String>> nodesPath) throws IOException {
        File f = new File(destinationFileName);
        if (f.createNewFile()) {
            System.out.println(String.format("%s created successfully.", destinationFileName));
        } else {
            System.out.println(String.format("%s already exists. Using old file.", destinationFileName));
        }
        FileOutputStream output = new FileOutputStream(destinationFileName);
        for (String key: nodesPath.keySet()){
            Stack<String> path = nodesPath.get(key);
            int size = path.size();
            sb.append(path.pop());
            while(!path.isEmpty()){
                sb.append(String.format(" -> %s", String.valueOf(path.pop())));
            }
            sb.append(String.format("\t (distance: %d)", size - 1));
            sb.append("\n");
        }
        try {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), StandardCharsets.UTF_8);
            try {
                writer.write(sb.toString());
            } finally {
                writer.close();
            }
        } finally {
            output.close();
        }
        System.out.println(String.format("Created and populated %s successfully", destinationFileName));
    }

    public static void hsWriteToOutputFile(StringBuilder sb, String destinationFileName, Map<String, Stack<String[]>> targetsMap, int k) throws IOException {
        File f = new File(destinationFileName);
        if (f.createNewFile()) {
            System.out.println(String.format("%s created successfully.", destinationFileName));
        } else {
            System.out.println(String.format("%s already exists. Using old file.", destinationFileName));
        }
        FileOutputStream output = new FileOutputStream(destinationFileName);
        for (String key: targetsMap.keySet()){
            Stack<String[]> targets = targetsMap.get(key);
            sb.append(String.format("Node %s:", key));
            if (targets.size() < k)
                sb.append(" NOT ENOUGH HOSPITALS FOUND ");
            while (!targets.isEmpty()) {
                String[] hospDist = targets.pop();
                sb.append(String.format(" hospital: %s (distance: %s) ", hospDist[0], hospDist[1]));
            }
            sb.append("\n");
        }
        try {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), StandardCharsets.UTF_8);
            try {
                writer.write(sb.toString());
            } finally {
                writer.close();
            }
        } finally {
            output.close();
        }
        System.out.println(String.format("Created and populated %s successfully", destinationFileName));
    }
}
