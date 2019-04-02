/*Lakhwinder pal Singh
CSC 131 
Prof. Posnett
03/07/2018
Sprint 3
*/
import java.util.*;
import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class TM {
   ITMModel Model1 = new TMModel();

    public static void main(String[] args) {
        new TM().appMain(args);
    }
     void appMain(String args[]){
        switch(args[0]){
            case "start": Model1.startTask(args[1]); break;
            case "stop":  Model1.stopTask(args[1]); break;
            case "describe":  if(args.length == 3){ 
                                    Model1.describeTask(args[1], args[2]); 
                            } else if (args.length == 4){
                                Model1.describeTask(args[1], args[2]);
                                 Model1.sizeTask(args[1],args[3]);
                                } 
                            break;       
            case "size":  Model1.sizeTask(args[1],args[2]);
                            break;
            case "summary": if(args.length == 1){
                                summary("all");
                            } else if(args.length == 2){
                                summary(args[1]);
                            }             
                            break;
            case "delete": Model1.deleteTask(args[1]);
                            break;
            case "rename": Model1.renameTask(args[1], args[2]);
                            break;
            default: usage();
                     break;
        }
    }
      
void usage(){
        System.err.println("command can be one of these: start, stop, describe, or summary.\n" +
                        "java TM start <task name>\t\t\t\t<task name> start time\n" +
                        "java TM stop <task name>\t\t\t\t<task name> stop time\n"+
                        "java TM size <task name> <task size>\t\t\tadds size of the task\n"+
                        "java TM describe <task name> <description> <size> \tdescription of the task\n"+
                        "java TM summary <task name>\t\t\t	<task name> summary\n"+
                        "java TM summary\t\t\t\t\t 	summary of all the tasks");
    }

    void summary(String input){
        Model1.update();
        Set<String> task_names =  Model1.taskNames();
        String total_time=null;
        String size=null;
        String description = null;

        if(input.equals("all")){
            for(String task: task_names){
                total_time = Model1.taskElapsedTime(task);
                size = Model1.taskSize(task);
                description = Model1.taskDescription(task);
                System.out.println("\nTask Name: "+ task +"\tSize: " + size 
                                    + "\nTime : " + total_time + "\t\tDescription: " + description +"\n" );
            }
            Set<String> sizes = Model1.taskSizes();
            for(String one_size: sizes){
                task_names = Model1.taskNamesForSize(one_size);
                if(task_names.size() >= 2){
                System.out.println("\nMin Time for size " + one_size + " = "+ Model1.minTimeForSize(one_size));
                System.out.println("Max Time for size " + one_size + " = "+ Model1.maxTimeForSize(one_size));
                System.out.println("Avg Time for size " + one_size + " = "+ Model1.avgTimeForSize(one_size));
                }
        }
        }else{
            total_time = Model1.taskElapsedTime(input);
            size = Model1.taskSize(input);
            description = Model1.taskDescription(input);
            System.out.println("\nTask Name: "+ input +"\tSize: " + size 
                                    + "\nTime : " + total_time + "\t\tDescription: " + description +"\n" );
        }

        
        
    }
}
