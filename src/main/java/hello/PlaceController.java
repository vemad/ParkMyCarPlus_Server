package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceController {

    private static final String template = "This place is, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/place")
    public Place place(@RequestParam(value="name", defaultValue="Huge") String name) {
        return new Place(counter.incrementAndGet(),
                            String.format(template, name));
    }
}