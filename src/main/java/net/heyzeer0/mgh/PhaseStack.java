package net.heyzeer0.mgh;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;

/**
 * Created by Frani on 02/11/2017.
 */
public class PhaseStack {

    private Deque<Object> phaseStack = new ArrayDeque<>(16);

    public void push(Object o) {
        if (phaseStack.contains(o)) {
            MagiHandlers.log("Tried to add someething already on the stack: " + o + ", stacktrace: ");
            printThread();
        }
        phaseStack.addFirst(o);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getFirst(Class<T> c) {
        return (Optional<T>) phaseStack.stream().filter(c::isInstance).findFirst();
    }

    public boolean remove(Object o) {
        boolean result = phaseStack.remove(o);
        if (!result) {
            MagiHandlers.log("Tried to remove something that wasn't in the stack: " + o + ", stacktrace:");
            printThread();
        }
        return result;
    }

    public boolean ignorePhase = false;

    public void printThread() {
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement e : Arrays.copyOfRange(Thread.currentThread().getStackTrace(), 2, 8)) {
            if(sb.length() != 0) sb.append('\n');
            sb.append(e);
        }
        System.out.println(sb);
    }
}
