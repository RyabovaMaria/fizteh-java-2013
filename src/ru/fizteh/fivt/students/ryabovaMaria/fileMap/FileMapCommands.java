package ru.fizteh.fivt.students.ryabovaMaria.fileMap;
    
import java.util.HashMap;
import ru.fizteh.fivt.students.ryabovaMaria.shell.AbstractCommands;
import static ru.fizteh.fivt.students.ryabovaMaria.shell.AbstractCommands.lexems;

public class FileMapCommands extends  AbstractCommands{
    public static HashMap<String, String> list;
    
    public FileMapCommands(HashMap<String, String> myList) {
        list = myList;
    }
    private static void parse() {
        String[] tempLexems = new String[0];
        if (lexems.length > 1) {
            tempLexems = lexems[1].split("[ \t\n\r]+", 2);
        }
        lexems = tempLexems;
    }
    private static void checkTheNumbOfArgs(int n, String commandName) throws Exception {
        if (lexems.length < n) {
            throw new Exception(commandName + ": there is no enough arguments.");
        }
        if (lexems.length > n) {
            throw new Exception(commandName + ": there is so many arguments.");
        }
    }
    
    public static void put() throws Exception {
        parse();
        checkTheNumbOfArgs(2, "put");
        if (list.containsKey(lexems[0])) {
            String oldValue = list.get(lexems[0]);
            list.remove(lexems[0]);
            list.put(lexems[0], lexems[1]);
            System.out.println("overwrite");
            System.out.println(oldValue);
        } else {
            list.put(lexems[0], lexems[1]);
            System.out.println("new");
        }
        
    }
    
    public static void get() throws Exception {
        parse();
        checkTheNumbOfArgs(1, "get");
        if (list.containsKey(lexems[0])) {
            String value = list.get(lexems[1]);
            System.out.println("found");
            System.out.println(value);
        } else {
            System.out.println("not found");
        }
    }
    
    public static void remove() throws Exception {
        parse();
        checkTheNumbOfArgs(1, "remove");
        if (list.containsKey(lexems[0])) {
            list.remove(lexems[0]);
            System.out.println("removed");
        } else {
            System.out.println("not found");
        }
    }
    
    public static void exit() {
        try { 
            FileMap.writeIntoFile();
        } catch (Exception e) {
            System.err.println("I can't write into db.dat");
            System.exit(1);
        }
        System.exit(0);
    }
}