package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    private Set<Visit> storedVisits;

    @Mock
    private VisitRepository repository;

    @InjectMocks
    private VisitSDJpaService service;

    @BeforeEach
    void setUp() {
        storedVisits = new HashSet<Visit>();
        storedVisits.add(new Visit(1L));
        storedVisits.add(new Visit(6L));
        storedVisits.add(new Visit(35L));
    }

    @Test
    void findAll() {
        //given - none
        given(repository.findAll()).willReturn(storedVisits);

        //when
        Set<Visit> visits = service.findAll();

        //then
        assertThat(visits.size()).isEqualTo(3);
        assertThat(visits.containsAll(storedVisits)).isTrue();
        then(repository).should().findAll();
    }

    @Test
    void findById() {
        // given
        given(repository.findById(any())).willReturn(Optional.of(new Visit(6L)));

        // when
        Visit visit = service.findById(6L);

        // then
        assertThat(visit.getId()).isEqualTo(6L);
        then(repository).should().findById(any());
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit(2L);
        given(repository.save(any(Visit.class))).willReturn(visit);

        //when
        Visit savedVisit = service.save(visit);

        //then
        then(repository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        //given
        Visit visit = new Visit(16L);

        //when
        service.delete(visit);

        //then
        then(repository).should().delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        //given
        Visit visit = new Visit(32L);

        //when
        service.deleteById(32L);

        //then
        then(repository).should().deleteById(anyLong());
    }

}