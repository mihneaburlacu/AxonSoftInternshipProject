package Impl;

import Model.Applicant;
import Model.Comparators.DateTimeComparator;
import Model.Comparators.ScoreAfterAdjustmentComparator;
import Model.Comparators.ScoreComparator;
import com.google.gson.Gson;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class ApplicantsProcessor {
    public ApplicantsProcessor() {super();}

    public int CalculateNumberOfUniqueApplicants(List<Applicant> list) {
        int size = list.size();

        List<Applicant> applicantsToRemove = new ArrayList<>();

        //in HashSet we can not add duplicate values, so we check the size of the set after adding the all list of applicants
        Set<String> emailList = new HashSet<>();
        for(int i = size - 1; i >= 0; i--) {
            boolean canInsert = emailList.add(list.get(i).getEmail());
            if(!canInsert) {
                applicantsToRemove.add(list.get(i));
            }
        }

        //remove the duplicates
        for(Applicant applicant : applicantsToRemove) {
            list.remove(applicant);
        }

        return emailList.size();
    }

    //Method for modify the score by checking if the day is first or last, if is last check for the hour to be > 12
    private void modifyScore(List<Applicant> list, LocalDateTime firstDate, LocalDateTime lastDate) {
        for(Applicant applicant : list) {
            if(applicant.getDateTime().getDayOfMonth() == firstDate.getDayOfMonth()) {
                double score = applicant.getScore();
                applicant.setScore(Math.min(score + 1, 10));
            }
            if(applicant.getDateTime().getDayOfMonth() == lastDate.getDayOfMonth() && applicant.getDateTime().getHour() >= 12) {
                double score = applicant.getScore();
                applicant.setScore(Math.max(score - 1, 0));
            }
        }
    }

    public String[] topThreeApplicants(List<Applicant> list){
        //sort the list by Date ascending working on a copy list to not edit the initial list of applicants
        List<Applicant> copyList = new ArrayList<>();
        for(Applicant applicant : list) {
            copyList.add(applicant.clone());
        }
        copyList.sort(new DateTimeComparator());

        //get first and last day from the list
        LocalDateTime firstDate;
        LocalDateTime lastDate;
        firstDate = copyList.get(0).getDateTime();
        lastDate = copyList.get(copyList.size() - 1).getDateTime();


        //modify the score
        if(firstDate.compareTo(lastDate) != 0) {
            modifyScore(copyList, firstDate, lastDate);
        }

        //sort the list by Score descending
        copyList.sort(new ScoreAfterAdjustmentComparator(firstDate, lastDate));

        //get top three applicants last name by score or if list size < 3 get only the number of applicants
        int index = 0;

        if(copyList.size() < 3) {
            index = copyList.size();
        }
        else {
            index = 3;
        }

        String[] topThree = new String[index];

        for(int i = 0; i < index; i++) {
            String name = copyList.get(i).getName();
            topThree[i] = name.substring(name.lastIndexOf(" ") + 1);
        }

        return topThree;
    }

    public BigDecimal getAverageTopHalfScore(List<Applicant> list) {
        //if the size is an odd number top half should be the one with an odd number of applicants
        int size = list.size();
        int halfSize = size / 2 + size % 2;

        //sort the list by the score
        List<Applicant> sortedList = new ArrayList<>(list);
        sortedList.sort(new ScoreComparator());

        //calculate sum of scores
        BigDecimal scoreSum = BigDecimal.ZERO;
        for(int i = 0; i < halfSize; i++) {
            scoreSum = scoreSum.add(BigDecimal.valueOf(sortedList.get(i).getScore()));
        }

        return scoreSum.divide(BigDecimal.valueOf(halfSize), 2, RoundingMode.HALF_UP);
    }

    public String processApplicants(InputStream csvStream) {
        //read and parse data from csv file
        List<Applicant> list = CSVReader.readFromCSV(csvStream);

        //get number of unique applicants
        int numberUniqueApplicants = CalculateNumberOfUniqueApplicants(list);
        System.out.println("Number of unique applicants: " + numberUniqueApplicants + "\n");

        //get top three applicants sorted by score after adjustment
        String[] topThree = topThreeApplicants(list);
        System.out.println("Top three applicants by score: ");
        for(String applicantName : topThree) {
            System.out.println(applicantName);
        }

        //get average top half score
        BigDecimal averageScoreTopHalf = getAverageTopHalfScore(list);
        System.out.println("\nAverage score of top half applicants: "+ averageScoreTopHalf + "\n");

        //create JSON output using GSON
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("numberUniqueApplicants", numberUniqueApplicants);
        jsonMap.put("topThree", topThree);
        jsonMap.put("averageScoreTopHalf", averageScoreTopHalf);

        Gson gson = new Gson();

        return gson.toJson(jsonMap);
    }

}
