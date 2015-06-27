package com.example.android.thepomoappandroid.api.services;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.LinkPeopleDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.interfaces.GroupsInterface;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;

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

    public void findById(String token, int id, Callback<GroupDTO> callback) {
        setAuthInterceptor(token);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.findById(id, callback);
    }

    public void create(String token, String name, String description, Callback<GroupDTO> callback) {
        setAuthInterceptor(token);
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(name, description);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.create(createGroupRequest, callback);
    }

    public void delete(String token, int id, Callback<ResponseCallback> callback) {
        setAuthInterceptor(token);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.delete(id, callback);
    }

    public void update(String token, int id, String name, String description, Callback<GroupDTO> callback) {
        setAuthInterceptor(token);
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(name, description);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.update(id, createGroupRequest, callback);
    }

    public void getSessions(String token, int id, Callback<List<SessionDTO>> callback) {
        setAuthInterceptor(token);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.getSessions(id, callback);
    }

    public void linkPeople(String token, int groupId, int userToLinkId, Callback<LinkPeopleDTO> callback) {
        setAuthInterceptor(token);
        LinkPeopleDTO linkPeopleDTO = new LinkPeopleDTO(groupId, userToLinkId);
        GroupsInterface groupsInterface = restAdapter.create(GroupsInterface.class);
        groupsInterface.linkPeople(groupId,userToLinkId, linkPeopleDTO, callback);
    }
}
