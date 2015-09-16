/*BY ROBERT ARON
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webreader;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

/**
 *
 * @author HeyImRige
 */
public class WebLoader {

    int i;
    int i2;
    int i3;
    URL url;
    WebClient webClient;
    HtmlPage page1;
    HtmlPage page2;
    HtmlPage page3;
    String[] subjects = new String[300];
    File file;
    String stringPage;
    File fileLocation;
    PrintWriter printWriter;

    public WebLoader(URL url) throws FileNotFoundException {//creates the webloader and gives it a url
        this.url = url;
        fileLocation = new File("C:\\Users\\HeyImRige\\Desktop\\classList.txt");
        printWriter = new PrintWriter(fileLocation);
        printWriter.flush();
    }

    public void LoadPage() throws IOException, InterruptedException {//loads the webpage
        webClient = new WebClient(BrowserVersion.CHROME);
        //System.out.println(url);
        page1 = webClient.getPage(url);
        Thread.sleep(5000);
        //System.out.println(page.asText());
    }

    public void Login() throws IOException {//Login to the mymav page. page has been moved to at this point
        HtmlForm form = page1.getFormByName("login");//find the login feild
        HtmlTextInput userNameField = form.getInputByName("userid");//username form
        userNameField.setValueAttribute("userid");//set
        HtmlPasswordInput passWordField = form.getInputByName("pwd");//password form
        passWordField.setValueAttribute("password");//set
        HtmlSubmitInput button = form.getInputByName("Submit");//find the sign in button
        page1 = button.click();
    }

    public void StudentCenter() throws IOException {//finds the anchor to move to the student center and clicks it
        //System.out.println(page.toString());
        //System.out.println(page);
        HtmlAnchor anchor = page1.getAnchorByHref("https://sis-portal-prod.uta.edu/psp/AEPPRD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL?pslnkid=UTA_STUDENTCENTER_LINK01&PORTALPARAM_PTCNAV=UTA_STUDENTCENTER_LINK01&EOPP.SCNode=EMPL&EOPP.SCPortal=EMPLOYEE&EOPP.SCName=ADMN");
        //System.out.println(anchor.asText());
        page1 = anchor.click();
    }

    public void StudentCenterCheat() throws IOException {
        page1 = webClient.getPage("https://sis-portal-prod.uta.edu/psp/AEPPRD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL?pslnkid=UTA_STUDENTCENTER_LINK01&PORTALPARAM_PTCNAV=UTA_STUDENTCENTER_LINK01&EOPP.SCNode=EMPL&EOPP.SCPortal=EMPLOYEE&EOPP.SCName=ADMN_STUDENT_CENTER&EOPP.SCLabel=Student%20Center&EOPP.SCPTcname=&FolderPath=PORTAL_ROOT_OBJECT.PORTAL_BASE_DATA.CO_NAVIGATION_COLLECTIONS.ADMN_STUDENT_CENTER.ADMN_S200908061331579902035584&IsFolder=false");
        //System.out.println(page.asXml());
    }

    public void Search() throws IOException {//clicks the search for classes button
        List<HtmlAnchor> anchor = page1.getAnchors();
        for (int i = 0; i < anchor.size(); i++) {
            System.out.println("Anchor" + i + ":" + anchor.get(i).asText());
        }
        //page = anchor.click();
    }

    public void SearchPageCheat() throws IOException {
        page1 = webClient.getPage("https://sis-cs-prod.uta.edu/psc/ACSPRD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.CLASS_SEARCH.GBL?FolderPath=PORTAL_ROOT_OBJECT.CO_EMPLOYEE_SELF_SERVICE.HCCC_SS_CATALOG.HC_CLASS_SEARCH&IsFolder=false&IgnoreParamTempl=FolderPath%2cIsFolder&PortalActualURL=https%3a%2f%2fsis-cs-prod.uta.edu%2fpsc%2fACSPRD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.CLASS_SEARCH.GBL&PortalContentURL=https%3a%2f%2fsis-cs-prod.uta.edu%2fpsc%2fACSPRD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.CLASS_SEARCH.GBL&PortalContentProvider=HRMS&PortalCRefLabel=Class%20Search&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fsis-portal-prod.uta.edu%2fpsp%2fAEPPRD%2f&PortalURI=https%3a%2f%2fsis-portal-prod.uta.edu%2fpsc%2fAEPPRD%2f&PortalHostNode=EMPL&NoCrumbs=yes&PortalKeyStruct=yes");
        System.out.println("got the search page");
    }

    public void FindAllSubjects() throws InterruptedException, IOException {
        i3 = 0;
        webClient.waitForBackgroundJavaScript(2000);
        System.out.println("Got past waiting");
        //System.out.println(page1.asText());
        System.out.println("success");
        HtmlAnchor anchor = page1.getAnchorByName("CLASS_SRCH_WRK2_SSR_PB_SUBJ_SRCH$0");
        page2 = anchor.click();
        webClient.waitForBackgroundJavaScript(2000);
        for (i = 65; (i <= 87); i++) {//check A-W
            if ((i != 81) && (i != 86)) {//make sure it doesn't check the empty areas
                anchor = page2.getAnchorByName("SSR_CLSRCH_WRK2_SSR_ALPHANUM_" + (char) i);
                page3 = anchor.click();
                webClient.waitForBackgroundJavaScript(1000);
                try {
                    for (i2 = 0; page3.getHtmlElementById("UTA_CLSRCH_SUBJ_SUBJECT$" + i2) != null; i2++) {
                        HtmlElement subject = page3.getHtmlElementById("UTA_CLSRCH_SUBJ_SUBJECT$" + i2);
                        subjects[i3] = subject.getTextContent();
                        System.out.println(subjects[i3] + " " + i3);
                        i3++;
                    }
                } catch (ElementNotFoundException e) {
                    System.out.println("not to worry, this is to be expected.");
                }
            }
        }
    }

    public void FinalRound() throws IOException {
        HtmlForm form;
        //System.out.println(form.asXml());
        HtmlTextInput subjectSearch;
        HtmlAnchor searchButton;
        for (i = 0;subjects[i] != null; i++) {
            webClient.getPage("https://sis-portal-prod.uta.edu/psp/AEPPRD/EMPLOYEE/EMPL/h/?tab=DEFAULT");
            page1 = webClient.getPage("https://sis-cs-prod.uta.edu/psc/ACSPRD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.CLASS_SEARCH.GBL?FolderPath=PORTAL_ROOT_OBJECT.CO_EMPLOYEE_SELF_SERVICE.HCCC_SS_CATALOG.HC_CLASS_SEARCH&IsFolder=false&IgnoreParamTempl=FolderPath%2cIsFolder&PortalActualURL=https%3a%2f%2fsis-cs-prod.uta.edu%2fpsc%2fACSPRD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.CLASS_SEARCH.GBL&PortalContentURL=https%3a%2f%2fsis-cs-prod.uta.edu%2fpsc%2fACSPRD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.CLASS_SEARCH.GBL&PortalContentProvider=HRMS&PortalCRefLabel=Class%20Search&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fsis-portal-prod.uta.edu%2fpsp%2fAEPPRD%2f&PortalURI=https%3a%2f%2fsis-portal-prod.uta.edu%2fpsc%2fAEPPRD%2f&PortalHostNode=EMPL&NoCrumbs=yes&PortalKeyStruct=yes");
            webClient.waitForBackgroundJavaScriptStartingBefore(5000);
            form = page1.getFormByName("win0");
            searchButton = page1.getAnchorByName("CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH");
            subjectSearch = form.getInputByName("SSR_CLSRCH_WRK_SUBJECT$0");
            System.out.println("starting class adding of " + subjects[i] + "(" + i + ")");
            subjectSearch.setValueAttribute(subjects[i]);
            page2 = searchButton.click();
            webClient.waitForBackgroundJavaScriptStartingBefore(120000);
            try {
                for (i2 = 0;; i2++) {
                    System.out.println("New Line to Writer");
                    HtmlDivision div = page2.getHtmlElementById("win0divSSR_CLSRSLT_WRK_GROUPBOX2GP$" + i2);//finds class number and name
                    printWriter.print(div.getTextContent() + ";");
                    HtmlAnchor sectionNum = page2.getAnchorByName("MTG_CLASSNAME$0");//section number
                    printWriter.print(sectionNum.getTextContent() + ";");
                    div = page2.getHtmlElementById("win0divMTG_DAYTIME$" + i2);//day and times
                    printWriter.print(div.getTextContent() + ";");
                    div = page2.getHtmlElementById("win0divMTG_INSTR$" + i2);
                    printWriter.println(div.getTextContent());
                }
            } catch (ElementNotFoundException e) {
                System.out.println(e);
                //page1 = webClient.getPage("https://sis-cs-prod.uta.edu/psc/ACSPRD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.CLASS_SEARCH.GBL?FolderPath=PORTAL_ROOT_OBJECT.CO_EMPLOYEE_SELF_SERVICE.HCCC_SS_CATALOG.HC_CLASS_SEARCH&IsFolder=false&IgnoreParamTempl=FolderPath%2cIsFolder&PortalActualURL=https%3a%2f%2fsis-cs-prod.uta.edu%2fpsc%2fACSPRD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.CLASS_SEARCH.GBL&PortalContentURL=https%3a%2f%2fsis-cs-prod.uta.edu%2fpsc%2fACSPRD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.CLASS_SEARCH.GBL&PortalContentProvider=HRMS&PortalCRefLabel=Class%20Search&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fsis-portal-prod.uta.edu%2fpsp%2fAEPPRD%2f&PortalURI=https%3a%2f%2fsis-portal-prod.uta.edu%2fpsc%2fAEPPRD%2f&PortalHostNode=EMPL&NoCrumbs=yes&PortalKeyStruct=yes");
                webClient.waitForBackgroundJavaScript(2000);
            }
        }
    }

    public void PrintWriterClose() {
        printWriter.close();
    }

    //THIS IS FOR LOGIN TO MEGABUS
    public void MegabusCheck() throws IOException, InterruptedException {
        HtmlCheckBoxInput input = page1.getHtmlElementById("aup_agree");
        input.setChecked(true);
        HtmlForm submit = page1.getFirstByXPath("//form[@action='#']");
        HtmlInput button = submit.getFirstByXPath("//input[@type='submit']");
        System.out.println("got here");
        page1 = button.click();
        System.out.println("button pressed");
        Thread.sleep(10000);
        System.out.println(page1.asText());//check with google
        System.out.println("wait here");
        Thread.sleep(10000);
        /*page = webClient.getPage("https://google.com");
         System.out.println(page.asText());*/
    }
}
