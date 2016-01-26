package com.example.android.thepomoappandroid.ui.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.LinkPeopleDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.ui.adapter.UserAdapter;
import com.example.android.thepomoappandroid.ui.adapter.UserSearchAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 19/04/2015.
 */
public class AddGroupDialog extends DialogFragment
        implements Toolbar.OnMenuItemClickListener,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        TextWatcher,
        Validator.ValidationListener {

    public static final String CLASS_TAG = AddGroupDialog.class.getSimpleName();

    protected Context context;

    protected Toolbar toolbar;

    private Validator validator;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Length(sequence = 2, min = 3, max = 20, messageResId = R.string.groupNameError)
    protected EditText name;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Length(sequence = 2, min = 3, max = 20, messageResId = R.string.groupDescriptionError)
    protected EditText description;

    private AutoCompleteTextView searchText;
    private ListView selectedUsersListView;

    private UserAdapter selectedUsersAdapter;
    private UserSearchAdapter searchAdapter;

    protected OnActionGroupFromDialog onActionGroupFromDialog;

    private Callback<List<PersonDTO>> findByFilterCallback = new Callback<List<PersonDTO>>() {
        @Override
        public void success(List<PersonDTO> personDTOs, Response response) {
            Log.v(CLASS_TAG, "findByFilterCallback success");
            searchAdapter.clear();
            searchAdapter.addAll(personDTOs);
            searchAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.v(CLASS_TAG, "findByFilterCallback failure");
        }
    };

    private Callback<LinkPeopleDTO> linkPeopleCallback = new Callback<LinkPeopleDTO>() {
        @Override
        public void success(LinkPeopleDTO linkPeopleDTO, Response response) {
            Log.v(CLASS_TAG, "linkPeopleCallback success");
        }

        @Override
        public void failure(RetrofitError error) {
            Log.v(CLASS_TAG, "linkPeopleCallback success");
        }
    };

    public interface OnActionGroupFromDialog {
        void onActionGroupFromDialog();
    }

    public static AddGroupDialog newInstance(OnActionGroupFromDialog onActionGroupFromDialog) {
        AddGroupDialog dialogFragment = new AddGroupDialog();
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        dialogFragment.onActionGroupFromDialog = onActionGroupFromDialog;
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(TextInputLayout.class,
                new ViewDataAdapter<TextInputLayout, String>() {
                    @Override
                    public String getData(TextInputLayout flet) throws ConversionException {
                        return flet.getEditText().getText().toString();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_add_group, container,
                false);
        context = getActivity();
        findViews(view);
        setListeners();
        init();
        setupToolbar(R.string.toolbar_add_group);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    protected void findViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        name = (EditText) view.findViewById(R.id.popupAddGroup_name);
        description = (EditText) view.findViewById(R.id.popupAddGroup_description);
        searchText = (AutoCompleteTextView) view.findViewById(R.id.popupAddGroup_searchUser);
        selectedUsersListView = (ListView) view.findViewById(R.id.selectedUsers);
    }

    protected void setListeners() {
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        searchText.setOnItemClickListener(this);
        searchText.addTextChangedListener(this);
    }

    public void setupToolbar(int titleResId) {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle(titleResId);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_save);
    }

    private void init() {
        selectedUsersAdapter = new UserAdapter(getActivity(), 0, new ArrayList<PersonDTO>());
        searchAdapter = new UserSearchAdapter(getActivity(), 0, new ArrayList<PersonDTO>());
        searchText.setAdapter(searchAdapter);
        selectedUsersListView.setAdapter(selectedUsersAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == toolbar.getChildAt(0).getId()) {
            dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PersonDTO selected = searchAdapter.getItem(i);
        searchText.dismissDropDown();
        searchText.setText("");
        searchAdapter.clear();
        selectedUsersAdapter.add(selected);
        selectedUsersAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == searchText.getThreshold()) {
            PeopleService.getInstance().findByFilter(Utils.getToken(getActivity()), charSequence.toString(), findByFilterCallback);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.save) {
            validator.validate();
        }
        return false;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ((TextInputLayout) name.getParent()).setError(null);
        ((TextInputLayout) name.getParent()).setErrorEnabled(false);
        ((TextInputLayout) description.getParent()).setError(null);
        ((TextInputLayout) description.getParent()).setErrorEnabled(false);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getFailedRules().get(0).getMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((TextInputLayout) view.getParent()).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        actionGroup(name.getText().toString(), description.getText().toString());
    }

    protected void actionGroup(String name, String description) {
        String token = Utils.getToken(getActivity());
        GroupsService.getInstance().create(token, name, description, new Callback<GroupDTO>() {
            @Override
            public void success(GroupDTO groupDTO, Response response) {
                GroupsService groupsService = GroupsService.getInstance();
                for (int i = 0; i < selectedUsersAdapter.getCount(); ++i) {
                    groupsService.linkPeople(Utils.getToken(context), groupDTO.getId(), selectedUsersAdapter.getItem(i).getId(), linkPeopleCallback);
                }
                onActionGroupFromDialog.onActionGroupFromDialog();
                dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        dismiss();
    }
}
