package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.exception.NotFoundException;
import com.prosigmaka.catra.diglett.model.entity.Industry;
import com.prosigmaka.catra.diglett.model.entity.Sales;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.ClientDto;
import com.prosigmaka.catra.diglett.model.entity.Client;
import com.prosigmaka.catra.diglett.repository.IndustryRepository;
import com.prosigmaka.catra.diglett.repository.SalesRepository;

import java.util.Optional;

@Component
public class ClientAssembler implements InterfaceAssembler<Client, ClientDto> {
	private final IndustryRepository industryRepository;
	
	private final SalesRepository salesRepository;

	public ClientAssembler(IndustryRepository industryRepository, SalesRepository salesRepository) {
		this.industryRepository = industryRepository;
		this.salesRepository = salesRepository;
	}

	@Override
	public Client fromDto(ClientDto dto) {
		if (dto == null)
			return null;
		Optional<Industry> industry = industryRepository.findByNama(dto.getIndustri());
		if (!industry.isPresent()){
			throw new NotFoundException(dto.getIndustri());
		}

		Optional<Sales> sales = salesRepository.findByNama(dto.getSales());
		if (!sales.isPresent()){
			throw new NotFoundException(dto.getSales());
		}

		Client entity = new Client();
		if (dto.getId() != null)
			entity.setId(dto.getId());
		if (dto.getNama() != null)
			entity.setNama(dto.getNama());
		if (dto.getIndustri() != null)
			entity.setIdIndustri(industry.get().getId());
		if (dto.getAlamat() != null)
			entity.setAlamat(dto.getAlamat());
		if (dto.getNoHp() != null)
			entity.setClNoHpPrimary(dto.getNoHp());
		if (dto.getEmail() != null)
			entity.setClEmailPrimary(dto.getEmail());
		if(dto.getSales() != null)
			entity.setIdSales(sales.get().getId());
		if(dto.getNickname() != null)
			entity.setNickname(dto.getNickname());
		return entity;
	}

	@Override
	public ClientDto fromEntity(Client entity) {
		if (entity == null)
			return null;
		String namaIndustri = null;
		String namaSales = null;
		if (entity.getIdIndustri() != null)
			namaIndustri = industryRepository.findById(entity.getIdIndustri()).get().getNama();
		if (entity.getIdSales() != null)
			namaSales = salesRepository.findById(entity.getIdSales()).get().getNama();
		return ClientDto.builder()
				.id(entity.getId())
				.nama(entity.getNama())
				.nickname(entity.getNickname())
				.sales(namaSales)
				.industri(namaIndustri)
				.alamat(entity.getAlamat())
				.email(entity.getClEmailPrimary())
				.noHp(entity.getClNoHpPrimary())
				.build();
	}

}
