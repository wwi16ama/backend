package com.WWI16AMA.backend_api.Plane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping(path = "planes")
public class PlaneController {

    @Autowired
    private PlaneRepository planeRepository;

    /**
     * Get Request which delivers all Users.
     *
     * @param limit     Defines the amount of objects to receive
     * @param start     Defines the page to view
     * @param direction Defines the sorting order
     * @param orderBy   Defines the field by which the sort is to be performed
     * @return Returns an Iterable of Airplanes paged and sorted by given parameters
     */
    @GetMapping(value = "")
    public Iterable<Plane> getAllPlanesPaged(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "name") String orderBy
    ) throws IllegalArgumentException {

        Sort sort = new Sort(Sort.Direction.fromString(direction), orderBy);

        return planeRepository.findAll(PageRequest.of(start, limit, sort)).stream()
                .collect(toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Plane> detail(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with the id " + id + " does not exist")), HttpStatus.OK);
    }

    @PostMapping(path = "")
    public Plane create(@RequestBody Plane reqPlane) {

        if (reqPlane.getId() != null) {
            throw new IllegalArgumentException("Plane has the ID: " + reqPlane.getId() +
                    ". Id has to be null when a new member shall be created");
        }

        planeRepository.save(reqPlane);

        return reqPlane;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Plane> put(@RequestBody Plane putPlane, @PathVariable int id) {

        if (planeRepository.existsById(id)) {
            putPlane.setId(id);
            planeRepository.save(putPlane);
        } else {
            throw new NoSuchElementException("Member with id " + id + " does not exist.");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Plane> delete(@PathVariable int id) {

        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with the id " + id + " does not exist"));
        planeRepository.delete(plane);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
