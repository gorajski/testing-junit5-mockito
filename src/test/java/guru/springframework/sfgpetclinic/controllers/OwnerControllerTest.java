package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.Binding;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_63 = "redirect:/owners/63";

    @Mock
    OwnerService service;

    @Mock
    BindingResult result;

    @InjectMocks
    OwnerController controller;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    void processFindFormWildCardString_LocalVarCaptor() {
        //given
        Owner owner = new Owner(1L, "katie", "holmes");
        List<Owner> owners = new ArrayList<>();
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(service.findAllByLastNameLike(captor.capture())).willReturn(owners);

        //when
        String view = controller.processFindForm(owner, result, null);

        //then
        assertThat("%holmes%").isEqualToIgnoringCase(captor.getValue());
    }

    @Test
    void processFindFormWildCardString_CaptorAnnotation() {
        //given
        Owner owner = new Owner(1L, "katie", "holmes");
        List<Owner> owners = new ArrayList<>();
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture())).willReturn(owners);

        //when
        String view = controller.processFindForm(owner, result, null);

        //then
        assertThat("%holmes%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
    }

    @Test
    void processCreationForm_handleErrors() {
        //given
        Owner owner = new Owner(1L, "gary", "oldman");
        given(result.hasErrors()).willReturn(true);

        //when
        String view = controller.processCreationForm(owner, result);

        //then
        assertThat(view).isEqualToIgnoringCase(VIEWS_OWNER_CREATE_OR_UPDATE_FORM);
        then(service).should(never()).save(any(Owner.class));
    }

    @Test
    void processCreationForm_savesOwner_andView() {
        //given
        Owner owner = new Owner(63L, "christian", "bale");
        given(result.hasErrors()).willReturn(false);
        given(service.save(any(Owner.class))).willReturn(owner);

        //when
        String view = controller.processCreationForm(owner, result);

        //then
        assertThat(view).isEqualToIgnoringCase(REDIRECT_OWNERS_63);
        then(service).should().save(any(Owner.class));
    }
}