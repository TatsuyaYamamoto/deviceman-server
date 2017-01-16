package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.persistence.entity.CheckOutEntity;
import jp.co.fujixerox.nbd.persistence.entity.CheckOutLogEntity;
import jp.co.fujixerox.nbd.persistence.repository.CheckOutRepository;
import jp.co.fujixerox.nbd.persistence.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 現在の貸出情報を取得する
     * 対象は貸出中の端末のみ
     *
     * @return
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getCurrentCheckout(){
        List<CheckOutEntity> checkouts = coRepo.findAll();

        Map<String, List> response = new HashMap<>();
        response.put("checkouts", checkouts);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 貸出履歴を取得する
     * 対象は返却済みの端末のみ
     *
     * @return
     */
    @RequestMapping(
            value = "/log",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getLog(){
        List<CheckOutLogEntity> logEntities = logRepo.findAll();

        Map<String, List> response = new HashMap<>();
        response.put("logs", logEntities);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}