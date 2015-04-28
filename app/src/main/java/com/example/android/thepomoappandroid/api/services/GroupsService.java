package com.example.android.thepomoappandroid.api.services;

import android.content.Context;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.interfaces.GroupsInterface;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;

import retrofit.Callback;

/**
 * Created by Enric on 22/04/2015.
 */
public class GroupsService extends BaseService {

    private static GroupsService ourInstance = new GroupsService();

    public static GroupsService getInstance() {
        return ourInstance;
    }

    private GroupsService() {
    }

    public void findById(Context context, int id, Callback<GroupDTO> callback) {
//        setAuthInterceptor(context);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.findById(id, callback);
    }

    public void create(Context context, String name, String description, Callback<GroupDTO> callback) {
//        setAuthInterceptor(context);
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(name, description);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.create(createGroupRequest, callback);
    }
}
