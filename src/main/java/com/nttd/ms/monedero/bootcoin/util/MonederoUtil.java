package com.nttd.ms.monedero.bootcoin.util;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MonederoUtil {

    public String generarCodigo(){
        int codigo = (int) (10000 + Math.random() * 90000);
        return String.valueOf(codigo);
    }

    public String generarNumeroTransaccion(){
        int codigo = (int) (10000000 + Math.random() * 90000000);
        return String.valueOf(codigo);
    }

}
