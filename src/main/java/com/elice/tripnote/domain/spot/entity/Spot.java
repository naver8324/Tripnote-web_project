package com.elice.tripnote.domain.spot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "spot")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String location; //주소명?

    @Column(nullable = false)
    private int likes;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private String region; //지역

    @Builder
    public Spot(String location, int likes, String imageUrl, String region){
        this.location=location;
        this.likes=likes;
        this.imageUrl=imageUrl;
        this.region=region;
    }

    public void increaseLikes(){
        this.likes++;
    }

    public void decreaseLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

}
