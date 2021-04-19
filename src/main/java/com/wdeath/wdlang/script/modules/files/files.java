package com.wdeath.wdlang.script.modules.files;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.modules.Module;
import com.wdeath.wdlang.script.exceptions.ArgumentsMismatchException;
import com.wdeath.wdlang.script.lib.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public final class files implements Module {

    private static Map<Integer, FileInfo> files;

    public static void initConstants(ScriptProgram scriptProgram) {
        scriptProgram.getVariables().define("FILES_COMPARATOR", new FunctionValue(new filesComparatorFunction()));
    }

    @Override
    public void init(ScriptProgram scriptProgram) {
        files = new HashMap<>();
        initConstants(scriptProgram);

        scriptProgram.getFunctions().set("fopen", new fopen());
        scriptProgram.getFunctions().set("flush", new flush());
        scriptProgram.getFunctions().set("fclose", new fclose());
        
        // Operations
        scriptProgram.getFunctions().set("copy", new copy());
        scriptProgram.getFunctions().set("delete", fileToBoolean(File::delete));
        scriptProgram.getFunctions().set("listFiles", new listFiles());
        scriptProgram.getFunctions().set("mkdir", fileToBoolean(File::mkdir));
        scriptProgram.getFunctions().set("mkdirs", fileToBoolean(File::mkdirs));
        scriptProgram.getFunctions().set("rename", new rename());

        // Permissions and statuses
        scriptProgram.getFunctions().set("canExecute", fileToBoolean(File::canExecute));
        scriptProgram.getFunctions().set("canRead", fileToBoolean(File::canRead));
        scriptProgram.getFunctions().set("canWrite", fileToBoolean(File::canWrite));
        scriptProgram.getFunctions().set("isDirectory", fileToBoolean(File::isDirectory));
        scriptProgram.getFunctions().set("isFile", fileToBoolean(File::isFile));
        scriptProgram.getFunctions().set("isHidden", fileToBoolean(File::isHidden));
        scriptProgram.getFunctions().set("setExecutable", new setExecutable());
        scriptProgram.getFunctions().set("setReadable", new setReadable());
        scriptProgram.getFunctions().set("setReadOnly", new setReadOnly());
        scriptProgram.getFunctions().set("setWritable", new setWritable());

        scriptProgram.getFunctions().set("exists", fileToBoolean(File::exists));
        scriptProgram.getFunctions().set("fileSize", new fileSize());
        scriptProgram.getFunctions().set("getParent", new getParent());
        scriptProgram.getFunctions().set("lastModified", new lastModified());
        scriptProgram.getFunctions().set("setLastModified", new setLastModified());

        // IO
        scriptProgram.getFunctions().set("readBoolean", new readBoolean());
        scriptProgram.getFunctions().set("readByte", new readByte());
        scriptProgram.getFunctions().set("readBytes", new readBytes());
        scriptProgram.getFunctions().set("readAllBytes", new readAllBytes());
        scriptProgram.getFunctions().set("readChar", new readChar());
        scriptProgram.getFunctions().set("readShort", new readShort());
        scriptProgram.getFunctions().set("readInt", new readInt());
        scriptProgram.getFunctions().set("readLong", new readLong());
        scriptProgram.getFunctions().set("readFloat", new readFloat());
        scriptProgram.getFunctions().set("readDouble", new readDouble());
        scriptProgram.getFunctions().set("readUTF", new readUTF());
        scriptProgram.getFunctions().set("readLine", new readLine());
        scriptProgram.getFunctions().set("readText", new readText());
        scriptProgram.getFunctions().set("writeBoolean", new writeBoolean());
        scriptProgram.getFunctions().set("writeByte", new writeByte());
        scriptProgram.getFunctions().set("writeBytes", new writeBytes());
        scriptProgram.getFunctions().set("writeChar", new writeChar());
        scriptProgram.getFunctions().set("writeShort", new writeShort());
        scriptProgram.getFunctions().set("writeInt", new writeInt());
        scriptProgram.getFunctions().set("writeLong", new writeLong());
        scriptProgram.getFunctions().set("writeFloat", new writeFloat());
        scriptProgram.getFunctions().set("writeDouble", new writeDouble());
        scriptProgram.getFunctions().set("writeUTF", new writeUTF());
        scriptProgram.getFunctions().set("writeLine", new writeLine());
        scriptProgram.getFunctions().set("writeText", new writeText());
    }

    private static class filesComparatorFunction implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(2, args.length);
            
            final int fd1 = args[0].asInt();
            final int fd2 = args[1].asInt();
            if (!files.containsKey(fd1)) {
                return NumberValue.of(files.containsKey(fd2) ? 1 : 0);
            }
            if (!files.containsKey(fd2)) {
                return NumberValue.of(files.containsKey(fd1) ? -1 : 0);
            }

            final File file1 = files.get(fd1).file;
            final File file2 = files.get(fd2).file;
            return NumberValue.of(file1.compareTo(file2));
        }
    }
    
    private static class fopen implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(1, args.length);
            
            final File file = new File(args[0].asString());
            try {
                if (args.length > 1) {
                    return process(file, args[1].asString().toLowerCase());
                }
                return process(file, "r");
            } catch (IOException ioe) {
                return NumberValue.MINUS_ONE;
            }
        }
        
        private Value process(File file, String mode) throws IOException {
            DataInputStream dis = null;
            BufferedReader reader = null;
            if (mode.contains("rb")) {
                dis = new DataInputStream(new FileInputStream(file));
            } else if (mode.contains("r")) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            }
            
            DataOutputStream dos = null;
            BufferedWriter writer = null;
            final boolean append = mode.contains("+");
            if (mode.contains("wb")) {
                dos = new DataOutputStream(new FileOutputStream(file, append));
            } else if (mode.contains("w")) {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
            }
            
            final int key = files.size();
            files.put(key, new FileInfo(file, dis, dos, reader, writer));
            return NumberValue.of(key);
        }
    }
    
    private abstract static class FileFunction implements Function {
        
        @Override
        public Value execute(Value... args) {
            if (args.length < 1) throw new ArgumentsMismatchException("File descriptor expected");
            final int key = args[0].asInt();
            try {
                return execute(files.get(key), args);
            } catch (IOException ioe) {
                return NumberValue.MINUS_ONE;
            }
        }
        
        protected abstract Value execute(FileInfo fileInfo, Value[] args) throws IOException;
    }

    private static class listFiles extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return ArrayValue.of(fileInfo.file.list());
        }
    }

    private static class copy implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            try {
                final FileInputStream is = new FileInputStream(fileFrom(args[0]));
                final FileOutputStream os = new FileOutputStream(fileFrom(args[1]));
                final FileChannel ic = is.getChannel();
                ic.transferTo(0, ic.size(), os.getChannel());
                is.close();
                os.close();
                return NumberValue.ONE;
            } catch (IOException ioe) {
                return NumberValue.MINUS_ONE;
            }
        }
    }

    private static class rename implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            return NumberValue.fromBoolean( fileFrom(args[0]).renameTo(fileFrom(args[1])) );
        }
    }
    
    private static class fileSize extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.file.length());
        }
    }

    private static class getParent extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final String parent = fileInfo.file.getParent();
            return new StringValue(parent == null ? "" : parent);
        }
    }

    private static class lastModified extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.file.lastModified());
        }
    }

    private static class setLastModified extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final long time;
            if (args[1].type() == Types.NUMBER) {
                time = ((NumberValue)args[1]).asLong();
            } else {
                time = (long) args[1].asNumber();
            }
            return NumberValue.fromBoolean(fileInfo.file.setLastModified(time));
        }
    }

    private static class setReadOnly extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.fromBoolean(fileInfo.file.setReadOnly());
        }
    }

    private static class setExecutable extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final boolean ownerOnly = (args.length < 3) || (args[2].asInt() != 0);
            return NumberValue.fromBoolean(
                    fileInfo.file.setExecutable(args[1].asInt() != 0, ownerOnly));
        }
    }

    private static class setReadable extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final boolean ownerOnly = (args.length < 3) || (args[2].asInt() != 0);
            return NumberValue.fromBoolean(
                    fileInfo.file.setReadable(args[1].asInt() != 0, ownerOnly));
        }
    }

    private static class setWritable extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final boolean ownerOnly = (args.length < 3) || (args[2].asInt() != 0);
            return NumberValue.fromBoolean(
                    fileInfo.file.setWritable(args[1].asInt() != 0, ownerOnly));
        }
    }
    
    private static class readBoolean extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.fromBoolean(fileInfo.dis.readBoolean());
        }
    }
    
    private static class readByte extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readByte());
        }
    }
    
    private static class readBytes extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final ArrayValue array = (ArrayValue) args[1];
            int offset = 0, length = array.size();
            if (args.length > 3) {
                offset = args[2].asInt();
                length = args[3].asInt();
            }
            
            final byte[] buffer = new byte[length];
            final int read = fileInfo.dis.read(buffer, 0, length);
            for (int i = 0; i < read; i++) {
                array.set(offset + i, NumberValue.of(buffer[i]));
            }
            return NumberValue.of(read);
        }
    }
    
    private static class readAllBytes extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final int bufferSize = 4096;
            final byte[] buffer = new byte[bufferSize];
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read;
            while ((read = fileInfo.dis.read(buffer, 0, bufferSize)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            baos.close();
            return ArrayValue.of(baos.toByteArray());
        }
    }
    
    private static class readChar extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of((short)fileInfo.dis.readChar());
        }
    }
    
    private static class readShort extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readShort());
        }
    }
    
    private static class readInt extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readInt());
        }
    }
    
    private static class readLong extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readLong());
        }
    }
    
    private static class readFloat extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readFloat());
        }
    }
    
    private static class readDouble extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readDouble());
        }
    }
    
    private static class readUTF extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return new StringValue(fileInfo.dis.readUTF());
        }
    }
    
    private static class readLine extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return new StringValue(fileInfo.reader.readLine());
        }
    }
    
    private static class readText extends FileFunction {

        private static final int BUFFER_SIZE = 4096;

        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final StringBuilder result = new StringBuilder();
            final char[] buffer = new char[BUFFER_SIZE];
            int read;
            while ((read = fileInfo.reader.read(buffer, 0, BUFFER_SIZE)) != -1) {
                result.append(buffer, 0, read);
            }
            return new StringValue(result.toString());
        }
    }
    
    
    private static class writeBoolean extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeBoolean(args[1].asInt() != 0);
            return NumberValue.ONE;
        }
    }
    
    private static class writeByte extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeByte((byte) args[1].asInt());
            return NumberValue.ONE;
        }
    }

    private static class writeBytes extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final ArrayValue array = (ArrayValue) args[1];
            int offset = 0, length = array.size();
            final byte[] bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                bytes[i] = (byte) (array.get(i).asInt() & 0xFF);
            }
            if (args.length > 3) {
                offset = args[2].asInt();
                length = args[3].asInt();
            }
            fileInfo.dos.write(bytes, offset, length);
            return NumberValue.ONE;
        }
    }
    
    private static class writeChar extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final char ch = (args[1].type() == Types.NUMBER)
                    ? ((char) args[1].asInt())
                    : args[1].asString().charAt(0);
            fileInfo.dos.writeChar(ch);
            return NumberValue.ONE;
        }
    }
    
    private static class writeShort extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeShort((short) args[1].asInt());
            return NumberValue.ONE;
        }
    }
    
    private static class writeInt extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeInt(args[1].asInt());
            return NumberValue.ONE;
        }
    }
    
    private static class writeLong extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final long value;
            if (args[1].type() == Types.NUMBER) {
                value = ((NumberValue)args[1]).asLong();
            } else {
                value = (long) args[1].asNumber();
            }
            fileInfo.dos.writeLong(value);
            return NumberValue.ONE;
        }
    }
    
    private static class writeFloat extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final float value;
            if (args[1].type() == Types.NUMBER) {
                value = ((NumberValue)args[1]).asFloat();
            } else {
                value = (float) args[1].asNumber();
            }
            fileInfo.dos.writeFloat(value);
            return NumberValue.ONE;
        }
    }
    
    private static class writeDouble extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeDouble(args[1].asNumber());
            return NumberValue.ONE;
        }
    }
    
    private static class writeUTF extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeUTF(args[1].asString());
            return NumberValue.ONE;
        }
    }
    
    private static class writeLine extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.writer.write(args[1].asString());
            fileInfo.writer.newLine();
            return NumberValue.ONE;
        }
    }

    private static class writeText extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.writer.write(args[1].asString());
            return NumberValue.ONE;
        }
    }
    
    private static class flush extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            if (fileInfo.dos != null) {
                fileInfo.dos.flush();
            }
            if (fileInfo.writer != null) {
                fileInfo.writer.flush();
            }
            return NumberValue.ONE;
        }
    }
    
    private static class fclose extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            if (fileInfo.dis != null) {
                fileInfo.dis.close();
            }
            if (fileInfo.dos != null) {
                fileInfo.dos.close();
            }
            if (fileInfo.reader != null) {
                fileInfo.reader.close();
            }
            if (fileInfo.writer != null) {
                fileInfo.writer.close();
            }
            return NumberValue.ONE;
        }
    }

    private static File fileFrom(Value value) {
        if (value.type() == Types.NUMBER) {
            return files.get(value.asInt()).file;
        }
        return new File(value.asString());
    }

    private interface FileToBooleanFunction {

        boolean apply(File file);
    }

    private static Function fileToBoolean(FileToBooleanFunction f) {
        return args -> {
            Arguments.check(1, args.length);
            return NumberValue.fromBoolean(f.apply(fileFrom(args[0])));
        };
    }
    
    private static class FileInfo {
        File file;
        DataInputStream dis;
        DataOutputStream dos;
        BufferedReader reader;
        BufferedWriter writer;

        public FileInfo(File file, DataInputStream dis, DataOutputStream dos, BufferedReader reader, BufferedWriter writer) {
            this.file = file;
            this.dis = dis;
            this.dos = dos;
            this.reader = reader;
            this.writer = writer;
        }
    }
}
