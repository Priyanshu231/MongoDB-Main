package com.niit.REST_API_MONGODB_MC_1.repository;

import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TrackRepository extends MongoRepository<Track,Integer> {
    @Query("{'trackRating' : {$in : [?0]}}")
    List<Track> findAllTrackDetailsHaveRatingGreaterThen4(int trackRating);
    @Query("{'trackArtist.artistName' : {$in : [?0]}}")
    List<Track> findAllTrackDetailsHaveArtistJustinBieber(String artistName);
}