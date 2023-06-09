package ru.practicum.exploreWithMeStats.hits;

import lombok.experimental.UtilityClass;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMeStats.hits.model.EndpointHit;
import ru.practicum.exploreWithMeStats.hits.model.ViewStats;

@UtilityClass
public class HitMapper {
    public static EndpointHit fromHitToEndpointHit(HitDto hit) {
        return EndpointHit.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static HitDto toHitDto(EndpointHit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(Math.toIntExact(viewStats.getHits()))
                .build();
    }
}
