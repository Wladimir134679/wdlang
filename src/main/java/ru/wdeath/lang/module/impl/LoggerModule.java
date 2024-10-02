package ru.wdeath.lang.module.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.ArgumentsUtil;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.module.InitModule;
import ru.wdeath.lang.module.NameExpansionModule;

import javax.swing.text.DateFormatter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.ListResourceBundle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LoggerModule implements NameExpansionModule {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String DATE = "${date}",
            THREAD = "${thread}",
            LEVEL = "${level}",
            MESSAGE = "${message}",
            MODULE = "${module}";

    private String template = "log - ${message}";
    private DateTimeFormatter dateFormatter;
    private String nameLogger;

    @Override
    public String name() {
        return "logger";
    }

    @Override
    public void init(InitModule init) {
        template = "%s [%s] {%s} %s --- %s".formatted(DATE, THREAD, LEVEL, MODULE, MESSAGE);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        nameLogger = init.rootContext().getName();
        init
                .add("init", this::initLog)
                .add("info", this::info)
                .add("debug", this::debug)
                .add("error", this::error)
                .add("warn", this::warn);
    }

    public Value initLog(ProgramContext pc, Value[] values){
        ArgumentsUtil.checkRange(1, 2, values.length);
        nameLogger = values[0].asString();
        if(values.length > 1)
            template = values[1].asString();
        return NumberValue.ZERO;
    }

    public Value info(ProgramContext pc, Value[] values) {
        log(pc, "info", createMessage(values));
        return NumberValue.ZERO;
    }

    public Value debug(ProgramContext pc, Value[] values) {
        log(pc, "debug", createMessage(values));
        return NumberValue.ZERO;
    }

    public Value warn(ProgramContext pc, Value[] values) {
        logError(pc, "warn", createMessage(values));
        return NumberValue.ZERO;
    }

    public Value error(ProgramContext pc, Value[] values) {
        logError(pc, "error", createMessage(values));
        return NumberValue.ZERO;
    }

    public String createMessage(Value[] values) {
        return Arrays.stream(values).map(Value::asString).collect(Collectors.joining());
    }

    public void log(ProgramContext pc, String level, String message) {
        String result = template
                .replace(DATE, dateFormatter.format(LocalDateTime.now()))
                .replace(THREAD, Thread.currentThread().getName())
                .replace(LEVEL, ANSI_GREEN + level + ANSI_RESET)
                .replace(MODULE, ANSI_BLUE + nameLogger + ANSI_RESET)
                .replace(MESSAGE, message);
        pc.getConsole()
                .println(result);
    }

    public void logError(ProgramContext pc, String level, String message) {
        String result = template
                .replace(DATE, dateFormatter.format(LocalDateTime.now()))
                .replace(THREAD, Thread.currentThread().getName())
                .replace(LEVEL, ANSI_RED + level + ANSI_RESET)
                .replace(MODULE, ANSI_BLUE + nameLogger + ANSI_RESET)
                .replace(MESSAGE, ANSI_RED + message + ANSI_RESET);
        pc.getConsole()
                .println(result);
    }
}
