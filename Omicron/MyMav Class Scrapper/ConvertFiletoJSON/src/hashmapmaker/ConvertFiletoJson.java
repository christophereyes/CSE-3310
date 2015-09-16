/*BY ROBERT ARON
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashmapmaker;

import com.csvreader.CsvWriter;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author HeyImRige
 */
public class ConvertFiletoJson {

    File readFile;
    File writeFile;
    String[] tokens;
    int count;
    BufferedReader reader;
    Gson gson;
    int count2;
    String readString;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ConvertFiletoJson runner = new ConvertFiletoJson();
        runner.run();
    }

    public void HashMapMaker() {

    }

    public void run() throws FileNotFoundException, IOException{
        //CsvWriter csvWrite = new CsvWriter();
        gson = new Gson();
        Information information;
        readFile = new File("C:\\Users\\HeyImRige\\Desktop\\FormatedFile.txt");
        writeFile = new File("C:\\Users\\HeyImRige\\Desktop\\JSONFormat.JSON");
        HashMap<String, Information> hashMap = new HashMap<String, Information>();
        reader = new BufferedReader(new FileReader(readFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile));
        writer.append("{\"results\":[");
        for (count = 0; (readString = reader.readLine()) != null; count++) {
            System.out.println(count);
            tokens = readString.split(";");
            if(count!=0){
                writer.append(",");
            }//Name,section,days,timeStart,timeEnd,time end,teacher,classNum,department
            if (tokens[4].contains("TBA")) {
                information = new Information(
                        tokens[2],//name
                        tokens[3],//section
                        "TBA",//days
                        "TBA",//time
                        "TBA",//time end
                        tokens[5],//teacher
                        tokens[1],//classNum
                        tokens[0]);//depart
            } else {
                information = new Information(
                        tokens[2],//name
                        tokens[3],//section
                        tokens[4],//days
                        tokens[5],//time
                        tokens[6],//end time
                        tokens[7],//teacher
                        tokens[1],//classNum
                        tokens[0]);//depart
            }
            gson.toJson(information, writer);
        }
        writer.append("]}");
        writer.close();
    }

    private static class Information {
        String department;
        int classNum;
        String name;
        String section;
        String days;
        String timeStart;
        String timeEnd;
        String teacher;

        public Information(String Name, String section, String days, String timeStart,String timeEnd, String teacher, String classNum,String department) {
            this.name = Name;
            this.section = section;
            this.days = days;
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
            this.teacher = teacher;
            this.classNum = Integer.parseInt(classNum);
            this.department = department;
        }
    }
}
