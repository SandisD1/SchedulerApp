# Scheduler Application

## To start the application: 
- run the `SchedulerApplication` class located in the `src/main/java/io/task/scheduler/` package.

## To change the application run-cycle: 
- modify the `scheduler.cycle-interval` value in the `application.properties` file.
- It is located in the `src/main/resources/` package.
- the value has to be a decimal numeric value of the desired time between each run-cycle in milliseconds.
- The value is one minute or 60000 milliseconds  by default.

## To add custom input values for time and days:
- Modify the `schedule.csv` file located in the `src/main/resources/input/` package.
- The first column contains a time in the HH:MI format specifying time when the action should be executed.
- The second column contains a bitmask specified in numeric format,  
representing days of the week when the action should be made.
- The second column accepts both, decimal values from 1 to 127 and Hexadecimal values from 0x01 to 0x7F.
- The columns need to be seperated by a coma.

## Output:
- A task will be executed if one of the values in the `schedule.csv` file  
match the current time and day of week in Lagos, Nigeria.
- If a task should be executed, the program will log `executing scheduled task`.
- If no entries match the current time and day in Lagos, the program will log `nothing to execute`.