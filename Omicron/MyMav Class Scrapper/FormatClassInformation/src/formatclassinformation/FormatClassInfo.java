/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package formatclassinformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;

/**
 *
 * @author HeyImRige
 */
public class FormatClassInfo {

    File writeFile;
    File readFile;
    File tempFile;
    BufferedReader reader;
    PrintWriter writer;
    String line;
    String delims;
    static String[] tokens;
    int count;
    int count2;
    int count3;

    public FormatClassInfo() {        
    }

    public void Go() throws FileNotFoundException, IOException {
        writeFile = new File("C:\\Users\\HeyImRige\\Desktop\\FormatedFile.txt");
        readFile = new File("C:\\Users\\HeyImRige\\Desktop\\classList-best.txt");
        tempFile = new File("C:\\Users\\HeyImRige\\Desktop\\temp.txt");
        writer = new PrintWriter(writeFile);
        delims = "[ ;-]";
        count2 = 0;
        RemoveInvisableCharacter();
        reader = new BufferedReader(new FileReader(tempFile));
        while ((line = reader.readLine()) != null) {//first line
            if (line.equals("")) {
                line = reader.readLine();
            }
            if (count2 == 2403) {
                System.out.println("here");
            }
            System.out.println(count2);
            count2++;
            tokens = line.split(delims);
            writer.print(tokens[0]);
            for (count = 1; !((tokens[count].charAt(0) >= '0') && (tokens[count].charAt(0) <= '9')); count++) {
                writer.print(tokens[count]);
            }
            writer.print(";"+tokens[count]+";");
            
            for (count = count+3; (!((tokens[count].charAt(0) >= '0') && (tokens[count].charAt(0) <= '9'))); count++) {//Writes down the class name till the section num comes up
                writer.print(tokens[count] + ' ');
                while ("".equals(tokens[count + 1])) {
                    count++;
                }
            }
            writer.print(';' + tokens[count] + ";");
            tokens = reader.readLine().split(delims);//second line
            if (tokens.length > 2) {
                writer.print(tokens[1] + ';' + tokens[2] + ';' + tokens[5] + ';');
            } else {
                writer.print(tokens[1]+';');
            }
            tokens = reader.readLine().split(delims);//third line(teachers)
            while (tokens.length >= 2) {
                for (count3 = 1; count3 <= tokens.length - 2; count3++) {
                    writer.print(tokens[count3] + ' ');
                }
                writer.print(tokens[count3]);
                tokens = reader.readLine().split(delims);
                if (tokens.length >= 2) {
                    writer.print(',');
                }
            }
            writer.println(";");
        }
        writer.close();
    }

    public void RemoveInvisableCharacter() throws FileNotFoundException {
        int counter=0;
        String line="hello\n";
        Scanner scanner = new Scanner(readFile);
        PrintWriter write = new PrintWriter(tempFile);
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            for (int i = 0; i <= line.length() - 1; i++) {
                if (line.charAt(i) != ' ') {
                    write.append(line.charAt(i));
                }
            }
            write.write('\n');
            counter++;
        }
        scanner.close();
        write.close();
    }
}