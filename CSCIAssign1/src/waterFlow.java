/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

/**
 *
 * @author nsbabra
 */
public class waterFlow {
    private static int TEST_CASES ;
    private static HashMap<String, Node> mGraph ;
    
    private static Test[] tests;
    
    
    public waterFlow(){
        mGraph = new HashMap<>();
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length!=2){
            System.out.println("No/Bad File Specified");
            System.exit(1); 
        }
        if(!args[0].equals("-i") || !args[1].endsWith(".txt")){
            System.out.println("No/Bad File Specified");
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
        

        /**
         * Should Match the Input File.
         * TODO :  Remove it after debugging
         */
        for(int i=0; i<tests.length; i++){
            System.out.println("Test ["+i+"]");
            Test test = tests[i];
            System.out.println(test.type);
            System.out.println(test.startNode);
            System.out.println(test.destinationNodes);
            System.out.println(test.middleNodes);
            System.out.println(test.numPipes+" ");
            System.out.println(Arrays.toString(test.graph));
            System.out.println(test.startTime);
            System.out.println("");
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
    
    private static void BFS(BufferedReader reader){
        
    }
    
    
    
    private class Node{
        String mlabel;
        List<Node> mAdjList;
    }
    
    
    
}
