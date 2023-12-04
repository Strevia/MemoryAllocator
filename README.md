# MemoryAllocator for CS630
To run, run the command `java MemoryAllocator.java <config file> <memory amount> <allocation type> <number of time steps>`

config file - the file which will define when processes are spawned and how much memory is used (see example.txt for an example)

memory amount - how much memory the system will have

allocation type - which allocation algorithm to use. currently has: ff (first fit)

number of time steps - how many time steps to run the system for. any further events defined in the config file will be ignored

## config file
The config file should contain a list of process events, one per line. The process events should contain:

process name - String for name of process

amount of memory for that process - Integer

Time of the event - Integer

If two events have the same time, only the last one will be used. If the process name is of a process that has been spawned already, it will be first be removed from memory and then a new one will be allocated with the new memory amount.
