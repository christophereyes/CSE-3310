/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webreader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author HeyImRige
 */
public class WebReader {
    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        WebLoader webLoader;/*
        webLoader = new WebLoader(new URL("https://portal.moovmanage.com/coachusa-mb-tx/connect.php?res=notyet&uamip=10.0.0.1&uamport=3990&challenge=b4bce4bd1f7bf8cb4b8b9a099cb26f6f&called=30-14-4A-3E-F1-6B&mac=88-9F-FA-86-B9-36&ip=10.0.0.118&nasid=8753&sessionid=5543f8120000000d&ssl=https%3a%2f%2f1.0.0.1%3a4990%2f&userurl=http%3a%2f%2fwww.msftncsi.com%2fredirect&md=9C86279E0196E8D74FA987D7093C0DE9")); 
        webLoader.LoadPage();
        webLoader.MegabusCheck();*/
        
        webLoader = new WebLoader(new URL("https://sis-portal-prod.uta.edu/psp/AEPPRD/EMPLOYEE/EMPL/h/?tab=PAPP_GUEST"));//This needs the webiste name to actually do anything
        webLoader.LoadPage();
        webLoader.Login();
        //webLoader.StudentCenter();
        webLoader.StudentCenterCheat();
        //webLoader.Search();
        webLoader.SearchPageCheat();
        webLoader.FindAllSubjects();
        webLoader.FinalRound();
        webLoader.PrintWriterClose();
        
        
        //google
        /*webLoader = new WebLoader(new URL("https://google.com"));
        webLoader.LoadPage();
        System.out.println(webLoader.page.asText());
        */
    }
    
}
