package ru.fizteh.fivt.students.anastasyev.filemap;

import ru.fizteh.fivt.students.anastasyev.shell.Command;

import java.io.IOException;

public class DropCommand implements Command<FileMapTable> {
    @Override
    public boolean exec(FileMapTable state, String[] command) {
        if (command.length != 2) {
            System.err.println("drop: Usage - drop tablename");
            return false;
        }
        try {
            state.dropTable(command[1]);
        } catch (IOException e) {
            System.err.println("drop: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String commandName() {
        return "drop";
    }
}
