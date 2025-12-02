package com.prosigmaka.catra.diglett.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.FormPlottingDto;
import com.prosigmaka.catra.diglett.model.dto.UpdatePlottingDetailDto;
import com.prosigmaka.catra.diglett.model.entity.Email;
import com.prosigmaka.catra.diglett.model.entity.Plotting;
import com.prosigmaka.catra.diglett.model.entity.PlottingDetail;
import com.prosigmaka.catra.diglett.model.entity.Response;
import com.prosigmaka.catra.diglett.model.projection.PlottingDetailProjection;
import com.prosigmaka.catra.diglett.repository.PlottingDetailRepository;
import com.prosigmaka.catra.diglett.service.PlottingService;

import reactor.core.publisher.Mono;

@RestController
public class PlottingController {

    private final PlottingDetailRepository plottingDetailRepository;
    private final PlottingService service;
    private final Environment env;
    
    private static final String base_url = "http://localhost:1234/v1/pidgeon";
    private static final String uri = "/email/sendEmail";
    
	Logger logger = LoggerFactory.getLogger(PlottingController.class);

    public PlottingController(PlottingDetailRepository plottingDetailRepository, PlottingService service, Environment env) {
        this.plottingDetailRepository = plottingDetailRepository;
        this.service = service;
        this.env = env;
    }

    /* Insert Data */
    // http://localhost:1212/v1/diglett/plotting
    @PostMapping("/plotting")
    public DefaultResponse<List<Object>> insert(@RequestBody FormPlottingDto dto) {
        return DefaultResponse.ok(service.insertPlotting(dto));
    }
    
    @GetMapping("/plottingDetail")
    public DefaultResponse<List<PlottingDetail>> getData(){
    	List<PlottingDetail> plottingList = plottingDetailRepository.findAll();
    	return DefaultResponse.ok(plottingList);
    }

    // http://localhost:1212/v1/diglett/plotting-detail/PSM-001
    @GetMapping("/plotting-detail/{idCandidate}")
    public DefaultResponse<List<PlottingDetailProjection>> getPlottingDetailByCandidate(@PathVariable String idCandidate){
        List<PlottingDetailProjection> plottingDetailProjection = plottingDetailRepository.findPlottingDetailByCandidate(idCandidate);
        return DefaultResponse.ok(plottingDetailProjection);
    }
    
    // http://localhost:1212/v1/diglett/plotting-detail/last/PSM-001
    @GetMapping("/plotting-detail/last/{idCandidate}")
    public DefaultResponse<List<PlottingDetailProjection>> getLastPlottingDetailByCandidate(@PathVariable String idCandidate){
        List<PlottingDetailProjection> plottingDetailProjection = plottingDetailRepository.findLastPlottingDetailByCandidate(idCandidate);
        return DefaultResponse.ok(plottingDetailProjection);
    }

    // http://localhost:1212/v1/diglett/plotting-detail/PLTDET-001
    @PutMapping("/plotting-detail/{idPltDet}")
    public DefaultResponse<PlottingDetail> update(@PathVariable String idPltDet, @RequestBody UpdatePlottingDetailDto dto){
        return DefaultResponse.ok(service.updatePlottingDetail(idPltDet, dto));
    }
	@PostMapping("/sendEmail")
	public Response create(@RequestBody Email e, HttpServletRequest request) throws MalformedURLException
	{
		URL requestURL = new URL(request.getRequestURL().toString());
	    String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
	    logger.info(requestURL.getProtocol() + "://" + requestURL.getHost() + port + requestURL.getPath());
		String path = env.getProperty("pidgeon");
		WebClient webClient = WebClient.create(base_url);
		Response sendEmail = webClient.post().uri(uri)
				.body(Mono.just(e), Email.class)
				.retrieve()
				.bodyToMono(Response.class).block();
		logger.info(sendEmail.getNotification());
		return sendEmail;
	}
	@GetMapping("/getRequest")
	public Mono<ResponseEntity<Response>> getRequest() {
		Response response = new Response();
		try {
			response.setNotification("Your email is being processed.");
			logger.info("Your email is being processed.");
		} catch (MailSendException e) {
			response.setNotification(e.getMessage());
			logger.info(e.getMessage());
		}
		Response responseBody = response;
		return Mono.just(ResponseEntity.ok().body(responseBody));
	}
}
