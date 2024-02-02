package com.niit.REST_API_MONGODB_MC_1.controller;

import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackAlreadyExistException;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackNotFoundException;
import com.niit.REST_API_MONGODB_MC_1.service.ITrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TrackController {
    private ResponseEntity responseEntity;
    private ITrackService trackService;
    @Autowired
    public TrackController(ITrackService trackService) {
        this.trackService = trackService;
    }
//    working
    @PostMapping("track")
    public ResponseEntity saveTrack(@RequestBody Track track) throws TrackAlreadyExistException {
        try {
            trackService.saveTrackObject(track);
            responseEntity = new ResponseEntity(track, HttpStatus.CREATED);
        }catch (TrackAlreadyExistException exception) {
            throw new TrackAlreadyExistException();
        }catch (Exception exception) {
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
//    working
    @DeleteMapping("track/{trackId}")
    public ResponseEntity deleteTrack(@PathVariable("trackId") int trackId) throws TrackNotFoundException {
        try {
            trackService.deleteTrack(trackId);
            responseEntity = new ResponseEntity("Successfully Deleted",HttpStatus.OK);
        }catch (TrackNotFoundException exception) {
            throw new TrackNotFoundException();
        }catch (Exception exception) {
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
//    working
    @GetMapping("tracks")
    public ResponseEntity getAllTrack() {
        try {
            responseEntity = new ResponseEntity(trackService.getAllTrackDetails(),HttpStatus.OK);
        }catch (Exception exception) {
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
//    working
    @GetMapping("track/{trackRating}")
    public ResponseEntity getAllTrackDetailsHaveRatingGreaterThen4(@PathVariable int trackRating) {
        try {
            responseEntity = new ResponseEntity(trackService.getAllTrackDetailsHaveRatingGreaterThen4(trackRating),HttpStatus.OK);
        }catch (Exception exception) {
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
//    working
    @GetMapping("tracks/{artistName}")
    public ResponseEntity getAllTrackDetailsHaveArtistJustinBieber(@PathVariable String artistName) {
        try {
            responseEntity = new ResponseEntity(trackService.getAllTrackDetailsHaveArtistJustinBieber(artistName),HttpStatus.OK);
        }catch (Exception exception) {
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
