package com.nttd.ms.monedero.bootcoin.resource;

import com.nttd.ms.monedero.bootcoin.dto.AceptarCompraBootCoin;
import com.nttd.ms.monedero.bootcoin.dto.SolicitarCompraBootCoin;
import com.nttd.ms.monedero.bootcoin.entity.Monedero;
import com.nttd.ms.monedero.bootcoin.service.MonederoService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/monedero")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MonederoResource {

    @Inject
    MonederoService monederoService;

    @GET
    public Multi<Monedero> listarMonedero() {
        return monederoService.listarMonedero();
    }

    @POST
    @Transactional
    public Uni<Monedero> guardarMonedero(Monedero monedero) {
        return monederoService.guardarMonedero(monedero);
    }

    @PUT
    @Path("/generar-compra-bootCoin/{id}")
    public Uni<String> generarSolicitudcompraBootCoin(@PathParam("id") String id,
                                       @QueryParam("monto") Double monto,
                                       @QueryParam("modoPago") String modoPago){
        return monederoService.generarSolicitudcompraBootCoin(id, monto, modoPago);
    }

    @PUT
    @Path("/aceptar-compra-bootCoin/{id}")
    public Uni<String> aceptarSolicitudcompraBootCoin(@PathParam("id") String id,
                                                      @QueryParam("numeroSolicitud") String numeroSolicitud,
                                                      @QueryParam("numeroCuentaSolicitud") String numeroCuentaSolicitud){
        return monederoService.aceptarSolicitudcompraBootCoin(id, numeroSolicitud, numeroCuentaSolicitud);
    }

    @GET
    @Path("/solicitudes-bootCoin")
    public Multi<SolicitarCompraBootCoin> obtenerSolicitudes(){
        return monederoService.obtenerSolicitudes();
    }

    @GET
    @Path("/solicitudes-aceptadas-bootCoin")
    public Multi<AceptarCompraBootCoin> obtenerSolicitudesAceptadas(){
        return monederoService.obtenerSolicitudesAceptadas();
    }

    /*@GET
    @Path("/{id}")
    public Uni<Monedero> buscarMonederoXId(@PathParam("id") String id){
        return monederoService.buscarMonederoXId(id);
    }*/

    /*@DELETE
    @Path("/eliminar")
    public Uni<SolicitarCompraBootCoin> eliminarSolicitud(@QueryParam("key") String key){
        return monederoService.eliminarSolicitud(key);
    }*/

}
