package com.niit.REST_API_MONGODB_MC_1.service;

import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackAlreadyExistException;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackNotFoundException;
import com.niit.REST_API_MONGODB_MC_1.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements ITrackService{
    private TrackRepository trackRepository;
    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track saveTrackObject(Track track) throws TrackAlreadyExistException {
        if (trackRepository.findById(track.getTrackId()).isPresent())
        {
            throw new RuntimeException();
        }
        return trackRepository.save(track);
    }

    @Override
    public boolean deleteTrack(int trackId) throws TrackNotFoundException {
        boolean flag = false;
        if (trackRepository.findById(trackId).isEmpty())
        {
            throw new TrackNotFoundException();
        }
        else {
            trackRepository.deleteById(trackId);
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Track> getAllTrackDetails() throws Exception {
        return trackRepository.findAll();
    }

    @Override
    public List<Track> getAllTrackDetailsHaveRatingGreaterThen4(int trackRating) throws TrackNotFoundException {
        if (trackRepository.findAllTrackDetailsHaveRatingGreaterThen4(trackRating).isEmpty())
        {
            throw new TrackNotFoundException();
        }
        return trackRepository.findAllTrackDetailsHaveRatingGreaterThen4(trackRating);
    }

    @Override
    public List<Track> getAllTrackDetailsHaveArtistJustinBieber(String artistName) throws TrackNotFoundException {
        if (trackRepository.findAllTrackDetailsHaveArtistJustinBieber(artistName).isEmpty())
        {
            throw new TrackNotFoundException();
        }
        return trackRepository.findAllTrackDetailsHaveArtistJustinBieber(artistName);
    }
}
