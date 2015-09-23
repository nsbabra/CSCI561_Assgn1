import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author nsbabra
 */
public class waterFlow {
//    private static int TEST_CASES ;
    
    //Variables used for Current Test Case
    private static Node mStartNode ;
    private static HashMap<String, Node> mDestinationNodes;
    private static HashMap<String, Node> mMiddleNodes;
    private static int mStartTime;
    
    
    //Because Macs and Windows have different newline character.
    private static final String NEWLINE = System.getProperty("line.separator");

    //Test Cases in the Input File
    private static Test[] tests;
    

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(Arrays.toString(args));
        
        // If output.txt exists, delete it. Just a sanity check.
        File f = new File("output.txt");
        if(f.exists()){
            f.delete();
        }
        
        //Runtime Arguments Parsing
        if(args.length!=2){
             try (FileWriter filewriter = new FileWriter("output.txt")) {
                    filewriter.write("Exception in Parsing th File");
                    filewriter.close();
             }catch(IOException ex){
                 System.out.println("Exception in Writing to File : "+ex);
             }
            System.exit(1); 
        }
        if(!args[0].equals("-i") || !args[1].endsWith(".txt")){
             try (FileWriter filewriter = new FileWriter("output.txt")) {
                    filewriter.write("Exception in Parsing th File");
                    filewriter.close();
             }catch(IOException ex){
                 System.out.println("Exception in Writing to File : "+ex);
             }
            System.exit(1); 
        }

        //Reading from the File
        BufferedReader reader = new BufferedReader(new FileReader(args[1]));
        try{
            int _testCases = Integer.parseInt(reader.readLine());

            //Instantiate the Test array 
            tests =  new Test[_testCases];

            
            //Reads the Input file and creates "test" objects for each test
            for(int i=0; i<_testCases ; i++){

                String type = reader.readLine();
                String startNode = reader.readLine();
                String destinations = reader.readLine();
                String middleNodes = reader.readLine();

                int pipes = Integer.parseInt(reader.readLine());

                String graph[] = new String[pipes];
                for(int j=0; j<pipes; j++){
                       graph[j] = reader.readLine();
                }

                int startTime = Integer.parseInt(reader.readLine());

                tests[i] = new Test(type, startNode, destinations, middleNodes, graph, startTime);
                reader.readLine(); //Empty Line
            }
        
        }catch(IOException | NumberFormatException ex){
            try (FileWriter filewriter = new FileWriter("output.txt")) {
                reader.close();
                filewriter.write("Exception in Parsing the Input File");
                filewriter.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("Exception in Parsing Input File : "+ex);
            System.exit(1);
        }
        
        try (FileWriter filewriter = new FileWriter("output.txt")) {
            
            //Iterate on all the Tests 
            for (Test currentTest : tests) {
                switch(currentTest.type){
                    case "BFS":
                        BFSDFSGraphBuilder(currentTest);
                        BFS(filewriter);
                        break;
                    case "DFS":
                        BFSDFSGraphBuilder(currentTest);
                        DFS(filewriter);
                        break;
                    case "UCS":
                        UCS(filewriter);
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(waterFlow.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
       
    private static void BFSDFSGraphBuilder(Test currentTest){

        HashMap<String, Node> graph = new HashMap<>();
        
        mStartNode = new Node(currentTest.startNode);
        mStartTime = currentTest.startTime;
        
        graph.put(mStartNode.label, mStartNode);
        
        mDestinationNodes = new HashMap<>();
        
        // Adding Goal Nodes to the HashMap
        StringTokenizer tokenizerDest = new StringTokenizer(currentTest.destinationNodes);
        while(tokenizerDest.hasMoreTokens()){
            String currentToken = tokenizerDest.nextToken();
            Node destNode = new Node(currentToken);
            mDestinationNodes.put(currentToken, destNode);
            graph.put(currentToken, destNode);
        }
        
        //Adds middle nodes to graph, which will be used in adjancey list creator.
        StringTokenizer tokenizerMiddle = new StringTokenizer(currentTest.middleNodes);
        while(tokenizerMiddle.hasMoreTokens()){
            String currentToken = tokenizerMiddle.nextToken();
            Node middleNode = new Node(currentToken);
            graph.put(currentToken, middleNode);
        }
        
        //Adjancy List Settter, for every node
        StringTokenizer tokenizer = null;
        for (String graphLine : currentTest.graph) {
            tokenizer = new StringTokenizer(graphLine);
            Node node = graph.get(tokenizer.nextToken());
            node.adjList.add(graph.get(tokenizer.nextToken()));
        }
    }

    
    private static void BFS(FileWriter fileWriter){
                 
         //Start Node is the Goal, So write to file and return. 
         //Should not happen. but here because of sanity check
         if(mDestinationNodes.containsKey(mStartNode.label)){
             try{
                fileWriter.append((mDestinationNodes.get(mStartNode.label)).toString());
                fileWriter.append(" "+(mStartTime%24 == 0 ? 0: mStartTime)+NEWLINE);
             }
             catch(IOException ex){
                 System.out.println("Exception in writing to File : "+ex);
             }
             return;
         }
         
         //Frontier, initialized with start ode
         LinkedList<Node> fifoQueue = new LinkedList<>();
         fifoQueue.add(mStartNode);
         
         //Explored Queue (Empty)
//         LinkedList<Node> exploredQueue = new LinkedList<>();
         
         
         while(true){
             //If Frontier is empty, return None in File  
             if(fifoQueue.isEmpty()){
                 try{
                    fileWriter.append("None"+NEWLINE);
                 }
                 catch(IOException ex){
                     ex.printStackTrace();
                 }
                 return;
             }
            
             //If Not, then remove the First Node (is queue.remove) from Frontier 
             Node currentNode = (Node) fifoQueue.remove();
             
             //Set State to 1 - i.e. Explored
             currentNode.state = 1;
             
             //Add to Explored Queue
//             exploredQueue.add(currentNode);
             
             //Comparator for adjList, Alphabetical Sorting of adjList before inserting in Frontier
             Comparator<Node> nodeComparator = (Node o1, Node o2) -> o1.toString().compareTo(o2.toString());
             
             //Sort them by Alphabetical Order
             Collections.sort(currentNode.adjList, nodeComparator);
             
             //For all the nodes in current node's adj list if the node is not explored or in frontier
             //Check if it's Destination. if yes, write to File and Return.
             //Else add the node to frontier
             for(Node node: currentNode.adjList){
                 //If Node is NOT explored and it is not in Frontier (fifoQueue)
                 if(node.state==0 && !fifoQueue.contains(node)){
                    node.parent = currentNode;

                     //If the node is Destination
                     if(mDestinationNodes.containsKey(node.label)){
                         int i=0;
                         
                         Node tempforPath = mDestinationNodes.get(node.label);
                         
                         if(tempforPath!=null){
                         while(!mStartNode.equals(tempforPath)){
                             i++;
//                             System.out.println("Path backwards : "+tempforPath.label);
                             tempforPath = tempforPath.parent;
                             if(tempforPath==null){
                                 break;
                             }
                         }
                         }

                         try{
                            fileWriter.append((mDestinationNodes.get(node.label)).toString());
                            int cost = (mStartTime+i);
                            while(cost>=24){
                                cost = cost - 24;
                            }
                            fileWriter.append(" "+(cost)+NEWLINE);
                         }
                         catch(IOException ex){
                             System.out.println("Exception in writing to File : "+ex);
                         }
//                         System.out.println("Cost is : "+(mStartTime+i));
                         return;
                     }
                     //If node is not destination, just add to frontier
                     fifoQueue.add(node);
                 }
             }
        }
    }
    
    private static void DFS(FileWriter fileWriter){
        
         //Start Node is the Goal, So write to file and return. 
         //Should not happen. but here because of sanity check
         if(mDestinationNodes.containsKey(mStartNode.label)){
             try{
                fileWriter.append((mDestinationNodes.get(mStartNode.label)).toString());
                fileWriter.append(" "+(mStartTime%24 == 0 ? 0: mStartTime)+NEWLINE);
             }
             catch(IOException ex){
                 System.out.println("Exception in writing to File : "+ex);
             }
             return;
         }
         
         
         //Stack for DFS
         Stack<Node> lifoStack = new Stack<>();
         lifoStack.add(mStartNode);
         
         //Loop to iterate over the stack, while it's empty
         while(true){
             if(lifoStack.isEmpty()){
                 //If Stack is Empty, that means Goal Node not found, so write None and return. 
                 try{
                    fileWriter.append("None"+NEWLINE);
                 }
                 catch(Exception ex){
                     ex.printStackTrace();
                 }
                 break;
             }
             
             //Remove the last node from the List. ie. LIFO ie. Stack.pop()
             Node currentNode = lifoStack.pop();
             
             //Set the state to be 1 (1 = visited)
             currentNode.state = 1;
             
             //Comapator for Alphabetical Sorting
             Comparator<Node> nodeComparator = (Node o1, Node o2) -> o1.toString().compareTo(o2.toString());
             
             //Sort the adj list of currentNode, so that stack pushes are in Alphabetical order
             Collections.sort(currentNode.adjList, nodeComparator);
             
           
             //Iterate over Adj list (already Sorted) to check if it contains Goal Node or not 
             //If Yes, Calculate path and write to file.
             //Else, push the node on the stack.
             for (Node node : currentNode.adjList) {
                 if(node.state==0 && !lifoStack.contains(node)){
                     
                     //Set the parent , used to trace the path back (shortest path)
                     node.parent = currentNode;
                    
                     //If node (in adj list) is one of the Goal Nodes
                     if(mDestinationNodes.containsKey(node.label)){
                         //Goal Node Found
                         //Find the Path weight by going upwards
                         int i=0;  
                         Node tempforPath = mDestinationNodes.get(node.label);                       
                         if(tempforPath!=null){
                             while(!mStartNode.equals(tempforPath)){
                                 i++;
                                 tempforPath = tempforPath.parent;
                                 if(tempforPath==null){
                                     break;
                                 }
                             }
                         }
                         
                         //Writes Goal Node to the file 
                         //Parses the Path Weight and writes that too
                         try{
                             fileWriter.append((mDestinationNodes.get(node.label)).toString());
                             int cost = (mStartTime+i);
                             while(cost>=24){
                                 cost = cost - 24;
                             }
                             fileWriter.append(" "+(cost)+NEWLINE);
                         } catch(IOException ex){
                             System.out.println("Exception in writing to File : "+ex);
                         }
                         return;
                     }
                     //If Not a Destination node, push on the Stack
                     lifoStack.push(node);    
                 }
             }
        }
    }
    
    private static void UCS(FileWriter fileWriter){
        try{
        fileWriter.append("None"+NEWLINE);
        }
        catch(Exception ex){
            ex.printStackTrace();;
        }
        
    }
    
    private static class Node{
        String label;
        List<Node> adjList;
        int state ;
        Node parent;
        
        public Node(String label){
            this.label = label;
            adjList = new ArrayList<>();
            state = 0;
            parent = null;
                   
        }

        @Override
        public String toString() {
            return label;
        }
    }
      
    /**
     * Used to read objects from the Input File. 
     * Doesn't represent any ACTUAL object.
     */
    private static class Test{
        String type;  //First Line of Case
        String startNode;  //Second
        String destinationNodes; //Third
        String middleNodes; //Fourth
        String[] graph; //Number of Lines followed by line num 5 == the count given in line 5
        int startTime;  //Last Line

        public Test(String type, String startNode, String destinationNodes, String middleNodes, 
                                         String[] graph, int startTime) {
            this.type = type;
            this.startNode = startNode;
            this.destinationNodes = destinationNodes;
            this.middleNodes = middleNodes;
            this.graph = graph;
            this.startTime = startTime;
        }
    }
    

    
    
}
