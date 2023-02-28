package com.nttd.ms.monedero.bootcoin.service.impl;

import com.nttd.ms.monedero.bootcoin.dto.AceptarCompraBootCoin;
import com.nttd.ms.monedero.bootcoin.dto.SolicitarCompraBootCoin;
import com.nttd.ms.monedero.bootcoin.entity.Monedero;
import com.nttd.ms.monedero.bootcoin.repository.MonederoRepository;
import com.nttd.ms.monedero.bootcoin.service.MonederoService;
import com.nttd.ms.monedero.bootcoin.util.MonederoUtil;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

@ApplicationScoped
public class MonederoServiceImpl implements MonederoService {

    @Inject
    MonederoRepository monederoRepository;

    @Inject
    ReactiveRedisDataSource redisDataSource;

    @Inject
    MonederoUtil util;

    @Override
    public Uni<Monedero> guardarMonedero(Monedero monedero) {
        return monederoRepository.persist(monedero);
    }

    @Override
    public Uni<String> generarSolicitudcompraBootCoin(String id, Double monto, String modoPago) {
        return this.buscarMonederoXId(id)
                .onItem()
                .transform(monedero -> {
                   SolicitarCompraBootCoin compra = new SolicitarCompraBootCoin();
                   compra.setMonto(monto);
                   compra.setModoPago(modoPago);
                   compra.setTipoNumero(monedero.getTipoNumero());
                   compra.setNumero(monedero.getNumero());
                   compra.setNumeroSolicitud(util.generarCodigo());
                   return compra;
                })
                .call(c -> this.guardarSolicitud("generar-solicitud: " + c.getNumeroSolicitud(), c))
                .onItem().transformToUni(c -> Uni.createFrom().item("Se realizo la solicitud"));
    }

    @Override
    public Uni<String> aceptarSolicitudcompraBootCoin(String id, String numeroSolicitud, String numeroCuentaSolicitud) {
        return this.buscarMonederoXId(id)
                .onItem()
                .transformToUni(monedero -> this.obtenerSolicitudes()
                        .select()
                        .when(solicitud -> Uni.createFrom().item( solicitud.getNumeroSolicitud().equals(numeroSolicitud) ))
                        .toUni()
                        .onItem()
                        .ifNull()
                        .failWith(() -> new BadRequestException("Numero de solicitud incorrecto."))
                        .onItem()
                        .transform(scb -> {
                            AceptarCompraBootCoin aceptarCompra = new AceptarCompraBootCoin();
                            aceptarCompra.setMonto(scb.getMonto());
                            aceptarCompra.setModoPago(scb.getModoPago());
                            aceptarCompra.setNumeroTransaccion(util.generarNumeroTransaccion());
                            aceptarCompra.setCTipoNumero(scb.getTipoNumero());
                            aceptarCompra.setCNumero(scb.getNumero());
                            aceptarCompra.setVTipoNumero(scb.getTipoNumero());
                            aceptarCompra.setVNumero(numeroCuentaSolicitud);
                            return aceptarCompra;
                        })
                )
                .call(acb -> this.guardarSolicitudAceptada("aceptar-solicitud: " + acb.getNumeroTransaccion(), acb)
                        //.call(() -> this.eliminarSolicitud("generar-solicitud: " + numeroSolicitud))
                )
                .onItem().transformToUni(c -> Uni.createFrom().item("Se genero código de transacción"));
    }


    /* Generar solicitud de compra */
    @Override
    public Multi<SolicitarCompraBootCoin> obtenerSolicitudes() {
        return this.redisDataSource.key().keys("generar-solicitud: " + "*")
                .onItem()
                .<String>disjoint()
                .flatMap(key -> this.obtenerSolicitud(key).toMulti());
    }

    private Uni<Void> guardarSolicitud(String key, SolicitarCompraBootCoin bootCoin) {
        return redisDataSource
                .value(String.class, SolicitarCompraBootCoin.class)
                .set(key, bootCoin);
    }

    private Uni<SolicitarCompraBootCoin> obtenerSolicitud(String key) {
        return this.redisDataSource
                .value(String.class, SolicitarCompraBootCoin.class)
                .get(key);
    }

    /*@Override
    public Uni<SolicitarCompraBootCoin> eliminarSolicitud(String key) {
        return this.redisDataSource
                .value(String.class, SolicitarCompraBootCoin.class)
                .getdel(key);
    }*/

    /* Aceptar solicitud de compra */
    @Override
    public Multi<AceptarCompraBootCoin> obtenerSolicitudesAceptadas() {
        return this.redisDataSource.key().keys("aceptar-solicitud: " + "*")
                .onItem()
                .<String>disjoint()
                .flatMap(key -> this.obtenerSolicitudAceptada(key).toMulti());
    }

    private Uni<Void> guardarSolicitudAceptada(String key, AceptarCompraBootCoin bootCoin) {
        return redisDataSource
                .value(String.class, AceptarCompraBootCoin.class)
                .set(key, bootCoin);
    }

    private Uni<AceptarCompraBootCoin> obtenerSolicitudAceptada(String key) {
        return this.redisDataSource
                .value(String.class, AceptarCompraBootCoin.class)
                .get(key);
    }

    /*public Uni<AceptarCompraBootCoin> eliminarSolicitudAceptada(String key) {
        return this.redisDataSource
                .value(String.class, AceptarCompraBootCoin.class)
                .getdel(key);
    }*/

    //@Override
    private Uni<Monedero> buscarMonederoXId(String id) {
        return monederoRepository.findById(new ObjectId(id))
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("No existe un monedero con el id " + id));
    }

}
