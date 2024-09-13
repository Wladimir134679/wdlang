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

    public static void main(String[] args) throws IOException, InterruptedException {
        //"./examples/testImport.wdl"
        extracted("./examples/testImport.wdl");
    }

    private static void extracted(String path) {
        ProgramRunnerConfig config = new ProgramRunnerConfig();
//        config.showProgramContext = true;
//        config.generateMermaidText = true;

        final var runner = new ProgramRunner(config, new InputSourceFile(path));
        runner.init();
        runner.run();
        runner.resultAndPrint();
    }
}
