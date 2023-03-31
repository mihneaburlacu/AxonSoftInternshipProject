package Model;

import java.time.LocalDateTime;

public class Applicant {
    private String name;
    private String email;
    private LocalDateTime dateTime;
    private double score;

    public Applicant() {super();}

    public Applicant(String name, String email, LocalDateTime dateTime, double score) {
        this.name = name;
        this.email = email;
        this.dateTime = dateTime;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Applicant clone() {
        return new Applicant(this.name, this.email, this.dateTime, this.score);
    }

    @Override
    public String toString() {
        return "DTO.Applicant{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dateTime=" + dateTime +
                ", score=" + score +
                '}';
    }
}
