package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.fauxspring.ModelMapImpl;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.Binding;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_63 = "redirect:/owners/63";

    @Mock
    OwnerService service;

    @Mock
    BindingResult result;

    @Mock
    Model model;

    @InjectMocks
    OwnerController controller;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation -> {
                    List<Owner> ownerList = new ArrayList<>();

                    String name = invocation.getArgument(0);

                    if (name.equals("%holmes%")) {
                        ownerList.add(new Owner(11L, "katie", "holmes"));
                        return ownerList;
                    } else if (name.equals("%DontFindMe%")) {
                        return ownerList;
                    } else if (name.equals("%murphy%")) {
                        ownerList.add(new Owner(11L, "katie", "holmes"));
                        ownerList.add(new Owner(14L, "cillian", "murphy"));
                        return ownerList;
                    }

                    throw new RuntimeException("Invalid Argument");
                });
    }

    @Test
    void processFindFormWildCard_notFound() {
        //given
        Owner owner = new Owner(11L, "katie", "DontFindMe");

        //when
        String view = controller.processFindForm(owner, result, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(view);
    }

    @Test
    void processFindFormWildCard_SingleOwner() {
        //given
        Owner owner = new Owner(11L, "katie", "holmes");
        List<Owner> owners = new ArrayList<>();

        //when
        String view = controller.processFindForm(owner, result, null);

        //then
        assertThat("%holmes%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/11").isEqualToIgnoringCase(view);
    }

    @Test
    void processFindFormWildCard_multipleOwners() {
        //given
        Owner owner = new Owner(14L, "cillian", "murphy");
        InOrder inOrder = inOrder(service, model);

        //when
        String view = controller.processFindForm(owner, result, model);

        //then
        assertThat("owners/ownersList").isEqualToIgnoringCase(view);
        assertThat("%murphy%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());

        // inOrder asserts
        inOrder.verify(service).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
    }

}
