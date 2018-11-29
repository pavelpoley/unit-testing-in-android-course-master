package com.techyourchance.testdrivendevelopment.exercise8;


import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.FetchContactsUseCase;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {

    public static final String TERMS = "terms";
    public static final String ID = "3453262";
    public static final String NAME = "a";
    public static final String IMAGE_URL = "111";
    public static final int AGE = 18;
    public static final String FULL_PHONE_NUMBER = "111";


    @Mock
    GetContactsHttpEndpoint getContactsHttpEndpoint;

    private FetchContactsUseCase sut;

    @Mock
    private FetchContactsUseCase.Listener mListener1;
    @Mock
    private FetchContactsUseCase.Listener mListener2;

    @Captor
    ArgumentCaptor<List<Contact>> argCaptCallback;

    @Before
    public void setUp() throws Exception {
        sut = new FetchContactsUseCase(getContactsHttpEndpoint);
        //success();
    }


    @Test
    public void fetch_correctValuesPassedToEndPoint() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        sut.fetchContacts(TERMS);

        verify(getContactsHttpEndpoint).getContacts(argumentCaptor.capture()
                , any(GetContactsHttpEndpoint.Callback.class));

        List<String> values = argumentCaptor.getAllValues();

        assertThat(values.get(0), CoreMatchers.is(TERMS));
    }

    @Test
    public void generalError_notifyListeners() {
        generalError();

        sut.registerListeners(mListener1);
        sut.registerListeners(mListener2);

        sut.fetchContacts(TERMS);
        verify(mListener1).onGeneralError();
        verify(mListener2).onGeneralError();
    }

    @Test
    public void generalError_NotNotifyUnsubscribedListeners() {
        generalError();

        sut.registerListeners(mListener1);
        sut.registerListeners(mListener2);
        sut.removeListener(mListener2);

        sut.fetchContacts(TERMS);

        verify(mListener1).onGeneralError();
        verifyNoMoreInteractions(mListener2);
    }


    @Test
    public void networkError_notifyListeners() {
        networkError();

        sut.registerListeners(mListener1);
        sut.registerListeners(mListener2);

        sut.fetchContacts(TERMS);
        verify(mListener1).onNetworkError();
        verify(mListener2).onNetworkError();
    }

    @Test
    public void networkError_NotNotifyUnsubscribedListeners() {
        networkError();

        sut.registerListeners(mListener1);
        sut.registerListeners(mListener2);
        sut.removeListener(mListener2);

        sut.fetchContacts(TERMS);

        verify(mListener1).onNetworkError();
        verifyNoMoreInteractions(mListener2);
    }

    @Test
    public void success_returnContactList() {
        success();

        sut.registerListeners(mListener1);
        sut.registerListeners(mListener2);

        sut.fetchContacts(TERMS);

        verify(mListener1).onSuccess(argCaptCallback.capture());
        verify(mListener2).onSuccess(argCaptCallback.capture());

        List<List<Contact>> allValues = argCaptCallback.getAllValues();
        List<Contact> contacts = allValues.get(0);
        List<Contact> contacts2 = allValues.get(1);

        assertThat(contacts, CoreMatchers.is(getContactList()));
        assertThat(contacts2, CoreMatchers.is(getContactList()));

    }


    // Helper methods -------------------------------------------------------------------------


    private void success() {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] arguments = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) arguments[1];
                callback.onGetContactsSucceeded(getFakeContactSchemaList());

                return null;
            }
        }).when(getContactsHttpEndpoint).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));

    }

    private List<Contact> getContactList() {

        List<Contact> contactSchemaList = new ArrayList<>();

        contactSchemaList.add(new Contact(ID, NAME, IMAGE_URL));

        return contactSchemaList;
    }


    private List<ContactSchema> getFakeContactSchemaList() {

        List<ContactSchema> contactSchemaList = new ArrayList<>();

        contactSchemaList.add(new ContactSchema(ID, NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));

        return contactSchemaList;
    }



    private void networkError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] arguments = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) arguments[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);

                return null;
            }
        }).when(getContactsHttpEndpoint).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }


    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] arguments = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) arguments[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);

                return null;
            }
        }).when(getContactsHttpEndpoint).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

}