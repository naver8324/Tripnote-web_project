package com.elice.tripnote.domain.route.entity;

import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.route.status.RouteStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integrated_route_id", nullable = false)
    private IntegratedRoute integratedRoute;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus routeStatus;

    @Column(nullable = true)
    private int expense;

    @Builder
    public Route(Member member, IntegratedRoute integratedRoute, RouteStatus routeStatus, int expense){
        this.integratedRoute=integratedRoute;
        this.member=member;
        this.routeStatus=routeStatus;
        this.expense = expense;
    }
}
