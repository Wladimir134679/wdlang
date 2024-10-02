package ru.wdeath.lang.module.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.module.InitModule;
import ru.wdeath.lang.module.NameExpansionModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesModule implements NameExpansionModule {

    @Override
    public String name() {
        return "files";
    }

    @Override
    public void init(InitModule init) {
        init
                .add("writeString", FilesModule::writeString)
                .add("deleteFile", FilesModule::deleteFile)
                .add("readString", FilesModule::readString);
    }

    public static Value deleteFile(ProgramContext pc, Value[] args){
        ArgumentsUtil.check(1, args.length);
        try {
            Files.delete(Path.of(args[0].asString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return NumberValue.ZERO;
    }

    public static Value writeString(ProgramContext pc, Value[] args) {
        ArgumentsUtil.check(2, args.length);
        try {
            Files.writeString(Path.of(args[0].asString()), args[1].asString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return NumberValue.ZERO;
    }

    public static Value readString(ProgramContext pc, Value[] args) {
        ArgumentsUtil.check(1, args.length);
        try {
            return new StringValue(Files.readString(Path.of(args[0].asString())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
