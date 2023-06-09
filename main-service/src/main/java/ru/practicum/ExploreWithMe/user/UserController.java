package ru.practicum.ExploreWithMe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(required = false) Collection<Long> ids, @RequestParam(defaultValue = "0") int from,
                             @RequestParam(defaultValue = "10") int size) {
        if (ids != null) {
            return userService.getIds(ids, from, size);
        }
        return userService.getAll(from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        return userService.add(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto delete(@PathVariable long userId) {
        return userService.delete(userId);
    }
}
