package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.controller.form.CreateDeviceForm;
import jp.co.fujixerox.nbd.domain.model.Device;
import jp.co.fujixerox.nbd.domain.service.DeviceService;
import jp.co.fujixerox.nbd.exception.InvalidRequestException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceRestController {

    @Autowired
    DeviceService deviceService;

    /**
     * 端末情報をすべて取得する
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAll(){
        List<Device> devices = deviceService.getAll(0, 0);

        Map<String, List> response = new HashMap<>();
        response.put("devices", devices);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * IDに対応した端末情報を取得する
     * @param id
     * @return
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getOne(
            @PathVariable("id") String id){
        Device device = deviceService.getById(id);

        if(device == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    /**
     * 端末を登録する
     *
     * @param deviceForm
     * @param uriBuilder
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity register(
            @RequestBody @Valid CreateDeviceForm deviceForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder){

        /* bean validation */
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult.toString(), bindingResult);
        }

        try {
            deviceService.register(deviceForm.getId(), deviceForm.getName());
        } catch (ApplicationException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        URI location = uriBuilder.path("api/users/{deviceId}")
                .buildAndExpand(deviceForm.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    /**
     * CSVファイルを読み込んで端末情報を更新する
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(
            value = "upload.csv",
            method = RequestMethod.POST,
            consumes = "multipart/form-data")
    public ResponseEntity putWithCsvFile(@RequestParam("csv_file") MultipartFile multipartFile){

        if(multipartFile == null || multipartFile.isEmpty()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<Device> devices = new ArrayList();
        try {
            byte[] bytes = multipartFile.getBytes();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));


            CSVParser parse = CSVFormat.DEFAULT.withHeader().parse(bufferedReader);
            for (CSVRecord line : parse) {
                Device device = new Device();
                device.setId(line.get("id"));
                device.setName(line.get("name"));
                device.setCreated(System.currentTimeMillis());
                device.setDisabled(false);

                devices.add(device);
            }
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            deviceService.bulkUpdate(devices);
        } catch (ApplicationException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 端末を借り出す
     *
     * @param userId
     * @param deviceId
     * @param dueReturnTime
     * @return
     */
    @RequestMapping(
            value = "/{deviceId}/checkout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity checkout(
            @PathVariable("deviceId") String deviceId,
            @RequestParam(name = "user_id") String userId,
            @RequestParam(name = "due_return_time") long dueReturnTime){

        try {
            deviceService.checkout(userId, deviceId, dueReturnTime);
        } catch (ApplicationException e) {
            switch(e.getError()){
                case ENTITY_NOT_FOUND:
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                case CHECKOUT_CONFLICT:
                    return new ResponseEntity(HttpStatus.CONFLICT);
                case ILLEGAL_DATE:
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 端末を返却する
     * @param userId
     * @param deviceId
     * @return
     */
    @RequestMapping(
            value = "/{deviceId}/return",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity returnDevice(
            @PathVariable("deviceId") String deviceId,
            @RequestParam(name = "user_id") String userId){

        try {
            deviceService.returnDevice(userId, deviceId);
        } catch (ApplicationException e) {
            switch(e.getError()){
                case ENTITY_NOT_FOUND:
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                case NOT_CHECKOUT:
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
