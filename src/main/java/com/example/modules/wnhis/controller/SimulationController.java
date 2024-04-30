package com.example.modules.wnhis.controller;


import com.example.modules.wnhis.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Toomth
 * @date 2021/1/15 14:51
 * @explain
 */

@RestController
public class SimulationController {

    @Autowired
    private SimulationService simulationService;


}
