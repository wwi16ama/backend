package com.WWI16AMA.backend_api.PlaneLog;

import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "planeLog")
public class PlaneLogController {

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

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlaneLog> detail(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")).getPlaneLog(), HttpStatus.OK);
    }


}
