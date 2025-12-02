package com.prosigmaka.catra.diglett.service;

import java.util.List;

import com.prosigmaka.catra.diglett.model.dto.FormPlottingDto;
import com.prosigmaka.catra.diglett.model.dto.UpdatePlottingDetailDto;
import com.prosigmaka.catra.diglett.model.entity.PlottingDetail;

public interface PlottingService {
    List<Object> insertPlotting (FormPlottingDto dto);
    PlottingDetail updatePlottingDetail (String pltdetid, UpdatePlottingDetailDto dto);
}
