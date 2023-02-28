package com.nttd.ms.monedero.bootcoin.repository;

import com.nttd.ms.monedero.bootcoin.entity.Monedero;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MonederoRepository implements ReactivePanacheMongoRepository<Monedero> {
}
