package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.CarbonCalculationResultDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcRequestDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcResponseDTO;
import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.model.*;
import br.com.actionlabs.carboncalc.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculationService {

    private final CalculationRepository calculationRepository;
    private final EnergyEmissionFactorRepository energyFactorRepository;
    private final TransportationEmissionFactorRepository transportationFactorRepository;
    private final SolidWasteEmissionFactorRepository solidWasteFactorRepository;

    public StartCalcResponseDTO createCalculation(StartCalcRequestDTO requestDTO) {
        String uf = requestDTO.getUf().toUpperCase();
        if(!energyFactorRepository.existsById(uf)) {
            throw new IllegalArgumentException("Invalid UF: " + uf + " The state provided does not have a registered emission factor");
        }

        Calculation newCalculation = new Calculation();
        newCalculation.setName(requestDTO.getName());
        newCalculation.setEmail(requestDTO.getEmail());
        newCalculation.setPhoneNumber(requestDTO.getPhoneNumber());
        newCalculation.setUf(requestDTO.getUf());
        Calculation savedCalculation = calculationRepository.save(newCalculation);
        StartCalcResponseDTO responseDTO = new StartCalcResponseDTO();
        responseDTO.setId(savedCalculation.getId());
        return responseDTO;
    }

    public void updateCalculationInfo(UpdateCalcInfoRequestDTO request) {
        Calculation calculation = calculationRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Calculation not found with ID: " + request.getId()));

        List<Calculation.TransportationInfo> transportationInfos = new ArrayList<>();
        request.getTransportation().forEach(dto -> {
            Calculation.TransportationInfo info = new Calculation.TransportationInfo();
            info.setType(dto.getType());
            info.setMonthlyDistance(dto.getMonthlyDistance());
            transportationInfos.add(info);
        });

        calculation.setEnergyConsumption(request.getEnergyConsumption());
        calculation.setTransportation(transportationInfos);
        calculation.setSolidWasteTotal(request.getSolidWasteTotal());
        calculation.setRecyclePercentage(request.getRecyclePercentage());
        calculationRepository.save(calculation);
    }

    public CarbonCalculationResultDTO calculateAndGetResult(String id) {
        Calculation calculation = calculationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calculation not found with ID: " + id));

        if (calculation.getEnergyConsumption() == null || calculation.getTransportation() == null || calculation.getSolidWasteTotal() == null) {
          throw new IllegalStateException("The consumption data for calculating ID " + id + " was not filled in. Call the PUT /info endpoint first.");
        }        

        double energy = calculateEnergyEmission(calculation);
        double transportation = calculateTransportationEmission(calculation);
        double solidWaste = calculateSolidWasteEmission(calculation);
        double total = energy + transportation + solidWaste;

        calculation.setCarbonFootprintResult(total);
        calculationRepository.save(calculation);

        CarbonCalculationResultDTO resultDTO = new CarbonCalculationResultDTO();
        resultDTO.setEnergy(energy);
        resultDTO.setTransportation(transportation);
        resultDTO.setSolidWaste(solidWaste);
        resultDTO.setTotal(total);
        return resultDTO;
    }

    private double calculateEnergyEmission(Calculation calculation) {
        EnergyEmissionFactor factor = energyFactorRepository.findById(calculation.getUf())
                .orElseThrow(() -> new RuntimeException("Energy factor not found for UF: " + calculation.getUf()));
        return calculation.getEnergyConsumption() * factor.getFactor();
    }

    private double calculateTransportationEmission(Calculation calculation) {
        return calculation.getTransportation().stream()
            .mapToDouble(info -> {
                TransportationEmissionFactor factor = transportationFactorRepository.findById(info.getType())
                        .orElseThrow(() -> new RuntimeException("Transport factor not found for type: " + info.getType()));
                return info.getMonthlyDistance() * factor.getFactor();
            })
            .sum();
    }

    private double calculateSolidWasteEmission(Calculation calculation) {
        SolidWasteEmissionFactor factors = solidWasteFactorRepository.findById(calculation.getUf())
                .orElseThrow(() -> new RuntimeException("Solid waste factors not found for the UF: " + calculation.getUf()));

        double totalWaste = calculation.getSolidWasteTotal();
        double recyclePercentage = calculation.getRecyclePercentage();
        
        double recyclableEmission = (totalWaste * recyclePercentage) * factors.getRecyclableFactor();
        double nonRecyclableEmission = (totalWaste * (1 - recyclePercentage)) * factors.getNonRecyclableFactor();

        return recyclableEmission + nonRecyclableEmission;
    }
}