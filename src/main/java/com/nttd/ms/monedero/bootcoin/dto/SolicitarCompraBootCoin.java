package com.nttd.ms.monedero.bootcoin.dto;

import lombok.Data;

@Data
public class SolicitarCompraBootCoin {

    private Double monto;

    //1: billetera digital
    //2: transferencia
    private String modoPago;

    //1: celular
    //2: numero de cuenta
    private String tipoNumero;

    private String numero;

    private String numeroSolicitud;

}
