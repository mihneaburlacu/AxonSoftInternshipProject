package Start;

import Impl.ApplicantsProcessor;
import Impl.CSVReader;

import java.io.InputStream;

public class MainClass {
    public static void main(String args[]) {
        ApplicantsProcessor applicantsProcessor = new ApplicantsProcessor();
        InputStream inputStream = CSVReader.createInputStreamFromCSVFile("src/main/file.csv");
        String outputJSON = applicantsProcessor.processApplicants(inputStream);
        System.out.println(outputJSON);
    }
}
