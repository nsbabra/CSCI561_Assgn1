/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author nsbabra
 */
public class waterFlow {
    private static int TEST_CASES ;
    private static HashMap<String, Node> mGraph ;
    
    private static Node mStartNode ;
    private static HashMap<String, Node> mDestinationNodes;
    private static HashMap<String, Node> mMiddleNodes;
    private static int mNumPipes;
    private static int mStartTime;
    
    
    private static Test[] tests;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(Arrays.toString(args));
        if(args.length!=2){
            System.out.println("No/Bad File Specified");
            System.exit(1); 
        }
        if(!args[0].equals("-i") || !args[1].endsWith(".txt")){
            System.out.println("No/Bad File Specified 2");
            System.exit(1); 
        }

        BufferedReader reader = new BufferedReader(new FileReader(args[1]));
        try{
            TEST_CASES = Integer.parseInt(reader.readLine());

            //Number of Tests 
            tests =  new Test[TEST_CASES];

            for(int i=0; i<TEST_CASES ; i++){

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

                tests[i] = new Test(type, startNode, destinations, middleNodes, pipes, graph, startTime);
                reader.readLine(); //Empty Line
            }
        
        }catch(IOException | NumberFormatException ex){
            System.out.println("Exception in Parsing File : "+ex);
        }

        
        try{
            FileWriter filewriter = new FileWriter("output.txt");
            for (Test currentTest : tests) {
                switch(currentTest.type){
                    case "BFS":
                        BFSGraphBuilder(currentTest);
                        BFS(filewriter);
                        break;
//                    case "DFS":
//                        new DFS(currentTest,filewriter);
//                    case "UCS":
//                        new UCS(currentTest, filewriter);
                }
            }
            filewriter.close();
        }catch(Exception ex){
            System.out.println("Exception in writing to file : "+ex);
        }
        
        
        
        

    }
    
    
    private static void BFSGraphBuilder(Test currentTest){
        mGraph = new HashMap<>();
        mStartNode = new Node(currentTest.startNode);
        mStartTime = currentTest.startTime;
        mNumPipes =  currentTest.numPipes; 
        
        mGraph.put(mStartNode.label, mStartNode);
        
        mDestinationNodes = new HashMap<>();
        StringTokenizer tokenizerDest = new StringTokenizer(currentTest.destinationNodes);
        while(tokenizerDest.hasMoreTokens()){
            String currentToken = tokenizerDest.nextToken();
            Node destNode = new Node(currentToken);
            mDestinationNodes.put(currentToken, destNode);
            mGraph.put(currentToken, destNode);
        }
        
        mMiddleNodes = new HashMap<>();
        StringTokenizer tokenizerMiddle = new StringTokenizer(currentTest.middleNodes);
        while(tokenizerMiddle.hasMoreTokens()){
            String currentToken = tokenizerMiddle.nextToken();
            Node middleNode = new Node(currentToken);
            mMiddleNodes.put(currentToken, middleNode);
            mGraph.put(currentToken, middleNode);
        }
        
        
        for (String graphLine : currentTest.graph) {
            StringTokenizer tokenizer = new StringTokenizer(graphLine);
            Node node = mGraph.get(tokenizer.nextToken());
            node.adjList.add(mGraph.get(tokenizer.nextToken()));
        }
    }

    
    private static void BFS(FileWriter fileWriter){
         
         Queue fifoQueue = new LinkedList();
         fifoQueue.add(mStartNode);
         
         Queue exploredQueue = new LinkedList();
         
         
         while(true){
             if(fifoQueue.isEmpty()){
                 System.out.println("Queue Empty in BFS");
                 break;
             }
             
             Node currentNode = (Node) fifoQueue.remove();
             currentNode.state = 1;
             exploredQueue.add(currentNode);
             
             for(Node node: currentNode.adjList){
                 if(node.state==0 && !fifoQueue.contains(node)){
                     if(mDestinationNodes.containsKey(node.label)){
                          //FileWrite and RETURN;
                         //TODO : Remove inner path loop, only for debugging
                         System.out.println("Path is : ");
                         int i = 0;
                         for(i=0; i < exploredQueue.size(); i++){
                            System.out.print(((Node)exploredQueue.remove()).toString()+" ");
                         }
                         System.out.println(mDestinationNodes.get(node.label));
                         try{
                            fileWriter.append((mDestinationNodes.get(node.label)).toString());
                            fileWriter.append(" "+(mStartTime+i));
                         }
                         catch(IOException ex){
                             System.out.println("Exception in writing to File : "+ex);
                         }
                         System.out.println("Cost is : "+(mStartTime+i));
                         return;
                     }
                     fifoQueue.add(node);
                 }
             }
        }
        //Labha hi nahi.. So nothing to write ?
    }
    
    
    private static class DFS {
        public DFS(Test currentTest, FileWriter filewriter) {
        }
    }

    private static class UCS {
        public UCS(Test currentTest, FileWriter filewriter) {
        }
    }
    
    
    
    private static class Node{
        String label;
        List<Node> adjList;
        int state ;
        
        public Node(String label){
            this.label = label;
            adjList = new ArrayList<>();
            state = 0;
        }

        @Override
        public String toString() {
            return label;
        }
    }
    
    
     private static class Test{
        String type;
        String startNode;
        String destinationNodes;
        String middleNodes;
        int numPipes;
        String[] graph;
        int startTime;

        public Test(String type, String startNode, String destinationNodes, String middleNodes, 
                                        int numPipes, String[] graph, int startTime) {
            this.type = type;
            this.startNode = startNode;
            this.destinationNodes = destinationNodes;
            this.middleNodes = middleNodes;
            this.numPipes = numPipes;
            this.graph = graph;
            this.startTime = startTime;
        }
        
        
    }
    

    
    
}
