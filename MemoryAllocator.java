import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
public class MemoryAllocator {
    public static void main(String[] args) {
        //args:
        //arg 1: filename for process list
        //arg 2: number of bytes in memory
        //arg 3: allocation algorithm to use
        //arg 4: last time step in process list
        ProcessEvent[] processEvents = new ProcessEvent[Integer.parseInt(args[3])];
        try {
            File file = new File(args[0]);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] sections = data.split(" ");
                ProcessEvent nextEvent = new ProcessEvent(sections[0], Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                if (Integer.parseInt(sections[2]) < Integer.parseInt(args[3]))
                    processEvents[Integer.parseInt(sections[2])] = nextEvent;
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Failed to find file");
            System.exit(1);
        }
        switch (args[2]) {
            case "ff":
                FirstFit(processEvents, Integer.parseInt(args[1]));
                break;
            default:
                System.out.println("Unknown allocation type");
                break;
        }
        
    }
    //memory allocation algorithms
    public static void FirstFit(ProcessEvent[] events, int memoryAmount) {
        HashMap<Integer, Integer> free = new HashMap<Integer, Integer>();
        free.put(0, memoryAmount);
        HashMap<String, Integer> processTable = new HashMap<String, Integer>();
        HashMap<String, Integer> processSizes = new HashMap<String, Integer>();
        int c = -1;
        for (ProcessEvent event : events) {
            c++;
            System.out.println("Time step: " + c);
            if (event == null) {
                continue;
            }
            if (processTable.containsKey(event.taskName)) { //if process already in table, remove it
                int newFree = 0;
                int location = processTable.get(event.taskName);
                int size = processSizes.get(event.taskName);
                if (free.containsKey(location + size)) { //if there's some memory free after, combine them
                    free.put(location, size + free.get(location + size));
                    free.remove(location + size - 1);
                    newFree++;
                }
                int beforeSpot = 0;
                for (int freeSpot : free.keySet()) { //if there's some memory free before, combine them
                    if (freeSpot + free.get(freeSpot) == location) {
                        free.replace(freeSpot, free.get(freeSpot) + size);
                        newFree++;
                        beforeSpot = freeSpot;
                        break;
                    }
                }
                if (newFree == 0) { //otherwise, make new entry in free
                    free.put(location, size);
                } else if (newFree == 2) { //if there was a free slot both before and after, combine them again
                    free.replace(beforeSpot, free.get(location + size) + free.get(beforeSpot));
                    free.remove(location + size);
                }
                processTable.remove(event.taskName);
            }
            if (event.memoryAmount <= 0) {
                for (int freeSpot : free.keySet()) {
                    System.out.println(freeSpot);
                    System.out.println(free.get(freeSpot));
                }
                for (String process : processTable.keySet()) {
                    System.out.println(process);
                    System.out.println(processTable.get(process));
                }
                continue;
            }
            processSizes.put(event.taskName, event.memoryAmount);
            boolean found = false;
            for (int freeSpot : free.keySet()) {
                if (free.get(freeSpot) >= event.memoryAmount) {
                    processTable.put(event.taskName, free.get(freeSpot) + freeSpot - event.memoryAmount);
                    free.replace(freeSpot, free.get(freeSpot) - event.memoryAmount);
                    if (free.get(freeSpot) == 0) {
                        free.remove(freeSpot);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Ran out of memory");
                System.exit(1);
            }
            for (int freeSpot : free.keySet()) {
                System.out.println(freeSpot);
                System.out.println(free.get(freeSpot));
            }
            for (String process : processTable.keySet()) {
                System.out.println(process);
                System.out.println(processTable.get(process));
            }
        }
    }
}