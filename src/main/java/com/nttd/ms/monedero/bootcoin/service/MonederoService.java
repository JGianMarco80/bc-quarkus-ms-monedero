package com.nttd.ms.monedero.bootcoin.service;

import com.nttd.ms.monedero.bootcoin.dto.AceptarCompraBootCoin;
import com.nttd.ms.monedero.bootcoin.dto.SolicitarCompraBootCoin;
import com.nttd.ms.monedero.bootcoin.entity.Monedero;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface MonederoService {

    //Multi<Monedero> listarMonedero();

    //Uni<Monedero> buscarMonederoXId(String id);

    Uni<Monedero> guardarMonedero(Monedero monedero);

    Uni<String> generarSolicitudcompraBootCoin(String id, Double monto, String modoPago);

    Uni<String> aceptarSolicitudcompraBootCoin(String id, String numeroSolicitud, String numeroCuentaSolicitud);

    Multi<SolicitarCompraBootCoin> obtenerSolicitudes();

    Multi<AceptarCompraBootCoin> obtenerSolicitudesAceptadas();

    //Uni<SolicitarCompraBootCoin> eliminarSolicitud(String key);
}
