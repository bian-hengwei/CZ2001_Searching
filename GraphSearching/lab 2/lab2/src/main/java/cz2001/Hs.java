package cz2001;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


/**
 * class for hospital search algorithm
 * a very efficient algorithm for graph searching
 * time complexity: O(n)
 */
public class Hs {

    /**
     * hsWithPath is an algorithm that reads in the nodes map and finds the nearest hospital path for each hospital
     * @param targetNodes a set of hospital nodes index
     * @param nodesMap a map of set containing {node index: set of neighbour nodes}
     * @return a map of stacks containing the paths for each node {node index: stack of path found to nearest hospital}
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Stack<String>> hsWithPath(Set<String> targetNodes, Map<String, Set<String>> nodesMap){
        Queue<Stack<String>> queue = new LinkedList<>();
        Map<String, Stack<String>> nodesPath = new HashMap<>();
        Set<String> visitedNodes = new HashSet<>();
        int counter = 0;

        Iterator<String> hospitalIterator = targetNodes.iterator();
        while (hospitalIterator.hasNext()){
            String hospital = hospitalIterator.next();
            Stack<String> path = new Stack<>();
            path.add(hospital);
            queue.add(path);
            visitedNodes.add(hospital);
        }

        while (!queue.isEmpty()){
            Stack<String> currentPath = queue.poll();
            String currentNode = currentPath.peek();
            if (!nodesPath.containsKey(currentNode)){
                nodesPath.put(currentNode, currentPath);
                if (counter % 500000 == 0)
                    System.out.println("Counter: " + counter);
                counter++;
            } else{
                continue;
            }
            Set<String> nodeNeighbours = nodesMap.get(currentNode);
            Iterator<String> nodeNeighbourIterator = nodeNeighbours.iterator();
            while (nodeNeighbourIterator.hasNext()){
                String neighbour = nodeNeighbourIterator.next();
                if (!visitedNodes.contains(neighbour)){
                    Stack<String> copyPath = (Stack<String>) currentPath.clone();
                    copyPath.push(neighbour);
                    queue.add(copyPath);
                    visitedNodes.add(neighbour);
                }
            }
        }
        return nodesPath;
    }
    
    /**
     * a methods that takes in the map and finds k nearest hospitals for all nodes
     * @param targetNodes a set of hospital nodes index
     * @param nodesMap a map of set containing {node index: set of neighbour nodes}
     * @param k number of hospital nodes to be found
     * @return a map of stack of hospital targets {node index: stack of {target index, distance}}
     */
    public static Map<String, Stack<String[]>> hs(Set<String> targetNodes, Map<String, Set<String>> nodesMap, int k) {

        // for each node, target map stores a stack of [hospital index, distance to hospital]
        Map<String, Stack<String[]>> nodeToHospInfo = new HashMap<>();
        // queue for [node index, hospital index, distance to hospital]
        Queue<String[]> queue = new LinkedList<>();
        // mapping from source node to visited nodes set
        Map<String, Set<String>> visitedNodes = new HashMap<>();
        int counter = 0;

        Iterator<String> hospitalIterator = targetNodes.iterator();
        while (hospitalIterator.hasNext()){
            String hospital = hospitalIterator.next();
            String[] tuple = new String[] {hospital, hospital, "0"};
            queue.add(tuple);
            Set<String> hospitalVisited = new HashSet<>();
            hospitalVisited.add(hospital);
            visitedNodes.put(hospital, hospitalVisited);
        }
        
        while (!queue.isEmpty()) {
            String[] currentNodeInfo = queue.poll();
            String currentNode = currentNodeInfo[0];
            if (!nodeToHospInfo.containsKey(currentNode) || nodeToHospInfo.get(currentNode).size() < k) { // check if this node has been visited by k num of hosp
                Stack<String[]> hospInfo = nodeToHospInfo.getOrDefault(currentNode, new Stack<String[]>());
                hospInfo.push(new String[] {currentNodeInfo[1], currentNodeInfo[2]});
                nodeToHospInfo.put(currentNode, hospInfo);
                
                if (counter % 250000 == 0)
                    System.out.println("Paths discovered: " + counter);
                counter++;
            } else {
                continue;
            }

            Set<String> nodeNeighbours = nodesMap.get(currentNodeInfo[0]);
            Iterator<String> nodeNeighbourIterator = nodeNeighbours.iterator();
            while (nodeNeighbourIterator.hasNext()) {
                String neighbour = nodeNeighbourIterator.next();
                String sourceNode = currentNodeInfo[1];
                if (!visitedNodes.get(sourceNode).contains(neighbour)) {
                    queue.add(new String[] {neighbour, sourceNode, String.valueOf(Integer.valueOf(currentNodeInfo[2]) + 1)});
                    Set<String> set = visitedNodes.get(sourceNode);
                    set.add(neighbour);
                    visitedNodes.put(sourceNode, set); // update visitednodes map
                }
            }
        }
        return nodeToHospInfo;
    }
}
