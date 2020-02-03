package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks // Instantiates the object by passing in the mocks into the constructor
    SpecialitySDJpaService service;

    @Test
    void testDeleteById_SingleInvocationSuccess() {
        service.deleteById(1L);

        verify(specialtyRepository).deleteById(1L);
    }

    @Test
    void testDeleteById_DoubleInvocationFailure() {
        service.deleteById(1L);
        service.deleteById(1L);

        verify(specialtyRepository).deleteById(1L);     // Mockito defaults to one call only.
                                                        // If you want more, you need to be explicit as shown below.
    }

    @Test
    void testDeleteById_DoubleInvocationSuccess() {
        service.deleteById(1L);
        service.deleteById(1L);

        verify(specialtyRepository, times(2)).deleteById(1L);
    }

    // Other ways to handle multiple mock verifications
    @Test
    void testDeleteById_AtLeastOnce() {
        service.deleteById(1L);
        service.deleteById(1L);

        verify(specialtyRepository, atLeastOnce()).deleteById(1L);
    }

    @Test
    void testDeleteById_atMostFiveTimes() {
        service.deleteById(1L);
        service.deleteById(1L);

        verify(specialtyRepository, atMost(5)).deleteById(1L);
    }

    @Test
    void testDeleteById_Never() {
        service.deleteById(1L);
        service.deleteById(1L);

        verify(specialtyRepository, never()).deleteById(5L);    // Verify it doesn't ever get called with given argument
    }

    @Test
    void testDelete() {
        service.delete(new Speciality());
    }
}