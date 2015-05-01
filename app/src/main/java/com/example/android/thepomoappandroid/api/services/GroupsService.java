package com.example.android.thepomoappandroid.api.services;

import android.content.Context;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.interfaces.GroupsInterface;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    public interface OnCreateGroup {
        void onCreate(GroupDTO groupDTO);
    }

    public interface OnDeleteGroup {
        void onDelete();
    }

    public void findById(Context context, int id, Callback<GroupDTO> callback) {
//        setAuthInterceptor(context);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.findById(id, callback);
    }

    public void create(String token, String name, String description, final OnCreateGroup onCreateGroup) {
        setAuthInterceptor(token);
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(name, description);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.create(createGroupRequest, new Callback<GroupDTO>() {
            @Override
            public void success(GroupDTO groupDTO, Response response) {
                if (onCreateGroup != null) onCreateGroup.onCreate(groupDTO);
            }

            @Override
            public void failure(RetrofitError error) {
                if (onCreateGroup != null) ((OnRetrofitError) onCreateGroup).onError(error);
            }
        });
    }

    public void delete(String token, int id, final OnDeleteGroup onDeleteGroup) {
        setAuthInterceptor(token);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.delete(id, new ResponseCallback() {
            @Override
            public void success(Response response) {
                if (onDeleteGroup != null) onDeleteGroup.onDelete();
            }

            @Override
            public void failure(RetrofitError error) {
                if (onDeleteGroup != null) ((OnRetrofitError) onDeleteGroup).onError(error);
            }
        });
    }
}
