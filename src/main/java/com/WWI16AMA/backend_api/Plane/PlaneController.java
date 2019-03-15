package com.WWI16AMA.backend_api.Plane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "planes")
public class PlaneController {

    @Autowired
    private PlaneRepository planeRepository;

    /**
     * Get Request which delivers all Users.
     * <p>
     * //     * @param limit     Defines the amount of objects to receive
     * //     * @param start     Defines the page to view
     * //     * @param direction Defines the sorting order
     * //     * @param orderBy   Defines the field by which the sort is to be performed
     * //     * @return Returns an Iterable of Airplanes paged and sorted by given parameters
     */
    @GetMapping(value = "")
    public Iterable<Plane> getAllPlanesPaged(
//            @RequestParam(defaultValue = "20") int limit,
//            @RequestParam(defaultValue = "0") int start,
//            @RequestParam(defaultValue = "asc") String direction,
//            @RequestParam(defaultValue = "name") String orderBy
    ) throws IllegalArgumentException {

//        Sort sort = new Sort(Sort.Direction.fromString(direction), orderBy);

//        return planeRepository.findAll(PageRequest.of(start, limit, sort)).stream()
//                .collect(toList());
        return planeRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Plane> detail(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @PostMapping(path = "")
    public Plane create(@RequestBody Plane reqPlane) {

        if (reqPlane.getId() != null) {
            throw new IllegalArgumentException("Plane has the ID: " + reqPlane.getId() +
                    ". Id has to be null when a new plane shall be created");
        }

        planeRepository.save(reqPlane);

        return reqPlane;
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<Plane> put(@RequestBody Plane putPlane, @PathVariable int id) {

        if (planeRepository.existsById(id)) {
            putPlane.setId(id);
            planeRepository.save(putPlane);
        } else {
            throw new NoSuchElementException("Plane with id " + id + " does not exist.");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Plane> delete(@PathVariable int id) {

        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist"));
        planeRepository.delete(plane);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
