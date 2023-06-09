package ru.practicum.ExploreWithMe.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.CategoryMapper;
import ru.practicum.ExploreWithMe.category.CategoryRepository;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.NewCompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ExploreWithMe.enums.RequestStatus;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.dto.EventShortDto;
import ru.practicum.ExploreWithMe.event.model.EventShort;
import ru.practicum.ExploreWithMe.exception.DuplicateException;
import ru.practicum.ExploreWithMe.request.RequestRepository;
import ru.practicum.ExploreWithMe.statistics.StatisticService;
import ru.practicum.ExploreWithMe.user.User;
import ru.practicum.ExploreWithMe.user.UserRepository;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatisticService statisticService;

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        try {
            Compilation compilation = compilationRepository.save(new Compilation(null, newCompilationDto.getTitle(),
                    newCompilationDto.getPinned(), newCompilationDto.getEvents()));
            return createResponse(compilation);
        } catch (Exception e) {
            throw new DuplicateException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NullPointerException("Compilation with id=" + compId + " was not found"));
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NullPointerException("Compilation with id=" + compId + " was not found"));
        if (updateCompilationRequest.getTitle() != null) compilation.setTitle(updateCompilationRequest.getTitle());
        if (updateCompilationRequest.getPinned() != null) compilation.setPinned(updateCompilationRequest.getPinned());
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(updateCompilationRequest.getEvents());
        }
        return createResponse(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto get(Long compId) {
        return createResponse(compilationRepository.findById(compId).orElseThrow(() -> new NullPointerException("Compilation with id=" + compId + " was not found")));
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationRepository.findAll(page).stream()
                    .map(this::createResponse)
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAllByPinned(pinned, page).stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    private CompilationDto createResponse(Compilation compilation) {
        List<EventShortDto> eventsShort = eventRepository.findEventsShort(compilation.getEvents()).stream()
                .map(this::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventsShort)
                .build();
    }

    private EventShortDto toEventShortDto(EventShort eventShort) {
        Integer confirmedReq = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, eventShort.getId()).size();
        CategoryDto category = CategoryMapper.toCategoryDto(categoryRepository.findById(eventShort.getCategory()).orElseThrow());
        User user = userRepository.findById(eventShort.getInitiator()).orElseThrow();
        return EventShortDto.builder()
                .annotation(eventShort.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedReq)
                .eventDate(eventShort.getEventDate())
                .id(eventShort.getId())
                .initiator(new UserShortDto(user.getId(), user.getName()))
                .paid(eventShort.getPaid())
                .title(eventShort.getTitle())
                .views(statisticService.getViews(eventShort.getId()).getHits())
                .build();
    }
}
