package com.arya.hmac.auth.service.impl;

import com.arya.hmac.auth.model.SuperHero;
import com.arya.hmac.auth.service.SuperHeroService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperHeroServiceImpl implements SuperHeroService {

    private static final List<SuperHero> superHeroes = new ArrayList<>();

    static {
        superHeroes.add(SuperHero.builder().id(1).name("Wade").superName("Deadpool").profession("Street fighter").age(28).canFly(false).build());
        superHeroes.add(SuperHero.builder().id(2).name("Bruce").superName("Hulk").profession("Doctor").age(50).canFly(false).build());
        superHeroes.add(SuperHero.builder().id(3).name("Steve").superName("Captain America").profession("Solder").age(120).canFly(false).build());
        superHeroes.add(SuperHero.builder().id(4).name("Tony").superName("Iron Man").profession("Business man").age(45).canFly(true).build());
        superHeroes.add(SuperHero.builder().id(5).name("Peter").superName("Spider Man").profession("Student").age(21).canFly(true).build());
    }


    @Override
    public List<SuperHero> findAll() {
        return superHeroes;
    }

    @Override
    public SuperHero findById(int id) {
        return superHeroes.stream()
                .filter(superHero -> superHero.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public SuperHero save(SuperHero superHero) {
        superHeroes.add(superHero);
        return superHero;
    }

    @Override
    public SuperHero update(int id, SuperHero superHero) {
        SuperHero fromStore = findById(id);
        int index = superHeroes.indexOf(fromStore);
        superHeroes.remove(index);

        superHero.setId(id);
        superHeroes.add(index, superHero);

        return (superHero);
    }

    @Override
    public void delete(int id) {
        superHeroes.remove(findById(id));
    }
}