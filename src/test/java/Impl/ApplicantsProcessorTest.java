package Impl;

import Model.Applicant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantsProcessorTest {
    private static final String TEST_CSV = "name,email,delivery_datetime,score\n" +
            "Speranța Cruce,speranta_cruce@gmail.com,2023-01-24T20:14:53,2.33\n" +
            "Ionică Sergiu Ramos,chiarel@ionicaromass.ro,2023-01-24T16:32:19,9.00\n" +
            "Carla Ștefănescu,carlita_ste@yahoo.com,2023-01-23T23:59:01,5.20\n" +
            "Lucrețiu Hambare,hambare_lucretiu@outlook.com,2023-01-24T22:30:15,10\n" +
            "Robin Hoffman-Rus,robman@dasmail.de,2023-01-23T12:00:46,8.99";

    @Test
    void calculateNumberOfUniqueApplicants() {
        InputStream stream = new ByteArrayInputStream(TEST_CSV.getBytes(StandardCharsets.UTF_8));
        List<Applicant> applicants = CSVReader.readFromCSV(stream);

        ApplicantsProcessor processor = new ApplicantsProcessor();
        int uniqueApplicants = processor.CalculateNumberOfUniqueApplicants(applicants);

        Assertions.assertEquals(5, uniqueApplicants);
    }

    @Test
    void topThreeApplicants() {
        InputStream stream = new ByteArrayInputStream(TEST_CSV.getBytes(StandardCharsets.UTF_8));
        List<Applicant> applicants = CSVReader.readFromCSV(stream);

        ApplicantsProcessor processor = new ApplicantsProcessor();
        String[] topThree = processor.topThreeApplicants(applicants);

        Assertions.assertArrayEquals(new String[]{ "Hoffman-Rus", "Hambare", "Ramos"}, topThree);
    }

    @Test
    void getAverageTopHalfScore() {
        InputStream stream = new ByteArrayInputStream(TEST_CSV.getBytes(StandardCharsets.UTF_8));
        List<Applicant> applicants = CSVReader.readFromCSV(stream);

        ApplicantsProcessor processor = new ApplicantsProcessor();
        BigDecimal averageScore = processor.getAverageTopHalfScore(applicants);

        Assertions.assertEquals(new BigDecimal("9.33"), averageScore);
    }

    @Test
    void processApplicants() {
        InputStream stream = new ByteArrayInputStream(TEST_CSV.getBytes(StandardCharsets.UTF_8));

        ApplicantsProcessor processor = new ApplicantsProcessor();
        String jsonOutput = processor.processApplicants(stream);

        String expectedOutput = "{\"numberUniqueApplicants\":5,\"topThree\":[\"Hoffman-Rus\",\"Hambare\",\"Ramos\"],\"averageScoreTopHalf\":9.33}";
        Assertions.assertEquals(expectedOutput, jsonOutput);
    }
}