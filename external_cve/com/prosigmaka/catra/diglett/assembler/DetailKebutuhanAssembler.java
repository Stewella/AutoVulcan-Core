package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.DetailKebutuhanDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.PicClientRepository;
import com.prosigmaka.catra.diglett.repository.PosisiRepository;
import com.prosigmaka.catra.diglett.repository.ServicesRepo;

@Component
public class DetailKebutuhanAssembler implements InterfaceAssembler<DetailKebutuhan, DetailKebutuhanDto> {
	@Autowired
	private PosisiRepository posisiRepo;
	@Autowired
	private ServicesRepo serviceRepo;
	@Autowired
	private PicClientRepository picRepo;
	@Autowired
	private ClientRepository clientRepo;

	@Override
	public DetailKebutuhan fromDto(DetailKebutuhanDto dto) {
		if (dto == null)
			return null;
		DetailKebutuhan entity = new DetailKebutuhan();

		if (dto.getPicName() != null)
			entity.setIdPic(picRepo.findByPicName(dto.getPicName()).getId());
		if (dto.getPosisi() != null)
			entity.setIdPosisi(posisiRepo.findByPosisi(dto.getPosisi()).getId());
		if (dto.getService() != null)
			entity.setIdService(serviceRepo.findByService(dto.getService()).getId());
		if (dto.getLevel() != null)
			entity.setLevel(dto.getLevel());
		if (dto.getJumlah() != null)
			entity.setJumlah(dto.getJumlah());
		return entity;
	}

	@Override
	public DetailKebutuhanDto fromEntity(DetailKebutuhan entity) {
		if (entity == null)
			return null;

		String picName = null;
		String posisi = null;
		String service = null;

		if (entity.getIdPic() != null)
			picName = picRepo.findById(entity.getIdPic()).get().getPicName();
			String idClient = (picRepo.findById(entity.getIdPic()).get().getIdClient());
			String nameClient = clientRepo.findById(idClient).get().getNickname();
		if (entity.getIdPosisi() != null)
			posisi = posisiRepo.findById(entity.getIdPosisi()).get().getPosisi();
		if (entity.getIdService() != null)
			service = serviceRepo.findById(entity.getIdService()).get().getService();

		return DetailKebutuhanDto.builder()
				.kode(entity.getKode())
				.id(entity.getId())
				.idPic(entity.getIdPic())
				.idPosisi(entity.getIdPosisi())
				.client(nameClient)
				.idService(entity.getIdService())
				.picName(picName)
				.posisi(posisi)
				.service(service)
				.level(entity.getLevel())
				.jumlah(entity.getJumlah())
				.build();
	}
	
	
	

}
