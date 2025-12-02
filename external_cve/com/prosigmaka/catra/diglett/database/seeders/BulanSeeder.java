package com.prosigmaka.catra.diglett.database.seeders;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.prosigmaka.catra.diglett.model.entity.Bulan;
import com.prosigmaka.catra.diglett.repository.BulanRepository;



public class BulanSeeder {
	
	private Logger logger = LoggerFactory.getLogger(BulanSeeder.class);
	
	@Autowired
	private BulanRepository bulanRepository;
	
	public BulanSeeder(BulanRepository bulanRepository) {
		this.bulanRepository=bulanRepository;
	}
	
	public void run() {
		try {
			this.data();
		} catch (Exception e) {
			logger.error("An exception occurred", e);
		}
	}
	
	public void data() {
		this.entity(1, "january");
		this.entity(2, "february");
		this.entity(3, "march");
		this.entity(4, "april");
		this.entity(5, "may");
		this.entity(6, "june");
		this.entity(7, "july");
		this.entity(8, "august");
		this.entity(9, "september");
		this.entity(10, "october");
		this.entity(11, "november");
		this.entity(12, "december");
	}
	
	public void entity(Integer id, String month) {
		Optional<Bulan> optional = this.bulanRepository.findById(id);
		if(this.bulanRepository.findAll().size() == 0 || !optional.isPresent()) {
			Bulan data = new Bulan();
			data.setId(id);
			data.setMonth(month);
			
			Bulan bulan=this.bulanRepository.save(data);
			
		}
		
	}

}
