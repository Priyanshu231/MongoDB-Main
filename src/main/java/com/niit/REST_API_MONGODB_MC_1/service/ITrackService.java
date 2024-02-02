package com.niit.REST_API_MONGODB_MC_1.service;

import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackAlreadyExistException;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackNotFoundException;

import java.util.List;

public interface ITrackService {
    Track saveTrackObject(Track track) throws TrackAlreadyExistException;
    boolean deleteTrack(int trackId) throws TrackNotFoundException;
    List<Track> getAllTrackDetails() throws Exception;
    List<Track> getAllTrackDetailsHaveRatingGreaterThen4(int trackRating) throws TrackNotFoundException;
    List<Track> getAllTrackDetailsHaveArtistJustinBieber(String artistName) throws TrackNotFoundException;
}
