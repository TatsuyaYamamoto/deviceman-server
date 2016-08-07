package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.controller.form.CreateDeviceForm;
import jp.co.fujixerox.nbd.domain.model.Device;
import jp.co.fujixerox.nbd.domain.model.User;
import jp.co.fujixerox.nbd.domain.service.DeviceService;
import jp.co.fujixerox.nbd.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jp.co.fujixerox.nbd.HttpError.ENTITY_NOT_FOUND;
import static jp.co.fujixerox.nbd.HttpError.ILLEGAL_DATE;

@RestController
@RequestMapping("/api/devices")
public class DeviceRestController {

    @Autowired
    DeviceService deviceService;

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

    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity register(
            @RequestBody
            @Valid
                    CreateDeviceForm deviceForm,
            UriComponentsBuilder uriBuilder){

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
     * 既に貸出中のcheckoutリソースを更新する
     *
     * @param userId
     * @param deviceId
     * @param dueReturnTime
     * @return
     */
    @RequestMapping(
            value = "/{deviceId}/checkout",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateCheckout(
            @PathVariable("deviceId") String deviceId,
            @RequestParam(name = "user_id") String userId,
            @RequestParam(name = "due_return_time") long dueReturnTime){

        try {
            deviceService.updateCheckout(userId, deviceId, dueReturnTime);
        } catch (ApplicationException e) {
            switch(e.getError()){
                case ENTITY_NOT_FOUND:
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                case NOT_CHECKOUT:
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
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
