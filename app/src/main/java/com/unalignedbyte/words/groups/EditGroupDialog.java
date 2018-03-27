package com.unalignedbyte.words.groups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.unalignedbyte.words.MainApplication;
import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.WordsDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rafal on 27/03/2018.
 */

public class EditGroupDialog extends DialogFragment
{
    @BindView(R.id.edit_group_nameEdit)
    EditText nameEdit;
    @BindView(R.id.edit_group_languageTitleText)
    TextView languageTitleText;
    @BindView(R.id.edit_group_languageSpinner)
    Spinner languageSpinner;
    private Group group;

    public static EditGroupDialog dialog(Group group)
    {
        EditGroupDialog dialog = new EditGroupDialog();

        Bundle arguments = new Bundle();
        if(group != null) {
            arguments.putInt("groupId", group.getId());
        }
        dialog.setArguments(arguments);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int groupId = getArguments().getInt("groupId", -1);
        if(groupId >= 0) {
            this.group = WordsDataSource.get(getActivity()).getGroup(groupId);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_group, null);
        AlertDialog dialog = builder.setView(view)
                .setPositiveButton(R.string.add_group, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        ButterKnife.bind(this, view);

        return dialog;
    }
}
