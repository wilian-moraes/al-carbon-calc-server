package br.com.actionlabs.carboncalc.model;

import lombok.Data;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.actionlabs.carboncalc.enums.TransportationType;

@Data
@Document("calculation")
public class Calculation {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String uf;
    private Integer energyConsumption;
    private List<TransportationInfo> transportation;
    private Integer solidWasteTotal;
    private Double recyclePercentage;
    private Double carbonFootprintResult;

    @Data
    public static class TransportationInfo {
        private TransportationType type;
        private int monthlyDistance;
    }
}
