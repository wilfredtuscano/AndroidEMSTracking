package com.example.wtuscano.emstracking.fragment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.activity.AdminMainActivity;
import com.example.wtuscano.emstracking.activity.DriverMainActivity;
import com.example.wtuscano.emstracking.adapter.GridEventImagesAdapter;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class AdminDocsFragment extends Fragment {

    private IAdminDocsInteractionListener mListener;

    private FloatingActionButton fabAddImages;
    private ProgressDialog pd;
    private GridView gvImageGrid;
    private GridEventImagesAdapter gvMyDocsAdapter;

    private int flagToCheckIfUploadIsComplete = 0;
    private String parentClassName;
    private Event currentEvent = new Event();
    private Bus currentBus;
    private ArrayList<Uri> imagesToUpload = new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference muidEvent;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference;

    public AdminDocsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intitializeComponents();
    }

    private void intitializeComponents() {
        gvImageGrid = (GridView) getActivity().findViewById(R.id.gvDocImages);

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);

        currentEvent = mListener.getCurrentEvent();
        currentBus = mListener.getCurrentBus();
        parentClassName = mListener.getParentClassName();

        fabAddImages = (FloatingActionButton) getActivity().findViewById(R.id.fabAddImages);
        if (currentBus != null) {
            fabAddImages.hide();
        } else {
            fabAddImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), 1);
                }
            });
        }

        muidEvent = databaseReference.child("EVENT");

        //Setup Images grid but check for the Parent Class First
        if (AdminMainActivity.CLASS_NAME.equals(parentClassName)) {
            gvMyDocsAdapter = new GridEventImagesAdapter((AdminMainActivity) getActivity(), currentEvent.getInfoSheetsURLs());
        } else if (DriverMainActivity.CLASS_NAME.equals(parentClassName)) {
            gvMyDocsAdapter = new GridEventImagesAdapter((DriverMainActivity) getActivity(), currentEvent.getInfoSheetsURLs());
        }

        gvImageGrid.setAdapter(gvMyDocsAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null && data.getClipData() != null) {
                    pd.show();

                    flagToCheckIfUploadIsComplete = 1;
                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        imagesToUpload.add(mClipData.getItemAt(i).getUri());

                        Uri filepath = mClipData.getItemAt(i).getUri();
                        storageReference = firebaseStorage.getReference("images/" + currentEvent.getKey() + "/" + filepath.getLastPathSegment());
                        UploadTask task = storageReference.putFile(filepath);
                        task.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ArrayList<String> imageURLs = currentEvent.getInfoSheetsURLs();
                                @SuppressWarnings("VisibleForTests") String s = taskSnapshot.getDownloadUrl().toString();
                                imageURLs.add(s);
                                currentEvent.setInfoSheetsURLs(imageURLs);

                                if (flagToCheckIfUploadIsComplete == imagesToUpload.size()) {
                                    muidEvent.child(currentEvent.getKey()).setValue(currentEvent);

                                    muidEvent.child(currentEvent.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            currentEvent = dataSnapshot.getValue(Event.class);
                                            mListener.setCurrentEvent(currentEvent);

                                            //Setup Updated Images grid
                                            gvMyDocsAdapter = new GridEventImagesAdapter((AdminMainActivity) getActivity(), currentEvent.getInfoSheetsURLs());
                                            gvImageGrid.setAdapter(gvMyDocsAdapter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Upload Complete.", Toast.LENGTH_SHORT).show();
                                } else {
                                    flagToCheckIfUploadIsComplete++;
                                }
                            }
                        });
                    }
                }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_docs, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAdminDocsInteractionListener) {
            mListener = (IAdminDocsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IAdminDocsInteractionListener {
        Event getCurrentEvent();

        void setCurrentEvent(Event currentEvent);

        Bus getCurrentBus();

        String getParentClassName();
    }
}
