package com.example.android.thepomoappandroid;

/**
 * Created by hanric on 24/10/15.
 */
public class SessionStartBusEvent {
    private int groupId;

    public SessionStartBusEvent(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
