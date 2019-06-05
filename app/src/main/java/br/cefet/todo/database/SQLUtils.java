package br.cefet.todo.database;

public class SQLUtils {

    public static String createTableTask() {
        String str = "CREATE TABLE IF NOT EXISTS Task(\n" +
                "            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "            title VARCHAR(120) NOT NULL,\n" +
                "            description TEXT NOT NULL,\n" +
                "            completed BOOLEAN NOT NULL,\n" +
                "            archived BOOLEAN NOT NULL,\n" +
                "            deleted BOOLEAN NOT NULL\n" +
                "        );";

        return str;
    }

}
