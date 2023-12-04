public class ProcessEvent {
    public String taskName;
    public int memoryAmount;
    public int time;
    public ProcessEvent (String taskNameString, int memoryAmountInt, int timeInt){
        this.taskName = taskNameString;
        this.memoryAmount = memoryAmountInt;
        this.time = timeInt;
    }
}
