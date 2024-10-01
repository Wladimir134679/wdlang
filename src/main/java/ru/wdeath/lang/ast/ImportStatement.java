package ru.wdeath.lang.ast;


import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.ProgramRunner;
import ru.wdeath.lang.ProgramRunnerConfig;
import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.lib.ImportValue;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.module.ExpansionModule;
import ru.wdeath.lang.module.ProgramExpansionModuleManager;
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
            String nameModule = listImport.joinPath();
            if (isExpansionModule(nameModule))
                expansionModule(listImport, nameModule);
            else
                includeFile(listImport);
        }
        return NumberValue.ZERO;
    }

    private void expansionModule(ImportDetails listImport, String nameModule) {
        if (!getModuleManager().isExists(nameModule))
            throw new WdlRuntimeException("Not find \"" + nameModule + "\" module", range);
        ProgramContext expansion = getModuleManager().expansion(nameModule, programContext);
        extract(listImport, expansion);
    }

    private void includeFile(ImportDetails listImport) {
        try {
            ProgramContext importContext = inputAndIncludeProgram(listImport);
            extract(listImport, importContext);
        } catch (Exception e) {
            throw new WdlRuntimeException("Error import file", e, getRange());
        }
    }

    private void extract(ImportDetails listImport, ProgramContext importContext) {
        if (listImport.isExpansion()) {
            programContext.getScope().expansionScope(importContext.getScope());
        } else {
            programContext.getScope().defineVariableInCurrentScope(listImport.getWordAsName(), importContextToValue(importContext));
        }
    }

    public boolean isExpansionModule(String nameModule) {
        return getModuleManager().isExists(nameModule);
    }

    public Value importContextToValue(ProgramContext importContext) {
        return new ImportValue(importContext);
    }

    public ProgramContext inputAndIncludeProgram(ImportDetails importDetails) {
        String pathToFileImport = importNameToPathSource(importDetails.words);
        InputSource inputSource = new InputSourceFile(pathToFileImport);

        ProgramRunnerConfig config = new ProgramRunnerConfig();
        ProgramContext newContext = new ProgramContext(importDetails.toValue(), programContext);
        ProgramRunner runner = new ProgramRunner(config, inputSource, newContext);
        runner.init();
        runner.run();
        return runner.getContext();
    }

    private String importNameToPathSource(List<String> words) {
        return "./" + String.join("/", words) + ".wdl";
    }

    private ProgramExpansionModuleManager getModuleManager(){
        return programContext.getModuleManager();
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

        public boolean isExpansion() {
            return getWordAsName().equals("*");
        }

        @Override
        public String toString() {
            return toValue();
        }

        public String joinPath() {
            return String.join(".", words);
        }

        public String toValue() {
            return joinPath() + " as " + getWordAsName();
        }
    }

    @Override
    public String toString() {
        return "import " + listImports;
    }
}
