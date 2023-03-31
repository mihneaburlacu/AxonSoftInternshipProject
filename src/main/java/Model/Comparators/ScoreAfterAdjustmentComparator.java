package Model.Comparators;

import Model.Applicant;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ScoreAfterAdjustmentComparator implements Comparator<Applicant> {
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    public ScoreAfterAdjustmentComparator() {}

    public ScoreAfterAdjustmentComparator(LocalDateTime first, LocalDateTime last) {
        this.firstDay = first;
        this.lastDay = last;
    }

    @Override
    public int compare(Applicant o1, Applicant o2) {
        int compared = Double.compare(o2.getScore(), o1.getScore());

        if(compared == 0) {
            double firstScore = o2.getScore();
            double secondScore = o1.getScore();

            //check if the score was changed
            if(o2.getDateTime().getDayOfMonth() == firstDay.getDayOfMonth()) {
                firstScore--;
            }
            if(o2.getDateTime().getDayOfMonth() == lastDay.getDayOfMonth() && o2.getDateTime().getHour() > 12) {
                firstScore++;
            }

            if(o1.getDateTime().getDayOfMonth() == firstDay.getDayOfMonth()) {
                secondScore--;
            }
            if(o1.getDateTime().getDayOfMonth() == lastDay.getDayOfMonth() && o1.getDateTime().getHour() > 12) {
                secondScore++;
            }

            if(Double.compare(firstScore, secondScore) == 0) {
                //compare by DateTime if equals
              compared = new DateTimeComparator().compare(o1, o2);
              if(compared == 0) {
                  //compare by email
                  compared = o1.getEmail().compareTo(o2.getEmail());
              }
            }
            else {
                compared = Double.compare(firstScore, secondScore);
            }

        }

        return compared;
    }
}
