package com.techyourchance.testdrivendevelopment.exercise8.contacts;

public class Contact {

    private final String mId;
    private final String mFullName;
    private final String mImageUrl;

    public Contact(String id, String fullName, String imageUrl) {
        mId = id;
        mFullName = fullName;
        mImageUrl = imageUrl;
    }

    public String getId() {
        return mId;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (mId != null ? !mId.equals(contact.mId) : contact.mId != null) return false;
        if (mFullName != null ? !mFullName.equals(contact.mFullName) : contact.mFullName != null)
            return false;
        return mImageUrl != null ? mImageUrl.equals(contact.mImageUrl) : contact.mImageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mFullName != null ? mFullName.hashCode() : 0);
        result = 31 * result + (mImageUrl != null ? mImageUrl.hashCode() : 0);
        return result;
    }
}
