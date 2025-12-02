package com.prosigmaka.catra.diglett.database;

import com.prosigmaka.catra.diglett.database.seeders.BulanSeeder;
import com.prosigmaka.catra.diglett.repository.BulanRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseSeeder {
	protected final BulanRepository bulanRepository;

	public DatabaseSeeder(BulanRepository bulanRepository) {
		this.bulanRepository = bulanRepository;
	}

	public void initialBulan() {
		new BulanSeeder(bulanRepository).run();
	}
	
	@PostConstruct
	public void initial() {
		initialBulan();
	}

}
