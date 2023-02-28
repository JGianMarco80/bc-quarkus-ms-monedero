package com.nttd.ms.monedero.bootcoin.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@MongoEntity(collection = "monedero_bootcoin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monedero {

    private ObjectId id;

    //1: DNI
    //2: Carnet de extranjeria
    //3: RUC
    private String tipoDocumento;

    private String numeroDocumento;

    //1: celular
    //2: numero de cuenta
    private String tipoNumero;

    private String numero;

    private String correoElectronico;

}
