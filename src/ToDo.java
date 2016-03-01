
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ericweidman on 2/8/16.
 */
public class ToDo {
    public static  void insertToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false)");
        stmt.setString(1, text);
        stmt.execute();
    }

    public static ArrayList<ToDoItem> selectToDos(Connection conn) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM todos");
        while (results.next()){
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, text, isDone));

        }
        return items;
    }

    public static void toggleToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

    }

    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN)");

       // ArrayList<ToDoItem> items = new ArrayList();
        Scanner scanner = new Scanner(System.in);


        while (true){
            System.out.println("1. Create to-do item.");
            System.out.println("2. Toggle to do item.");
            System.out.println("3. List to-do items");

            String option = scanner.nextLine();

            if(option.equals("1")){
                System.out.println("Enter your to-do item.");
                String text = scanner.nextLine();

                //ToDoItem item = new ToDoItem(text, false);
                //items.add(item);
                insertToDo(conn, text);

            }
            else if(option.equals("2")){
                System.out.println("Enter the number of the item you want to toggle.");
                int itemNum = Integer.valueOf(scanner.nextLine());
                //ToDoItem item = items.get(itemNum-1);
                //item.isDone = !item.isDone;
                toggleToDo(conn, itemNum);

            }
            else if(option.equals("3")){
                ArrayList<ToDoItem> items = selectToDos(conn);
                //int i = 1;
                for(ToDoItem item : items){
                    String checkbox = "[ ]";
                    if (item.isDone){
                        checkbox = "[x] ";
                    }
                    System.out.printf("%s %d. %s\n", checkbox, item.id, item.text);
                    1//i++;
                }
            }
            else {
                System.out.println("Invalid option.");
            }
        }
    }

}
