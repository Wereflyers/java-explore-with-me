package ru.practicum.ExploreWithMe.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ExploreWithMe.enums.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Request> {

    List<Request> findAllByRequester(Long userId);

    Request findByRequesterAndEvent(Long requesterId, Long eventId);

    List<Request> findAllByStatusAndEvent(RequestStatus state, Long eventId);

    List<Request> findAllByEvent(Long eventId);
}
