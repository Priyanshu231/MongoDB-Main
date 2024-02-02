import com.niit.REST_API_MONGODB_MC_1.domain.Artist;
import com.niit.REST_API_MONGODB_MC_1.domain.Track;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackAlreadyExistException;
import com.niit.REST_API_MONGODB_MC_1.exception.TrackNotFoundException;
import com.niit.REST_API_MONGODB_MC_1.repository.TrackRepository;
import com.niit.REST_API_MONGODB_MC_1.service.TrackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class TrackServiceTest {
    @Mock
    private TrackRepository trackRepository;
    @InjectMocks
    private TrackServiceImpl trackService;
    private Track track1,track2;
    List<Track> trackList;
    Artist artist1,artist2;
    @BeforeEach
    void setUp() {
        artist1 = new Artist(1,"Arijit");
        track1 = new Track(1,"vande matram",5,artist1);
        artist2 = new Artist(2,"JB");
        track2 = new Track(2,"never say never",4,artist2);
    }
    @AfterEach
    void tearDown() {
        track1 = null;
        track2 = null;
    }
    @Test
    public void givenTrackToSaveReturnTrack() throws TrackAlreadyExistException {
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(null));
        when(trackRepository.save(any())).thenReturn(track1);
        assertEquals(track1,trackService.saveTrackObject(track1));
        verify(trackRepository,times(1)).save(any());
        verify(trackRepository,times(1)).findById(any());
    }
    @Test
    public void givenTrackToDelete() throws TrackNotFoundException {
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(track1));
        boolean flag = trackService.deleteTrack(track1.getTrackId());
        assertTrue(flag);
        verify(trackRepository,times(1)).deleteById(any());
        verify(trackRepository,times(1)).findById(any());
    }
    @Test
    public void givenValidTrackIdToDeleteTrack() throws TrackNotFoundException {
        // Mocking repository behavior to return a track with the given ID
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.of(track1));

        boolean flag = trackService.deleteTrack(track1.getTrackId());

        // Assertions
        assertTrue(flag);

        // Verifying repository method invocations
        verify(trackRepository, times(1)).deleteById(any());
        verify(trackRepository, times(1)).findById(any());

    }
    @Test
    public void givenNonExistentTrackIdToDeleteTrackThrowException() {
        // Mocking repository behavior to return no track with the given ID
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(TrackNotFoundException.class, () -> {
            trackService.deleteTrack(track1.getTrackId());
        });

        // Verifying repository method invocations
        verify(trackRepository, times(0)).deleteById(any());
        verify(trackRepository, times(1)).findById(any());
    }
    @Test
    public void givenNoTracksWithRatingGreaterThan4ThrowException() {
        // Mocking repository behavior to return an empty list of tracks
        when(trackRepository.findAllTrackDetailsHaveRatingGreaterThen4(5)).thenReturn(new ArrayList<>());

        // Assertions
        assertThrows(TrackNotFoundException.class, () -> {
            trackService.getAllTrackDetailsHaveRatingGreaterThen4(5);
        });

        // Verifying repository method invocations
        verify(trackRepository, times(1)).findAllTrackDetailsHaveRatingGreaterThen4(5);
    }
    @Test
    public void testGetAllTrackDetailsHaveArtistJustinBieber_Success() throws TrackNotFoundException{
        // Arrange
        String artistName = "Justin Bieber";
        Artist artist1 = new Artist(1,"ABC");
        Track track1 = new Track(1,"Track1",3,artist1);
        Artist artist2 = new Artist(2,"ABCD");
        Track track2 = new Track(2,"Track2",4,artist2);
        List<Track> mockTracks = new ArrayList<>();
        // Add some mock Track objects with the artist name "Justin Bieber"
        mockTracks.add(track1);
        mockTracks.add(track2);

        // Define the behavior of the repository when called by the service
        when(trackRepository.findAllTrackDetailsHaveArtistJustinBieber(artistName)).thenReturn(mockTracks);

        // Act
       List<Track> result = trackService.getAllTrackDetailsHaveArtistJustinBieber("Justin Bieber");

        // Assert
        assertEquals(2, result.size()); // Check the number of tracks in the result
    }
}