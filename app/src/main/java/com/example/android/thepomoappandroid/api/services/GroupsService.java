package com.example.android.thepomoappandroid.api.services;

import android.content.Context;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.interfaces.GroupsInterface;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;

import java.util.List;

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
        void onCreateGroup(GroupDTO groupDTO);
    }

    public interface OnDeleteGroup {
        void onDeleteGroup();
    }

    public interface OnUpdateGroup {
        void onUpdateGroup();
    }

    public interface OnGetSessions {
        void onGetSessions(List<SessionDTO> sessionDTOs);
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
                if (onCreateGroup != null) onCreateGroup.onCreateGroup(groupDTO);
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
        groupsInterface.delete(id, new Callback<ResponseCallback>() {
            @Override
            public void success(ResponseCallback responseCallback, Response response) {
                if (onDeleteGroup != null) onDeleteGroup.onDeleteGroup();
            }

            @Override
            public void failure(RetrofitError error) {
                if (onDeleteGroup != null) ((OnRetrofitError) onDeleteGroup).onError(error);
            }
        });
    }

    public void update(String token, int id, String name, String description, final OnUpdateGroup onUpdateGroup) {
        setAuthInterceptor(token);
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(name, description);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.update(id, createGroupRequest, new Callback<GroupDTO>() {
            @Override
            public void success(GroupDTO groupDTO, Response response) {
                if (onUpdateGroup != null) onUpdateGroup.onUpdateGroup();
            }

            @Override
            public void failure(RetrofitError error) {
                if (onUpdateGroup != null) ((OnRetrofitError) onUpdateGroup).onError(error);
            }
        });
    }

    public void getSessions(String token, int id, final OnGetSessions onGetSessions) {
        setAuthInterceptor(token);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.getSessions(id, new Callback<List<SessionDTO>>() {
            @Override
            public void success(List<SessionDTO> sessionDTOs, Response response) {
                if (onGetSessions != null) onGetSessions.onGetSessions(sessionDTOs);
            }

            @Override
            public void failure(RetrofitError error) {
                if (onGetSessions != null) ((OnRetrofitError) onGetSessions).onError(error);
            }
        });
    }
}
