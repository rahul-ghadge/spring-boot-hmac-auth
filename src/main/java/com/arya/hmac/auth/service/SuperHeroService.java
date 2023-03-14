package com.arya.hmac.auth.service;


import com.arya.hmac.auth.model.SuperHero;

import java.util.List;

public interface SuperHeroService {

    List<?> findAll();

    SuperHero findById(int id);

    SuperHero save(SuperHero superHero);

    SuperHero update(int id, SuperHero superHero);

    void delete(int id);
}