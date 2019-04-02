/*Lakhwinder pal Singh
CSC 131 
Prof. Posnett
03/07/2018
Sprint 3
*/
import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;

public  class TMModel implements ITMModel{
    WriteLogFile newLog = new WriteLogFile();
    ReadLogFile readLog = new ReadLogFile();
    task one_task;
    // set information for our model
    public boolean startTask(String name){
       return newLog.write(LocalDateTime.now().toString() + "\t" +  "start" + "\t" + name);
    }
    public boolean stopTask(String name){
       return newLog.write(LocalDateTime.now().toString() + "\t" +  "stop" + "\t" + name);
    }
    public boolean describeTask(String name, String description){
      return newLog.write(LocalDateTime.now().toString() + "\t" + "describe" + "\t" + name + "\t"+ description);
    }
    public boolean sizeTask(String name, String size){
       return newLog.write(LocalDateTime.now().toString() + "\t" + "size"+"\t" + name + "\t" + size); 
    }
    public boolean deleteTask(String name){
         return newLog.write(LocalDateTime.now().toString() + "\t" +  "delete" + "\t" + name);
    }
    public boolean renameTask(String oldName, String newName){
        newLog.write(LocalDateTime.now().toString() + "\t" +  "rename" + "\t" + oldName + "\t"+ newName);
        return true;    
    }
    public void update(){
        readLog.ReadFile();
    }
    // return information about our tasks(certain task)
    public String taskElapsedTime(String name){
        one_task = new task(); 
        for(int i = 0;i<readLog.entries.size();i++){
            String newname = readLog.entries.get(i).task_name;
            if(newname.equals(name)){
                switch(readLog.entries.get(i).command){
                    case "start" : one_task.task_name = name;
                                one_task.task_start_time = readLog.entries.get(i).task_time;
                                break;
                    case "stop" :  one_task.task_stop_time = readLog.entries.get(i).task_time;
                                one_task.duration.add(time_diff(one_task.task_start_time, one_task.task_stop_time));
                                    one_task.total_time = total_time(one_task.duration);
                                    break;
                    case "describe" :  if(one_task.task_start_time.isEmpty()){
                                                one_task.task_start_time = readLog.entries.get(i).task_time;
                                        }
                                        break;
                }
            }
        }        
        return one_task.total_time;
    }
    public String  taskSize(String name){
        String size = "task doent have any size";
        for(int i = 0; i<readLog.entries.size(); i++){
            if(readLog.entries.get(i).command.equals("size") && readLog.entries.get(i).task_name.equals(name)){
                size = readLog.entries.get(i).task_size;
            }
        }
        return size;
    }
    public String taskDescription(String name){
        String describe = "" ;
        for(int i = 0; i<readLog.entries.size(); i++){
            if(readLog.entries.get(i).command.equals("describe") && readLog.entries.get(i).task_name.equals(name)){
                describe +=readLog.entries.get(i).description + "\n\t\t\t\t";
            }
        }
        return describe;
    }
    public String minTimeForSize(String size){
        Set<String> all_tasks = taskNamesForSize(size);
        long min= Long.MAX_VALUE;
        long hold = 0;
        String task_name = null;
        String task_time = null;
        for(String task:all_tasks){
        String time = taskElapsedTime(task);
            hold = convertToSeconds(time);
            if(hold<min){
                min = hold;
                task_name = task;
                task_time = time;
            }
        }
        return task_name + " " + task_time;
        
    }
    public String maxTimeForSize(String size){
        Set<String> all_tasks = taskNamesForSize(size);
        long max= Long.MIN_VALUE;
        long hold = 0;
        String task_name = null;
        String task_time = null;
        for(String task:all_tasks){
        String time = taskElapsedTime(task);
            hold = convertToSeconds(time);
            if(hold>max){
                max = hold;
                task_name = task;
                task_time = time;
            }
        }
        return task_name + " " + task_time;

    }
    public String avgTimeForSize(String size){
        Set<String> all_tasks = taskNamesForSize(size);
        LinkedList<Long> intervals = new LinkedList<Long>();
        LinkedList<Long> final_time = new LinkedList<Long>();
        long seconds=0;
        int total_tasks=0;
        for(String task:all_tasks){
            total_tasks++;
            String time = taskElapsedTime(task);
            seconds = convertToSeconds(time);
            intervals.add(seconds);
        }
        String time = total_time(intervals);
        long average = convertToSeconds(time);
        average = average/total_tasks;
        final_time.add(average);
        time = total_time(final_time);
        return time;
    }
    public Set<String> taskNamesForSize(String size){
        Set<String> taskswithSize = new TreeSet<String>();
        for(int i = 0; i<readLog.entries.size(); i++){
            if(readLog.entries.get(i).task_size.equals(size)){
                taskswithSize.add(readLog.entries.get(i).task_name);
            }
        }

        return taskswithSize;
    }
    public String elapsedTimeForAllTasks(){
        Set<String> task = taskNames();
        LinkedList<Long> time_list = new LinkedList<Long>();
        String time = null;
        long seconds = 0;
        for(String task_names: task){
           time =  taskElapsedTime(task_names);
           seconds = convertToSeconds(time);
           time_list.add(seconds);
        }
        return total_time(time_list);
    }
    public Set<String> taskNames(){
         Set<String> newtask = new TreeSet<String>();
        for(int i = 0; i<readLog.entries.size(); i++){
            String line = readLog.entries.get(i).task_name;
            newtask.add(line);
        }
        return newtask;
    }      
    public Set<String> taskSizes(){
        Set<String> newSize = new TreeSet<String>();
        String size = "";
        for(int i = 0; i<readLog.entries.size();i++){
            if(readLog.entries.get(i).command.equals("size")){
                size = readLog.entries.get(i).task_size;
                newSize.add(size);
            }
        }
        return newSize;
    }
    private long convertToSeconds(String timestamp){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date dt = sdf.parse(timestamp);
            int hours = dt.getHours();
            int min = dt.getMinutes();
            int sec = dt.getSeconds();
            long total_seconds = (hours *(3600) + (min*60) + sec);
            return total_seconds ;
          } catch(ParseException e) {
            return 1;
          }
        
    }
    private String total_time(LinkedList<Long> interval){ // adds all the seconds for linked list and returns appropriate time
        String elapsed = "";
        long total = 0;
        for(int i = 0; i<interval.size();i++){
            total += interval.get(i); }
        long hours = total / 3600; // convert seconds to hours
        long minutes = (total % 3600) / 60;
        long seconds = total % 60;
        elapsed = hours+ ":" + minutes+":"+seconds;
        return elapsed;
    }  
    private long time_diff(String start, String stop){
        LocalDateTime start_time = LocalDateTime.parse(start);
        LocalDateTime stop_time = LocalDateTime.parse(stop);
        long elapsedSeconds=(ChronoUnit.SECONDS.between(start_time,stop_time));
        return elapsedSeconds;
    }  
}
// reading log file, writing to log file, entry and task classes are below -------
class Entry{
    String command = "";
    String task_name = "";
    String new_name = "";
    String task_time = "";
    String description = "";
    String task_size = "";
}

class task{
    String task_name = "";
    String task_start_time = "";
    String task_stop_time = "";
    String total_time = "00:00:00";
    String task_description = "";
    String task_size = "";
    LinkedList<Long> duration = new LinkedList<Long>();
}

class ReadLogFile{
    LinkedList<Entry> entries = new LinkedList<Entry>();
    void ReadFile(){
        Entry log ;
        String fileName = "log.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                log = new Entry();
                String[] token = line.split("\t");
                log.task_time = token[0];   // time
                log.command =token[1];      // cmd
                log.task_name = token[2];   // task name
                if(token.length == 4){      // rename, size, describe
                    if(token[1].equals("rename")){
                        log.new_name = token[3];
                    }else if(token[1].equals("size")){
                        log.task_size = token[3];
                    }else if(token[1].equals("describe")){
                        log.description = token[3];
                    }
                } else if(token.length == 5){   // describe with size
                    log.description= token[3];
                    log.task_size = token[4];
                }
                entries.add(log);               
            }   
            bufferedReader.close();         
        }catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
        sort_entries();
    }
    void sort_entries(){ // for updating delete and renaming commands 
        for(int i = 0; i<entries.size();i++){
            String task_name = entries.get(i).task_name;
            switch(entries.get(i).command){
                case "delete":  for(int j = 0;j<entries.size() ;j++){
                                     if(task_name.equals(entries.get(j).task_name)){
                                        entries.remove(j);
                                        j = -1;
                                     }
                                    }                                    
                                break;
                case "rename": for(int j = 0; j<i;j++){
                                    if(task_name.equals(entries.get(j).task_name)){
                                        entries.get(j).task_name = entries.get(i).new_name;
                                    }                        
                                }
                                entries.remove(i);  
                                i--;
                                break;
            }
        }
    }
}

class WriteLogFile{
    boolean write(String input){
        boolean flag = true;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            File file = new File("log.txt");
            if(!file.exists()){
                flag = false;
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(input + "\n");
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(bw != null) bw.close();
                if(fw != null) fw.close();
            } catch(IOException ex){
                flag = false;
            }
        }
        return flag;
    }
}