package Project_2_Class_Package;
import java.util.ArrayList;

/**
 * @author      Michael Boettner
 * @version     1.0
 * PriorityQueue class using ArrayList storage of Node objects to simulate
 * job scheduling in an operating system.
 */
public class PriorityQueue
{
    /**
     * @author      Michael Boettner
     * @version     1.0
     * Inner Node class allows for new Node creation and initialization of
     * data pertaining to a job to be run by an operating system.
     */
    private class Node
    {
        /**
        * Represents the priority of the job, where 1 is the highest priority
        */
        private final int priority;
        
        /**
        * Number of time units to complete the run of the job
        */
        private int timeUnitsRemaining;
        
        /**
        * Unique identifying number for each job
        */
        private final int jobID;       
        
        /**
        * Constructor initializes new Node according to project guidelines
        */
        private Node()
        {
            this.timeUnitsRemaining = 10;
            this.jobID = IDAssigner++;
            //randomly assigns a priority from 1 - 4
            this.priority = (int)Math.ceil(Math.random() * 4);
        }
    }
    
    /**
    * ArrayList used to implement priority queue of Node objects
    */
    private final ArrayList<Node> myArrayList;
    
    /**
    * Simulated run time
    */
    private static int clock = 0;
    
    /**
    * Used to assign each job's unique ID
    */
    private static int IDAssigner = 1;
    
    /**
    * Represents the length of time given to each job within a context switch
    */
    private final int timeSlice = 3;
    
    /**
    * Constructor initializes new PriorityQueue (ArrayList implementation)
    */
    public PriorityQueue()
    {
        myArrayList = new ArrayList<>();
    }
    
    /**
    * Generates a new job Node to be added to the priority queue 
    */
    public void generateJob()
    {
        offer(new Node());
    }
    
    /**
    * Adds a Node to priority queue in correct order according to its
    * priority value
    * 
    * @param nodeToAdd The node to be added to the priority queue
    */
    private void offer(Node nodeToAdd)
    {   
        //first we place the node at the end of the ArrayList implementation
        myArrayList.add(nodeToAdd);
        
        /**
        * The calculated index value in the ArrayList of the newly added node
        */
        int child = myArrayList.size() - 1;
        
        /**
        * The calculated index value in the ArrayList of the newly added
        * node's parent
        */
        int parent = (child - 1) / 2;
        
        /*this loop will test and fix the order of the nodes based on
            priority value comparison. The loop will execute as long as
            the node is not at the root AND it has a lower priority value
            than its parent*/
        while(parent >= 0 && parentPriorityIsGreater(parent, child))
        {
            swap(parent, child);
            
            /*re-assign child and parent arraylist indexes to reflect the
                change made by the swap */
            child = parent;
            parent = (child - 1) / 2;
        }    
    }
    
    /**
    * Removes and processes a single job Node for the duration of a timeslice. 
    * Updates the Node's data, and re-places it on the priority queue. If the
    * job completes in less time than a timeslice, the method returns,
    * allowing the next job to be run immediately.
    * 
    * @return boolean, true if a job Node exists and has been successfully
    * processed, false if the priority queue is empty
    */
    public boolean runJob()
    {
        /**
        * ArrayList index of the parent Node (initialized to root location)
        */
        int parent = 0;
        
        /**
        * Calculated ArrayList index of the left child Node
        */
        int leftChild;
        
        /**
        * Calculated ArrayList index of the right child Node
        */
        int rightChild;
        
        /**
        * ArrayList index of the minimum priority child Node
        */
        int minChild;
        
        /**
        * Priority value of the left child Node
        */
        int leftPriority;
        
        /**
        * Priority value of the right child Node
        */
        int rightPriority;
        
        /**
        * Priority value of the minimum priority child Node
        */
        int minPriority;
        
        /**
        * Priority value of the parent Node 
        */
        int parentPriority;
        
        /**
        * A reference to the top of the heap (highest priority item)
        */
        Node topOfHeap;
        
        //if the list is empty, there are no jobs remaining to be run
        if(myArrayList.isEmpty())
            return false;

        //save a reference to the top priority job Node
        topOfHeap = myArrayList.get(0);
        
        //output current top priority job data 
        System.out.printf("%-12s %-12s %-12s %-12s %n", clock,
                topOfHeap.jobID, topOfHeap.priority,
                    topOfHeap.timeUnitsRemaining);
        
        //if only one job Node in the list, remove it
        if(myArrayList.size() == 1)
            myArrayList.remove(0);
        /*else ArrayList size > 1, so we must remove the Node, while 
            first maintaining structure of the list (remove last item and
            place it at the top), then fixing list order according to
            priority queue rules*/
        else
        {
            //remove the last Node and set it to the first Node position
            myArrayList.set(0, myArrayList.remove(myArrayList.size() - 1));
            
            //repeated algorithm to fix the order of the list
            while(true)
            {
                //calculate the index values of left and right child
                leftChild = (2 * parent) + 1;
                rightChild = leftChild + 1;

                /*if true, the list only contains one item. No fixing
                    is necessary*/
                if(leftChild >= myArrayList.size())
                    break;
                //if true, the parent only has one child, which is the left
                else if (rightChild >= myArrayList.size())
                {
                    //set priority and index values accordingly for comparison
                    parentPriority = myArrayList.get(parent).priority;
                    minPriority = myArrayList.get(leftChild).priority;
                    minChild = leftChild;
                }
                //else, the parent has two children, so all must be compared
                else
                {
                    //set priority values for comparison
                    parentPriority = myArrayList.get(parent).priority;
                    leftPriority = myArrayList.get(leftChild).priority;
                    rightPriority = myArrayList.get(rightChild).priority;

                    /*first, assume that the left child has the smaller
                        priority value*/
                    minChild = leftChild;
                    minPriority = leftPriority;
                    
                    /*then test against the right, and re-assign the mins if
                        necessary*/
                    if(rightPriority < leftPriority)
                    {
                        minChild = rightChild;
                        minPriority = rightPriority;
                    }
                }

                //swap the nodes if the parent's priority value is higher
                if(parentPriority > minPriority)
                {
                    swap(parent, minChild);
                    /*re-assign index values before looping back to re-test
                        the order of the list*/
                    parent = minChild;
                }
                else
                    break;        
            }
        }
        
        /*
        "run" the current job for timeSlice units of time
            (timeSlice = 3, per project guidelines)
        */
        for(int quantum = timeSlice; quantum > 0; quantum--)
        {
            /*if the job has not yet been completed, decrement 1 unit and
                increment the clock*/
            if(topOfHeap.timeUnitsRemaining > 0)
            {
                topOfHeap.timeUnitsRemaining =
                        topOfHeap.timeUnitsRemaining - 1;
                clock++;
            }
            else
                break;         
        }
        //if the job is not yet complete, re-place it on the priority queue
        if(topOfHeap.timeUnitsRemaining > 0)
            offer(topOfHeap);
        return true;
    }

    /**
    * Swaps index references between two Nodes in the ArrayList
    * 
    * @param parent The ArrayList index of the parent Node
    * @param child The ArrayList index of the child Node
    */
    private void swap(int parent, int child)
    {
        /**
        * reference to the parent node being swapped 
        */
        Node parentTemp = myArrayList.get(parent);
        
        myArrayList.set(parent, myArrayList.get(child));
        myArrayList.set(child, parentTemp);
    }
    
    /**
    * Tests the priority values between two Nodes
    * 
    * @param parent The ArrayList index of the parent Node
    * @param child The ArrayList index of the child Node
    * 
    * @return boolean, true if the parent (first parameter) has a greater
    * priority value than the child (second parameter)
    */
    private boolean parentPriorityIsGreater(int parent, int child)
    {
        /**
        * priority value of the parent Node 
        */
        int parentPriority = myArrayList.get(parent).priority;
        
        /**
        * priority value of the child Node 
        */
        int childPriority = myArrayList.get(child).priority;
        
        return (parentPriority) > (childPriority);
    }
    
    /**
    * Tests and returns whether the priority queue is empty
    * 
    * @return boolean, true if the the ArrayList implementation of the
    * priority queue is empty
    */
    public boolean isEmpty()
    {
        return (myArrayList.isEmpty());
    }
}