package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

public class ExceptionConverterStage implements Stage<Exception, SourceLocatedError> {

    @Override
    public SourceLocatedError perform(StagesData stagesData, Exception ex) {
        if (ex instanceof SourceLocatedError sle) return sle;
        return new SimpleError(ex.getMessage());
    }
}
