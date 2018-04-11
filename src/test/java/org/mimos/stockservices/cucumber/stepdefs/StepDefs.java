package org.mimos.stockservices.cucumber.stepdefs;

import org.mimos.stockservices.StockservicesApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = StockservicesApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
