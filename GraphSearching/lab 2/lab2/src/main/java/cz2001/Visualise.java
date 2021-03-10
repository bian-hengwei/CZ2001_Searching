package cz2001;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class Visualise {

    private static Graph graph;

    public static void visualise(Map<String,Set<String>> nodesMap, Set<String>hospitalNodes, String nodeIndex, Stack<String> foundPaths)
            throws IOException, InterruptedException {
        visualiseGraph(nodesMap);
                Visualise.setStartAndEndNodes(nodeIndex, hospitalNodes);
                Thread.sleep(3000); // sleep 3s before performing bfs
                if (foundPaths == null) {
                    System.out.println("no path found...");
                } else {
                    System.out.println(String.format("coloring path for starting node %s", nodeIndex));
                    colorFoundPath(foundPaths);
                }
                Thread.sleep(3000); // sleep 3 seconds before proceeding to next node as starting node
    }
    
    /*
     function called to visualise the graph, creates the graph by utilising the already populated nodesMap


     * @input filePath: string containing path to file to read nodes
     * @input test: boolean to denote whether this is an external test set, meaning to skip first 4 lines
     */
    public static void visualiseGraph(Map<String, Set<String>> nodesMap) throws IOException {
        System.setProperty("org.graphstream.ui", "swing"); 
        graph = new SingleGraph("Testing");

        Set<String> seenNodes = new HashSet<>();
        Set<String> seenEdges = new HashSet<>();

        int cnt  = 600; // cnt controls max num of nodes to read in
        Iterator nodes = nodesMap.entrySet().iterator();
        while (nodes.hasNext() && cnt>0) {
            Map.Entry<String, Set<String>> pair = (Map.Entry<String, Set<String>>) nodes.next();
            String node = pair.getKey();
            Iterator nodeNeighbours =  pair.getValue().iterator();
            if (seenNodes.add(node)) {
                graph.addNode(node);
                cnt--;
            }
            while(nodeNeighbours.hasNext()) {
                String neighbourNode = (String) nodeNeighbours.next();
                if (seenNodes.add(neighbourNode)) {
                    graph.addNode(neighbourNode); // add the neighbour node before connecting them
                    cnt--;
                }
                if (seenEdges.add(node+neighbourNode) && seenEdges.add(neighbourNode+node)) {
                    graph.addEdge(node+neighbourNode, node, neighbourNode); // add undirected edge
                }
            }
            
        }
        graph.display();
    }

    // colors start node 2 to green, end nodes 0 and 1 to red. This is hardcoded to return true only
    // all 3 nodes 0, 1, 2 were randomly created.

    /*
     function called to color the graph, creates the graph by utilising the already populated hospitalNodesSet


     * @input   startingNodeIndex    starting node index
     * @input   hospitalNodesSet    a set containing hospital nodes index
     */
    public static void setStartAndEndNodes(String startingNodeIndex, Set<String> hospitalNodesSet) {
        graph.setAttribute("ui.stylesheet", styleSheet);
        Iterator hospitalNodes = hospitalNodesSet.iterator();
        while(hospitalNodes.hasNext()) {
            Node hospitalNode = graph.getNode( (String) hospitalNodes.next());
            hospitalNode.setAttribute("ui.color", Float.valueOf("1")); // color hospital nodes red
            hospitalNode.setAttribute("ui.size", 30);
        }

        Node startingNode = graph.getNode(startingNodeIndex); // color starting node green
        startingNode.setAttribute("ui.color", Float.valueOf("0.5"));
        startingNode.setAttribute("ui.size", 30);
    }

    public static void colorFoundPath(Stack<String> foundPaths) throws InterruptedException {
        for (int i=0; i<foundPaths.size(); i++) {
            String foundNodeIndex = foundPaths.get(i);
            Node node = graph.getNode(foundNodeIndex);
            node.setAttribute("ui.color", Float.valueOf("0.75"));
            node.setAttribute("ui.size", 25);
            Thread.sleep(1000);
        }
        
    }

    private static String styleSheet = 
    "node {"+
    "   size-mode: dyn-size;"+
    "   shape: circle;"+
    "   size: 20px;"+
    "   fill-mode: dyn-plain;"+
    "   fill-color: white, green, red;"+
    "   stroke-mode: plain;"+
    "   stroke-color: black;"+
    "   stroke-width: 1px;"+
    "}";
}
