package com.angus.esjob.service;

import com.angus.esjob.annotation.JobTrace;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

	@JobTrace
	public void tester(String name) {
		System.err.println("name: " + name);
	}
}
