package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.controller.form.CreateDeviceForm;
import jp.co.fujixerox.nbd.exception.ConflictException;
import jp.co.fujixerox.nbd.persistence.entity.DeviceEntity;
import jp.co.fujixerox.nbd.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
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
     *
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAll(@RequestParam(name = "query", required = false) String query) {
        List<DeviceEntity> deviceEntities = deviceService.getAll(query);

        Map<String, List> response = new HashMap<>();
        response.put("devices", deviceEntities);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * IDに対応した端末情報を取得する
     *
     * @param id
     * @return
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getOne(
            @PathVariable("id") String id) {
        DeviceEntity deviceEntity;

        try {
            deviceEntity = deviceService.getById(id);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(deviceEntity);
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
            UriComponentsBuilder uriBuilder) {

        /* bean validation */
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            deviceService.register(deviceForm.getId(), deviceForm.getMacAddress(), deviceForm.getName());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        URI location = uriBuilder.path("api/users/{deviceId}")
                .buildAndExpand(deviceForm.getId())
                .toUri();
        return ResponseEntity.created(location).build();
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
    public ResponseEntity checkoutDevice(
            @PathVariable("deviceId") String deviceId,
            @RequestParam(name = "user_id") String userId,
            @RequestParam(name = "due_return_time") long dueReturnTime) {

        try {
            deviceService.checkout(userId, deviceId, dueReturnTime);
        } catch (ApplicationException e) {
            switch (e.getError()) {
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
     *
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
            @RequestParam(name = "user_id") String userId) {

        try {
            deviceService.returnDevice(userId, deviceId);
        } catch (ApplicationException e) {
            switch (e.getError()) {
                case ENTITY_NOT_FOUND:
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                case NOT_CHECKOUT:
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
