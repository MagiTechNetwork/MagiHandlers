package net.heyzeer0.mgh;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Frani on 02/11/2017.
 */
public class PhaseStack {

    private Deque<Object> phaseStack = new ArrayDeque<>(16);

    private Collection<Object> getStack() {
        return phaseStack;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getFrom(Class<T> c) {
        return (List<T>) phaseStack.stream().filter(o -> o.getClass() == c).collect(Collectors.toList());
    }

    public void push(Object o) {
        phaseStack.addFirst(o);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getFirst(Class<T> c) {
        return (Optional<T>) phaseStack.stream().filter(o -> o.getClass() == c).findFirst();
    }

    public boolean remove(Object o) {
        return phaseStack.remove(o);
    }

}
