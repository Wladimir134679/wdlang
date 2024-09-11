package ru.wdeath.lang;

import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.outputsettings.OutputSettings;
import ru.wdeath.lang.outputsettings.StringOutputSettings;
import ru.wdeath.lang.parser.linters.LinterStage;
import ru.wdeath.lang.parser.opttimization.OptimizationStage;
import ru.wdeath.lang.stages.ScopedStageFactory;
import ru.wdeath.lang.stages.StagesDataMap;
import ru.wdeath.lang.stages.impl.*;
import ru.wdeath.lang.utils.Console;
import ru.wdeath.lang.utils.Input.InputSourceFile;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.TimeMeasurement;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        ProgramRunnerConfig config = new ProgramRunnerConfig();
//        config.levelOptimization = 20;
//        config.optimization = true;
//        config.showOptimization = false;

        final var runner = new ProgramRunner(config, new InputSourceFile("./examples/classes.wdl"));
        runner.init();
        runner.run();
        runner.resultAndPrint();
    }
}
