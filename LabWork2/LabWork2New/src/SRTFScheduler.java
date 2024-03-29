import java.util.ArrayList;

/** A shortest-remaining-time-first Scheduler for a CPU.
 ** It maintains a linked list of jobs.
 **
 ** This scheduler is remarkably Simulatorilar to the DiskScheduler.
 **/

public class SRTFScheduler extends Scheduler {
    /** The queue of jobs awaiting service.
     ** If the queue is empty, jobsList = null or jobsList.size()==0.
     **/
    private ArrayList<Job> queue = null;

    /** Add a new job wanting service.
     ** The second argument is the amount of time remaining before the
     ** job currently using the device will finish (-1 if the device is idle).
     ** Return TRUE if this scheduler would like to preempt the current
     ** job.
     **/
    public boolean add(Job j, int timeLeft) {
        if (queue == null) {
            queue = new ArrayList<>();
        }
        queueChanged(1);    // update queue-length statistics
        int minBurst = Integer.MAX_VALUE;
        if(queue.size() == 0) {
            queue.add(j);
            return false;
        }
        else {
            for (Job process : queue) {
                minBurst = Math.min(process.burst, minBurst);
            }
            queue.add(j);
        }

        return j.burst < minBurst;
    }

    /** Retrieve (and remove) the next job to be served.
     ** Return null if there is no such job.
     **/
    public Job remove() {
        if (queue == null || queue.size() == 0)
            return null;

        int minBurst = Integer.MAX_VALUE;
        Job result = null;
        for(Job process : queue) {
            if(process.burst < minBurst) {
                minBurst = process.burst;
                result = process;
            }
        }
        queue.remove(result);
        queueChanged(-1);
        return result;
    }

    /** This method is called when there is a clock interrupt.
     ** It returns true if there is a reason to stop the current process
     ** and run another one instead.
     ** The argument is the amount of time left until the current job
     ** finishes service on the device.
     ** It is ignored for RR scheduling (we preempt if and only if there
     ** is some other job to run).
     **/
    public boolean reschedule(Job job) {
        return (queue != null);
    }

    /** For debugging: print the queue of waiting jobs */
    public void printQueue() {
        if (queue == null || queue.size() == 0)
            Simulator.db("| CPU queue: empty");
        else {
            Simulator.db("| CPU queue:");
            for (Job job : queue) {
                Simulator.db("|    " + job);
            }
        }
    }

    /** For debugging: a concise version of the queue of waiting jobs */
    private String queueString() {
        if (queue == null || queue.size()==0)
            return "[]";
        StringBuilder res = new StringBuilder("[");
        for (Job job : queue) {
            res.append(" ").append(job);
        }
        return res + "]";
    }
} // RRScheduler
