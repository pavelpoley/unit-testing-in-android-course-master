package com.techyourchance.testdrivendevelopment.exercise8.networking;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

    private final GetContactsHttpEndpoint mHttpEndPoint;
    private List<Listener> mListenerList = new ArrayList<>();

    public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
        this.mHttpEndPoint = getContactsHttpEndpoint;
    }

    public void fetchContacts(String filterTerm) {

        mHttpEndPoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> cartItems) {
                for (Listener listener : mListenerList) {
                    listener.onSuccess(convertSchemaToContact(cartItems));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {

                if (failReason == GetContactsHttpEndpoint.FailReason.GENERAL_ERROR) {
                    for (Listener listener : mListenerList) {
                        listener.onGeneralError();
                    }
                } else if (failReason == GetContactsHttpEndpoint.FailReason.NETWORK_ERROR) {
                    for (Listener listener : mListenerList) {
                        listener.onNetworkError();
                    }
                }


            }
        });
    }

    private List<Contact> convertSchemaToContact(List<ContactSchema> cartItems) {

        List<Contact> contacts = new ArrayList<>();

        for (ContactSchema cs : cartItems) {
            contacts.add(new Contact(cs.getId(), cs.getFullName(), cs.getImageUrl()));
        }

        return contacts;
    }

    public void registerListeners(Listener listener) {
        mListenerList.add(listener);
    }

    public void removeListener(Listener listener) {
        mListenerList.remove(listener);
    }

    public interface Listener {
        void onGeneralError();

        void onNetworkError();

        void onSuccess(List<Contact> contact);
    }
}
