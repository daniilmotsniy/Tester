package tester;

import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

public class Information {

        public Vector<String> text;

        void getText(String adress) throws  Exception {

                FileReader reader = new FileReader(adress);

                Scanner scan = new Scanner(reader);

                while (scan.hasNextLine()) {
                        text.add(scan.nextLine());
                }

                reader.close();
        }

        void printText(){
                for(int i = 0; i < text.size(); i++){
                        System.out.println(text.get(i));
                }
        }

        void getText(){
                for(int i = 0; i < text.size(); i++){
                        System.out.println(text.get(i));
                }
        }

}
