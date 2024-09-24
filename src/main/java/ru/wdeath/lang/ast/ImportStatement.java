package ru.wdeath.lang.ast;


import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.ProgramRunner;
import ru.wdeath.lang.ProgramRunnerConfig;
import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.lib.ImportValue;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.utils.Input.InputSource;
import ru.wdeath.lang.utils.Input.InputSourceFile;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ImportStatement implements Statement, SourceLocation {

    public final ProgramContext programContext;
    public final Range range;
    public final List<ImportDetails> listImports;

    public ImportStatement(ProgramContext programContext, List<ImportDetails> listImports, Range range) {
        this.programContext = programContext;
        this.range = range;
        this.listImports = listImports;
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public Value eval() {
        for (ImportDetails listImport : listImports) {
            try{
                ProgramContext importContext = inputAndIncludeProgram(listImport);
                programContext.getScope().defineVariableInCurrentScope(listImport.getWordAsName(), importContextToValue(importContext));
            } catch (Exception e) {
                throw new WdlRuntimeException("Error import file", e, getRange());
            }
        }
        return NumberValue.ZERO;
    }

    public Value importContextToValue(ProgramContext importContext) {
        return new ImportValue(importContext);
    }

    public ProgramContext inputAndIncludeProgram(ImportDetails importDetails) {
        String pathToFileImport = importNameToPathSource(importDetails.words);
        InputSource inputSource = new InputSourceFile(pathToFileImport);

        ProgramRunnerConfig config = new ProgramRunnerConfig();
        ProgramRunner runner = new ProgramRunner(config, inputSource);
        runner.getContext().setConsole(programContext.getConsole());
        runner.init();
        runner.run();
        return runner.getContext();
    }

    private String importNameToPathSource(List<String> words) {
        return "./" + String.join("/", words) + ".wdl";
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    public static class ImportDetails {
        public List<String> words = new ArrayList<>();
        public String asName;


        public String getWordAsName() {
            return asName == null ? words.get(words.size() - 1) : asName;
        }

        @Override
        public String toString() {
            return words.toString() + " AS " + getWordAsName();
        }
    }

    @Override
    public String toString() {
        return "Import " + listImports;
    }
}
