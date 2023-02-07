package nus.iss.team2.ADProjectTECHS.Model;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_crawled")
@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCrawled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private long courseId;

    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "url_link")
    private String urlLink;

    private long views;

    private long likes;

    private long subscribers;

    @Column(name = "channel_name")
    private String channelName;


    @Column(name = "date")
    private LocalDate date;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private String duration;

    private String img;

    @JsonProperty("skill_id")
    @ManyToOne(cascade = { 
        CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CourseCrawled that = (CourseCrawled) o;
        return courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}