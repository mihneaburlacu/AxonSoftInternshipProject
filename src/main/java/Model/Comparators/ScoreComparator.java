package Model.Comparators;

import Model.Applicant;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Applicant> {
    @Override
    public int compare(Applicant o1, Applicant o2) {
        return Double.compare(o2.getScore(), o1.getScore());
    }
}
