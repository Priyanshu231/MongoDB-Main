package com.niit.REST_API_MONGODB_MC_1.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.REST_API_MONGODB_MC_1.controller.TrackController;
import com.niit.REST_API_MONGODB_MC_1.domain.Artist;
import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackAlreadyExistException;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackNotFoundException;
import com.niit.REST_API_MONGODB_MC_1.service.TrackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTest {
    private MockMvc mockMvc;
    @Mock
    private TrackServiceImpl trackService;
    @InjectMocks
    private TrackController trackController;
    Track track1,track2;
    private Artist artist1,artist2;

    @BeforeEach
    void setUp() {
        artist1=new Artist(1,"ABC");
        track1 = new Track(1,"Track1",4,artist1);
        artist2=new Artist(2,"DEF");
        track2 = new Track(2,"Track2",5,artist2);
        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(trackController);
        mockMvc=builder.build();
    }

    @AfterEach
    void tearDown() {
        track1=null;
        artist1=null;
        track2=null;
        artist2=null;
    }
    @Test
    void givenTrackToSaveReturnSavedTrackSuccess() throws Exception {
        when(trackService.saveTrackObject(any())).thenReturn(track1);
        mockMvc.perform(post("/api/v1/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(track1)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).saveTrackObject(any());
    }
    @Test
    void givenTrackToSaveReturnSavedTrackFail() throws Exception {
        when(trackService.saveTrackObject(any())).thenThrow(TrackAlreadyExistException.class);
        mockMvc.perform(post("/api/v1/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(track1)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).saveTrackObject(any());
    }
    @Test
    void givenTrackIdDeleteTrack() throws Exception {
        when(trackService.deleteTrack(anyInt())).thenReturn(true);
        mockMvc.perform(delete("/api/v1/track/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).deleteTrack(anyInt());
    }
    @Test
    void getAllTrackSavedTest() throws Exception {
        List<Track> trackList = new ArrayList<>();
        trackList.add(new Track(1,"Track1",4,new Artist(1,"ABD")));
        trackList.add(new Track(2,"Track2",5,new Artist(2,"DEF")));
        when(trackService.getAllTrackDetails()).thenReturn(trackList);
        mockMvc.perform(get("/api/v1/tracks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(trackList)))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).getAllTrackDetails();
    }
    @Test
    void testGetAllTrackDetailsHaveRatingGreaterThen4() throws Exception {
        Track track1=new Track(1,"Track1",4,new Artist(1,"ABC"));
        Track track2=new Track(2,"Track2",5,new Artist(2,"BCD"));
        when(trackService.getAllTrackDetailsHaveRatingGreaterThen4(4)).thenReturn(List.of(track1,track2));
        mockMvc.perform(get("/api/v1/track/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).getAllTrackDetailsHaveRatingGreaterThen4(4);
    }
    @Test
    void testGetAllTrackDetailsHaveArtistJustinBieber() throws Exception {
        Track track1 = new Track(1,"Track1",4,new Artist(1,"Justin Bieber"));
        Track track2 = new Track(2,"Track2",5,new Artist(2,"ABC"));
        when(trackService.getAllTrackDetailsHaveArtistJustinBieber("Justin Bieber")).thenReturn(List.of(track1));
        mockMvc.perform(get("/api/v1/tracks/Justin Bieber")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(track1)))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).getAllTrackDetailsHaveArtistJustinBieber("Justin Bieber");
    }
    @Test
    void testGetAllTrackDetailsHaveArtistJustinBieber_notExist() throws Exception {
        Track track1 = new Track(1,"Track1",4,new Artist(1,"DEF"));
        Track track2 = new Track(2,"Track2",5,new Artist(2,"ABC"));
        when(trackService.getAllTrackDetailsHaveArtistJustinBieber("NonExistentArtist")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/tracks/NonExistentArtist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).getAllTrackDetailsHaveArtistJustinBieber("NonExistentArtist");
    }
    private static String jsonToString(final Object o) throws JsonProcessingException {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(o);
            result = jsonContent;
        }
        catch (JsonProcessingException e) {
            result = "Json processing error occurred";
        }
        return result;
    }
}
