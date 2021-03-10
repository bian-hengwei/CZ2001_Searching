## CZ2001 Group 4 Project 1 Code Submission 

  
### Prerequisites:
  
1. Any latest of JRE to run java programs 
2. .fna files that you wish to test on has to be placed in the same working directory as SearchGenome.class 
  
### Instructions to run the program:
  
1. if SearchGenome.class is not present, compile SearchGenome.java using the command: javac SearchGenome.java 
2. Run SearchGenome.class with the command: java SearchGenome 
  
### Program Constraints:
  
1. Size of .fna files that might be readable varies according to the hardware specs, in cases of crashes like 
a memory error, it indicates not an error in the program but a limitation posed by the hardware. For reference,
we have tested up to 3GB .fna files on a laptop with 32GB RAM. 
  
2. As documented in the report, pattern sizes longer than 32 characters will not be accepted as an input pattern 
for our implementation of hash search.


# CZ2001 Group 4 Project 2 Code Submission 
  
## Prerequisites: 
- Java8 and maven 
  
## Running the program: 
- mvn compile 
- mvn install 
- mvn exec:java -Dexec.mainClass=cz2001.Main 
  
Instructions to run the algorithms are provided in the command line interface. 
  
--- For visualisation --- 
To run a visualisation with randomly generated test sets, the number of edges provided has to be 25 or less. This is a self imposed restriction as we feel that the visualisation tool would be too cluttered and messy with too many nodes, defeating its purpose. An image would be generated for each node, such that every node has the chance to be a starting node. For the first h images, it would not have any path since the node itself is a hospital node. Starting node performing BFS would be marked as a green node, red nodes are the hospital nodes and brown nodes are the identified shortest path. 
  
## Members: 
  
He Yinan U1922693C 
Amadeus Koh Ying Jie U1922072A 
Gerald Yip Wei Yong U1921851J 
Fu Yongding U1921155E 
Bian Hengwei U1923732B 
