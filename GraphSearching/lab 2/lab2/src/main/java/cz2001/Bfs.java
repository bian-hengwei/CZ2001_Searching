package cz2001;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;


/**
 * bfs searching algorithm
 * not as efficient but time complexity is independent on number of hospitals
 */
public class Bfs {

    public static void search(int choice, String fileName, String hospitalNodeFileName, String destinationFileName, 
                                boolean createTestFile, boolean createHospitalFile, int numOfNodes, 
                                int numHospitals, int k) throws IOException, InterruptedException {
        
        // adjacent list format, key of node in map is the node's index identifier,
        // value at that index is a list of its neighbouring index
        Map<String, Set<String>> nodesMap = null;
        Set<String> hospitalNodes = null;
        boolean visualise = false;
        if (choice == 2 && numOfNodes <= 25){
            Scanner s = new Scanner(System.in);
            System.out.println("Would you like to visualise the graph? y/n");
            char ans = Character.toLowerCase(s.nextLine().charAt(0));
            while (ans != 'y' && ans != 'n') {
                ans = Character.toLowerCase(s.nextLine().charAt(0));
            }
            if (ans == 'y')
                visualise = true;
            s.close();
        }
        // preparing mappings
        if (createTestFile) {
            Io.writeTestSet(numOfNodes, fileName); // helper method to generate random graph
        }
        nodesMap = Io.buildGraph(fileName);
        if (createHospitalFile) {
            Io.writeHospitalNodesTestSet(numHospitals, hospitalNodeFileName, nodesMap.keySet());
        }
        hospitalNodes = Io.buildHospitalSet(hospitalNodeFileName);
        StringBuilder sb = Io.metadataString(fileName, hospitalNodeFileName);
        long startTime, endTime, duration;
        switch (choice) {
            case 1:
                startTime = System.nanoTime();
                Map<String, Stack<String>> hsPaths = Hs.hsWithPath(hospitalNodes, nodesMap);
                endTime = System.nanoTime();
                duration = endTime - startTime;
                System.out.println("Time taken: " + duration / 1000000 + "ms");
                Io.hsPathWriteToOutputFile(sb, destinationFileName, hsPaths);
                break;
            case 2:
                startTime = System.nanoTime();
                for (String nodeIndex: nodesMap.keySet()) {
                    Stack<String> foundPaths = bfsWithPath(nodeIndex, hospitalNodes, nodesMap);
                    if (visualise) {
                        Visualise.visualise(nodesMap, hospitalNodes, nodeIndex, foundPaths);
                    }
                    sb.append(Io.pathString(foundPaths, nodeIndex));
                }
                endTime = System.nanoTime();
                duration = endTime - startTime;
                System.out.println("Time taken: " + duration / 1000000 + "ms");
                Io.writeToOutputFile(sb, destinationFileName);
                break;
            case 3:
            case 4:
                startTime = System.nanoTime();
                for (String nodeIndex: nodesMap.keySet()) {
                    Stack<String[]> targetsStack = bfs(nodeIndex, hospitalNodes, nodesMap, k);
                    sb.append(Io.hospitalsString(targetsStack, nodeIndex, k));
                }
                endTime = System.nanoTime();
                duration = endTime - startTime;
                System.out.println("Time taken: " + duration / 1000000 + "ms");
                Io.writeToOutputFile(sb, destinationFileName);
                break;
            case 5:
                startTime = System.nanoTime();
                Map<String, Stack<String[]>> targetsMap = Hs.hs(hospitalNodes, nodesMap, k);
                endTime = System.nanoTime();
                duration = endTime - startTime;
                System.out.println("Time taken: " + duration / 1000000 + "ms");
                Io.hsWriteToOutputFile(sb, destinationFileName, targetsMap, k);
                break;
            default:
                System.out.println("command not recognized");
        }
        System.out.println("ALL DONE, check output file for results.");
    }

    /*
     * this function performs BFS and returns a path found
     * @param   startNodeIndex  string index of starting node
     * @param   targetNodes    set of string containg hospital nodes' index
     * @param   k   num of paths it tries to return
     * @return  a stack containing the path of the shortest distance, ** to be modified to be a list of stack
    */
    @SuppressWarnings("unchecked")
    public static Stack<String> bfsWithPath(String startNodeIndex, Set<String> targetNodes, 
                                                    Map<String, Set<String>> nodesMap) throws InterruptedException {
        Queue<Stack<String>> queue = new LinkedList<>();
        Set<String> visitedNodes = new HashSet<>(); // do not visit nodes in cycle, might remove some edges but still will result in optimal path!
        // start initial path with starting node
        Stack<String> initialPath = new Stack<>();
        initialPath.push(startNodeIndex);
        queue.add(initialPath);
        while (!queue.isEmpty()) {
            Stack<String> path = queue.poll();
            String lastNodeInPath = path.lastElement();
            if (targetNodes.contains(lastNodeInPath)) {
                return path;
            }
            // iterate through its neighbours, generate new possible paths
            // set of strings representing neighbour nodes
            Iterator<String> neighbourNodes = nodesMap.get(lastNodeInPath).iterator();
            while (neighbourNodes.hasNext()) {
                String neighbourNode = neighbourNodes.next();
                if (visitedNodes.add(neighbourNode)) { // not yet visited
                    Stack<String> copyPath = (Stack<String>) path.clone(); // make a deep copy
                    copyPath.add(neighbourNode);
                    queue.add(copyPath);
                }
            }
        }
        return null; // returns null when no path found
    }

    /**
     * top k bfs function that takes in the map and returns a the hospitals, distance stack
     * @param node node index for performing bfs
     * @param hospital hospitals set
     * @param nodesMap nodes map with each node mapped with all neighbours
     * @param k top k hospitals to be found
     * @return a stack of k [hospital, distance] tuples
     */
    public static Stack<String[]> bfs(String node, Set<String> hospital, Map<String, Set<String>> nodesMap, int k)
    {
        Stack<String[]> result = new Stack<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> depthCounter = new HashMap<>();
        queue.add(node);
        depthCounter.put(node, 0); // root node depth is 0
        while (!queue.isEmpty() && k != 0)
        {
            String currentNode = queue.poll();
            if (hospital.contains(currentNode))
            {
                k -= 1;
                result.push(new String[] {currentNode, String.valueOf(depthCounter.get(currentNode))});
            }
            Iterator<String> neighbourNodes = nodesMap.get(currentNode).iterator();
            while (neighbourNodes.hasNext())
            {
                String neighbourNode = neighbourNodes.next();
                if (!depthCounter.containsKey(neighbourNode))
                {
                    depthCounter.put(neighbourNode, depthCounter.get(currentNode) + 1);
                    queue.add(neighbourNode);
                }
            }
        }
        return result;
    }
}
