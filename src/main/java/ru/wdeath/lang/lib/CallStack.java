package ru.wdeath.lang.lib;

import ru.wdeath.lang.utils.Range;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class CallStack {

    private static final Deque<CallInfo> calls = new ConcurrentLinkedDeque<>();
    ;

    public static synchronized void enter(String name, Function function, Range range) {
        calls.push(new CallInfo(name, function.toString(), range));
    }

    public static synchronized void exit() {
        calls.pop();
    }

    public static synchronized Deque<CallInfo> getCalls() {
        return calls;
    }

    public static String getFormattedCalls() {
        return calls.stream()
                .map(CallInfo::format)
                .collect(Collectors.joining("\n"));
    }

    public record CallInfo(String name, String function, Range range) {

        String format() {
            return "\tat " + this;
        }

        @Override
        public String toString() {
            if(range == null)
                return String.format("%s: %s", name, function);
            return String.format("%s: %s %s", name, function, range.format());
        }
    }
}
