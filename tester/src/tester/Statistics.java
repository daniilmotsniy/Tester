package tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Statistics {

    private String path;
    HashMap<String, List<Integer>> users = new HashMap<>();

    private int best_result = 0;

    Statistics(String path){
        this.path = path;
    }

    void printUsers(HashMap<String, List<Integer>> users){
        users.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }

    void getBuff(){
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file); // поток, который подключается к текстовому файлу
            BufferedReader bufferedReader = new BufferedReader(fileReader); // соединяем FileReader с BufferedReader

            String line;
            String current_name = null;
            String current_result_str = null;
            int current_result_int = 0;

            int index_name = 0;
            int index_result = 0;

            while((line = bufferedReader.readLine()) != null) {
                index_name = line.indexOf('n');
                index_result = line.indexOf('r');

                current_name = line.substring(0, index_name);

                current_result_str += line.substring(index_result);
                current_result_int = Integer.parseInt(current_result_str);
                
                if(users.get(current_name) == null){
                    users.put(current_name, new ArrayList<>());
                }

                if (current_name!=null) {
                    users.get(current_name).add(current_result_int);
                }

                current_name = null;
            }


            bufferedReader.close(); // закрываем поток

        } catch (Exception e) {
            e.printStackTrace();
        }

        printUsers(users); //dm delete it!
    }

}
