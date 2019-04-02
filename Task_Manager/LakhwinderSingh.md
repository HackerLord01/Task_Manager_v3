# **Lakhwinder Singh**
##### Task Manager 
##### Individual Project - Sprint 3 (Some more commands and refactoring)
##### March 7, 2018

### Design
---
The program is updated version of Individual project- Sprint 2. The program added following tools and features for the user: 

* added rename a task    
* delete a task
* user will be able to give a deeper description to each task

Program is broken into 3 files, a GUI is created by creating an interface. Main file is interacting with model file, where all our logic is stored. And mode file implements the interface and additional class such as write to text file, reading from text file. The main file only accepts user input, and returns summary according to user requirement. 


#### Design updates 
---
This is the first we have implemented the GUI with differentiating the logic completely from it. At first it was hard as new syntax commands were introduced. But as program went on the efficiency and how easy it was to keep track of code helped in finishing up the project. 

__choices for better sorting the linked list__  
Tree Set was used to iterate over tasknames, task sizes to get all the required information. Only one linked is used when program reads data from file. List is updated immediately after reading, as delete command and rename commands are introduced. A sorted list is generated. No other form of memory is used to store or manipulate data. another classes are created as entry and task, but they are only generated per task, and deleted once the info for task is recieved. 
#### Application Limitation
---
* There are few bugs in the code. The summary function provides the previos/old name of the task even after renaming it. 
* All of the sizes are shown in summary because iterating over linked list created a set of all the sizes.

#### Conclusion
---
The program was hard as new approach was introduced. Made more sense and much easier to differentiate logic from user interface. 


