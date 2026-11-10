package com.example.repository;

import com.example.models.Places;
import java.util.List;

public interface PlaceRepository extends CrubRepository<Places> {
    List<Places> findAllOnYourCity(Long userid);
}
