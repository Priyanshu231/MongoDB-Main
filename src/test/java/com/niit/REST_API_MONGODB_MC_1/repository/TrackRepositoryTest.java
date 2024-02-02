package com.niit.REST_API_MONGODB_MC_1.repository;

import com.niit.REST_API_MONGODB_MC_1.domain.Artist;
import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackNotFoundException;
import com.niit.REST_API_MONGODB_MC_1.service.TrackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TrackRepositoryTest {
    @Autowired
    private TrackRepository trackRepository;
    private Artist artist;
    private Track track;
    @BeforeEach
    void setUp() {
        artist = new Artist(1,"John");
        track = new Track(1,"abc",5,artist);
    }
    @AfterEach
    void tearDown() {
        artist = null;
        track = null;
        trackRepository.deleteAll();
    }
    @Test
    @DisplayName("Test Case for saving track")
    void givenTrackToSaveShouldReturnTrack() {
        trackRepository.save(track);
        Track track1 = trackRepository.findById(track.getTrackId()).get();
        assertNotNull(track1);
        assertEquals(track.getTrackId(),track1.getTrackId());
    }
    @Test
    @DisplayName("Test Case for deleting track")
    void givenTrackToDeleteShouldDeleteTrack() {
        trackRepository.insert(track);
        Track track1 = trackRepository.findById(track.getTrackId()).get();
        trackRepository.delete(track1);
        assertEquals(Optional.empty(),trackRepository.findById(track.getTrackId()));
    }
    @Test
    @DisplayName("Test Case for retrieving all the track")
    void givenTrackReturnAllTrack() {
        trackRepository.insert(track);
        Artist artist1 = new Artist(2,"DEF");
        Track track1 = new Track(2,"GHI",4,artist1);
        trackRepository.insert(track1);
        List<Track> list = trackRepository.findAll();
        assertEquals(2,list.size());
        assertEquals("GHI",list.get(1).getTrackName());
    }
    @Test
    @DisplayName("Test case for deleting non-existent customer")
    public void givenNonExistentTrackToDeleteShouldFail() {
        Track nonExistentCustomer = new Track(1,"ABC",5,new Artist(1,"DEF"));
        trackRepository.delete(nonExistentCustomer);
        assertEquals(Optional.empty(),trackRepository.findById(nonExistentCustomer.getTrackId()));
    }
    @Test
    @DisplayName("Test case for getAllTracks have rating greater then 4 Success")
    public void TestGetAllTracksHavingRatingGreaterThen4Success() {
        Artist artist1 = new Artist(1,"ABC");
        Track track1 = new Track(1,"Track1",4,artist1);
        Artist artist2 = new Artist(2,"DEF");
        Track track2 = new Track(2,"Track2",5,artist2);
        trackRepository.save(track1);
        trackRepository.save(track2);
        List<Track> tracks = trackRepository.findAllTrackDetailsHaveRatingGreaterThen4(4);
        assertEquals(1,tracks.size());
    }
    @Test
    @DisplayName("Test case for getAllTracks have rating greater then 4 Failure")
    public void TestGetAllTracksHavingRatingGreaterThen4NoMatch() {
        Artist artist1 = new Artist(1,"ABC");
        Track track1 = new Track(1,"Track1",3,artist1);
        Artist artist2 = new Artist(2,"DEF");
        Track track2 = new Track(2,"Track2",2,artist2);
        trackRepository.save(track1);
        trackRepository.save(track2);
        List<Track> tracks = trackRepository.findAllTrackDetailsHaveRatingGreaterThen4(4);
        assertEquals(0,tracks.size());
    }
    @Test
    @DisplayName("Test case for getAllTracks have artist as Justin")
    public void TestCaseForGetTrackHavingArtistAsJustin() {
        String artistName = "Justin Bieber";
        Artist artist1 = new Artist(1,artistName);
        Track track1 = new Track(1,"Track1",3,artist1);
        Artist artist2 = new Artist(2,artistName);
        Track track2 = new Track(2,"Track2",4,artist2);
        trackRepository.save(track1);
        trackRepository.save(track2);
        List<Track> result = trackRepository.findAllTrackDetailsHaveArtistJustinBieber(artistName);
        assertEquals(2,result.size());
    }
    @Test
    @DisplayName("Test case for getAllTracks have artist as Justin NoMatch")
    public void TestCaseForGetTrackHavingArtistAsJustinNoMatch() {
        String artistName = "Justin Bieber";
        Artist artist1 = new Artist(1,"ABC");
        Track track1 = new Track(1,"Track1",3,artist1);
        Artist artist2 = new Artist(2,"ABCD");
        Track track2 = new Track(2,"Track2",4,artist2);
        trackRepository.save(track1);
        trackRepository.save(track2);
        List<Track> result = trackRepository.findAllTrackDetailsHaveArtistJustinBieber(artistName);
        assertEquals(0,result.size());
    }
}
