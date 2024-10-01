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
        log(pc, "warn", createMessage(values));
        return NumberValue.ZERO;
    }

    public String createMessage(Value[] values) {
        return Arrays.stream(values).map(Value::asString).collect(Collectors.joining());
    }

    public void log(ProgramContext pc, String level, String message) {
        String result = template
                .replace(DATE, dateFormatter.format(LocalDateTime.now()))
                .replace(THREAD, Thread.currentThread().getName())
                .replace(LEVEL, level)
                .replace(MODULE, nameLogger)
                .replace(MESSAGE, message);
        pc.getConsole()
                .println(result);
    }
}
