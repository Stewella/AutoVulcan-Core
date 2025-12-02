package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.OptiDto;
import com.prosigmaka.catra.diglett.model.dto.OptiDto.OptiDtoBuilder;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;
import com.prosigmaka.catra.diglett.repository.PicClientRepository;
import com.prosigmaka.catra.diglett.repository.PlottingRepository;
import com.prosigmaka.catra.diglett.repository.PosisiRepository;

@Component
public class OptiAssembler implements InterfaceAssembler<DetailKebutuhan, OptiDto> {
	@Autowired
	private PosisiRepository posisiRepo;
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private PicClientRepository picRepo;
	@Autowired
	private PlottingRepository plotRepo;
	@Autowired
	private DetailKebutuhanRepository detKebRepo;
	
	@Override
	public DetailKebutuhan fromDto(OptiDto dto) {
		if (dto == null)
			return null;
		DetailKebutuhan entity = new DetailKebutuhan();
		
//		if(dto.getNamaClient() != null)
//			entity.setPic()
		if (dto.getPosisi() != null)
			entity.setIdPosisi(posisiRepo.findByPosisi(dto.getPosisi()).getId());
		if (dto.getSisaKebutuhan() != null)
			entity.setJumlah(dto.getSisaKebutuhan());
		return entity;
	}
	
	@Override
	public OptiDto fromEntity(DetailKebutuhan entity) {
		if (entity == null)
			return null;
		String picName = null;
		String posisi = null;
		//String kodeKeb = null;
		Integer win = Integer.valueOf(plotRepo.winPlot(entity.getId()));
		Integer drop = Integer.valueOf(plotRepo.dropPlot(entity.getId()));
		Integer onProcess = Integer.valueOf(plotRepo.plotting(entity.getId())) - win - drop;
		
		
		
		if (entity.getIdPic() != null)
			picName = picRepo.findById(entity.getIdPic()).get().getPicName();
			String idClient = (picRepo.findById(entity.getIdPic()).get().getIdClient());
			String nameClient = clientRepo.findById(idClient).get().getNama();
		if (entity.getIdPosisi() != null)
			posisi = posisiRepo.findById(entity.getIdPosisi()).get().getPosisi();
		
	
		return OptiDto.builder()
				.namaClient(nameClient)
				.posisi(posisi)
				.sisaKebutuhan(entity.getJumlah())
				.onProcess(onProcess)
				.win(win)
				.drop(drop)
				.build();
	}

}
