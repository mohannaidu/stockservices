package org.mimos.stockservices.service.impl;

import org.mimos.stockservices.service.StockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockInfoServiceImpl implements StockInfoService {

    private final Logger log = LoggerFactory.getLogger(StockInfoServiceImpl.class);

}
