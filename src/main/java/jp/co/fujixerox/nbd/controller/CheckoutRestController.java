package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.controller.form.CreateDeviceForm;
import jp.co.fujixerox.nbd.domain.model.CheckOut;
import jp.co.fujixerox.nbd.domain.model.Device;
import jp.co.fujixerox.nbd.domain.model.Log;
import jp.co.fujixerox.nbd.domain.repository.CheckOutRepository;
import jp.co.fujixerox.nbd.domain.repository.LogRepository;
import jp.co.fujixerox.nbd.domain.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutRestController {

    @Autowired
    CheckOutRepository coRepo;

    @Autowired
    LogRepository logRepo;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getCurrentCheckout(){
        List<CheckOut> checkouts = coRepo.findAll();

        Map<String, List> response = new HashMap<>();
        response.put("checkouts", checkouts);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/log",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getLog(){
        List<Log> logs = logRepo.findAll();

        Map<String, List> response = new HashMap<>();
        response.put("logs", logs);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}