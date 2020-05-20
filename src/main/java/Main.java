import org.apache.log4j.BasicConfigurator;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String []args) {
        try {
            BasicConfigurator.configure();
            Detect detect = new Detect();
            detect.scanLanguage(detect.scanLanguage("bulgara.txt"));
        } catch (NullPointerException n){
            System.out.println("Done");
        }
    }
}
