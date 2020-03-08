package tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  Tester
 *
 *  Author DMotsniy & Mirniy18
 */

public class Statistics {

    private String path;
    HashMap<String, List<Integer>> users = new HashMap<>();

    private int best_result = 0;

    Statistics(String path){
        this.path = path;
    }

    void getData(){
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file); // thread connected to file
            BufferedReader bufferedReader = new BufferedReader(fileReader); // connecting FileReader and BufferedReader

            String line; // line in file with student's information

            String current_name;
            String current_result_str;
            String current_group;
            int current_result_int;

            int index_name;
            int index_result;
            int index_group;

            boolean flag = false;
            boolean scaned = false;

            while((line = bufferedReader.readLine()) != null) {
                for(int i = line.length() - 1; i >= 0; --i){
                    if(line.charAt(i) < '0' || line.charAt(i) > '9'){
                        flag = line.charAt(i) == 'r';
                        break;
                    }
                }
                index_name = line.lastIndexOf('n');
                index_result = line.indexOf('r');
                index_group =  line.lastIndexOf('g');

                current_name = line.substring(0, index_name);
                current_group = line.substring(index_name, index_group);
                current_result_str = line.substring(index_result+1);
                current_result_int = Integer.parseInt(current_result_str);
                
                if(users.get(current_name) == null){
                    users.put(current_name, new ArrayList<>());
                }

                if (current_name!=null) {
                    users.get(current_name).add(current_result_int);
                }

            }

            bufferedReader.close(); // close thread

        } catch (Exception e) {
            e.printStackTrace();
        }

        printUsers(users); // print hash map
    }

    void printUsers(HashMap<String, List<Integer>> users){
        users.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }

//    int findMax(HashMap<String, List<Integer>> users){
//        int max = 0, i = 0;
//
//        users.entrySet().forEach(entry->{
//            if(entry.getValue().get(i) > max){
//                max = entry.getValue().get(i);
//                i++;
//            }
//        });
//
//        return max;
//    }

}
