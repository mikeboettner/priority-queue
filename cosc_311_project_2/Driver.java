package cosc_311_project_2;

import Project_2_Class_Package.PriorityQueue;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author      Michael Boettner
 * @version     1.0
 * Driver class contains main method for Job Scheduling Simulation. Generates
 * desired number of jobs, and runs them all to completion, outputting
 * run-time data pertaining to each job
 */
public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /**
        * Constant number of jobs to be generated and run
        */
        final int NUMBER_OF_JOBS = 6;
        
        /**
        * Used to control loop which runs all jobs
        */
        boolean jobsRemain = true;
        
        /**
        * String divider used to format data output
        */
        String div = "************";
        
        /**
        * Date object used to output current date at runtime
        */
        Date currentDate = new Date();
        
        /**
        * Sets date format
        */
        SimpleDateFormat myDate =
                new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        
        /**
        * PriorityQueue object to store jobs for simulation
        */
        PriorityQueue myJobSchedulingPQ = new PriorityQueue();
        
        //generates NUMBER_OF_JOBS job Nodes and stores in PriorityQueue
        for(int i = 0; i < NUMBER_OF_JOBS; i++)
        {
            myJobSchedulingPQ.generateJob();
        }
        
        //output title and date
        System.out.println("Job Scheduling Simulation  " +
                myDate.format(currentDate));
        
        //output formatted table header
        System.out.printf("%-12s %-12s %-12s %-12s %n", div, div, div, div);
        System.out.printf("%-12s %-12s %-12s %-12s %n", "Time", "Job ID",
                "Priority", "Time To Completion");
        System.out.printf("%-12s %-12s %-12s %-12s %n", div, div, div, div);
        
        //run all jobs in the PriorityQueue
        while(jobsRemain)
        {
            jobsRemain = myJobSchedulingPQ.runJob();
        }
    } 
}