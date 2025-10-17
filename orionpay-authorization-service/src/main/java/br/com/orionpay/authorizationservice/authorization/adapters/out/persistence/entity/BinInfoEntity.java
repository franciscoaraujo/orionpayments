package br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "bin_info")
@Data
public class BinInfoEntity implements Serializable {

    @Id
    private String binRange;
    private String brand;
    private String cardType;
    private String cardLevel;
    private String issuingBank;
    private String countryCode;
}