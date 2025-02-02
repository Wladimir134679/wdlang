package ru.wdeath.lang.utils;

import java.util.Objects;

public record Range(Pos start, Pos end) {

    public static final Range ZERO = new Range(Pos.ZERO, Pos.ZERO);

    public Range normalize() {
        return new Range(start.normalize(), end.normalize());
    }

    public boolean isEqualPosition() {
        return Objects.equals(start, end);
    }

    public boolean isOnSameLine() {
        return start.row() == end.row();
    }

    public String format() {
        if (isEqualPosition()) {
            return start.format();
        } else if (isOnSameLine()) {
            return "[%d:%d~%d]".formatted(start.row(), start.col(), end.col());
        } else {
            return start.format() + "..." + end.format();
        }
    }

}
