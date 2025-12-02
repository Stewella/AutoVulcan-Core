package com.prosigmaka.catra.diglett.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import com.prosigmaka.catra.diglett.assembler.PlottingAssembler;
import com.prosigmaka.catra.diglett.model.dto.FormPlottingDto;
import com.prosigmaka.catra.diglett.model.dto.UpdatePlottingDetailDto;
import com.prosigmaka.catra.diglett.model.entity.Plotting;
import com.prosigmaka.catra.diglett.model.entity.PlottingDetail;
import com.prosigmaka.catra.diglett.model.entity.StepDetail;
import com.prosigmaka.catra.diglett.repository.PlottingDetailRepository;
import com.prosigmaka.catra.diglett.repository.PlottingRepository;
import com.prosigmaka.catra.diglett.repository.StepDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PlottingServiceImpl implements PlottingService {

	private final PlottingRepository repository;
	private final PlottingDetailRepository plottingDetailRepository;
	private final StepDetailRepository stepDetailRepository;
	private final PlottingAssembler assembler;

	public PlottingServiceImpl(PlottingRepository repository, PlottingDetailRepository plottingDetailRepository, StepDetailRepository stepDetailRepository, PlottingAssembler assembler) {
		this.repository = repository;
		this.plottingDetailRepository = plottingDetailRepository;
		this.stepDetailRepository = stepDetailRepository;
		this.assembler = assembler;
	}

	@Override
	public List<Object> insertPlotting(FormPlottingDto dto) {
		Plotting plotting = repository.save(assembler.fromDto(dto));
		repository.save(plotting);
		List<StepDetail> stepDetailList = stepDetailRepository
				.findAllByClientId(plotting.getDetailKebutuhan().getPicClient().getClient().getId());
		List<PlottingDetail> plottingDetailList = new ArrayList<>();
		for (int i = 0; i < stepDetailList.size(); i++) {
			PlottingDetail plottingDetail = new PlottingDetail();
			plottingDetail.setPlotting(plotting);
			plottingDetail.setStep(stepDetailList.get(i).getStep());
			if (i == 0) {
				plottingDetail.setTgl(dto.getTgl());
			} else {
				plottingDetail.setTgl(null);
			}
			plottingDetail.setTempResult(null);
			plottingDetail.setTempKeterangan(null);
			Date currentDate = new Date(System.currentTimeMillis());
			plottingDetail.setCreatedOn(currentDate);
			plottingDetailRepository.save(plottingDetail);
			plottingDetailList.add(plottingDetail);
		}
		List<Object> returnVal = new ArrayList<>();
		returnVal.add(plotting);
		plottingDetailList.stream().forEach(plotDet -> returnVal.add(plotDet));
		return returnVal;
	}

	@Override
	public PlottingDetail updatePlottingDetail(String pltdetid, UpdatePlottingDetailDto dto) {
		PlottingDetail plottingDetail = plottingDetailRepository.findById(pltdetid).get();
		String pltid = plottingDetail.getPlotting().getIdPlotting();
		List<PlottingDetail> listPltDet = plottingDetailRepository.findPlotDetailIdByPltId(pltid);
		
		if (dto.getResult().equals("NO")) {
			System.out.println("masuk sini");
			for (int i = 0; i < listPltDet.size(); i++) {
				PlottingDetail pltdet = plottingDetailRepository.findPlotDetailIdByPltId(pltid).get(i);
				pltdet.setTempResult(dto.getResult());
				pltdet.setTempKeterangan(dto.getKeterangan());
				pltdet.setTgl(dto.getTanggal());
				plottingDetailRepository.save(pltdet);
			}
		}
		plottingDetail.setTempResult(dto.getResult());
		plottingDetail.setTempKeterangan(dto.getKeterangan());
		plottingDetail.setTgl(dto.getTanggal());
		plottingDetailRepository.save(plottingDetail);
		return plottingDetail;
	}

}
