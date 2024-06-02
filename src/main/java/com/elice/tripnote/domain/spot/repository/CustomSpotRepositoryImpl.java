package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.QSpot;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomSpotRepositoryImpl implements CustomSpotRepository{
    private final JPAQueryFactory query;

    private final QSpot spot = new QSpot("s");
    private final QRouteSpot routeSpot = new QRouteSpot("rs");

    public List<SpotResponseDTO> findByRouteIds(Long integratedRouteId){

        return query
                .select(Projections.constructor(SpotResponseDTO.class,
                        spot.id,
                        spot.location,
                        spot.region
                ))
                .from(spot)
                .join(routeSpot).on(routeSpot.spot.id.eq(spot.id))
                .where(routeSpot.route.id.eq(integratedRouteId))
                .fetch();

    }
}
