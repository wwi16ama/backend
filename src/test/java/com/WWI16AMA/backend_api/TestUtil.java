package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.SerializationHelp.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.data.repository.CrudRepository;

import java.util.Random;

class TestUtil {

    /**
     * returns an id from an repository, which points to no entry, so repository.existsById(unusedId) returns false.
     */
    static int getUnusedId(CrudRepository repository) {
        //get random id with no associated member
        int randomId;
        Random random = new Random();
        do {
            randomId = random.nextInt();
        } while (repository.existsById(randomId));
        return randomId;
    }

    static String marshal(Object o) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectWriter ow = new CustomObjectMapper().writer();
        return ow.writeValueAsString(o);
    }
}
