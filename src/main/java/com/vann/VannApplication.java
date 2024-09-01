package com.vann;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vann.utils.LogHandler;

@SpringBootApplication
public class VannApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(VannApplication.class, args);
        } catch (Exception e) {
			if (e.getClass().getName().contains("SilentExitException")) {
				LogHandler.silentExitException();
			} else {
				LogHandler.applicationStartupError(e.getMessage());
				throw e;
			}
		}
	}
}
