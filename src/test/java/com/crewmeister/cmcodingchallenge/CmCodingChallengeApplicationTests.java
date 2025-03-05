package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.config.CmCodingChallengeTestConfigurer;
import com.crewmeister.cmcodingchallenge.service.impl.ExchangeRateServiceImpl;
import com.crewmeister.cmcodingchallenge.util.BundesbankClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = {CmCodingChallengeTestConfigurer.class})
class CmCodingChallengeApplicationTests {

	@Test
	void contextLoads() {
	}

}
