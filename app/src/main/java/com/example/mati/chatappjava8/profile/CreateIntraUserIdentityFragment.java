package com.example.mati.chatappjava8.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.example.mati.app_core.Core;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.commons.Utils;
import com.example.mati.chatappjava8.util.BitmapWorkerTask;

import org.iop.ns.chat.ChatNetworkServicePluginRoot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Updated by Andres Abreu aabreu1 27/06/16
 */
public class CreateIntraUserIdentityFragment extends Fragment {

    private static final String TAG = "CreateIdentity";

    private static final int CREATE_IDENTITY_FAIL_MODULE_IS_NULL = 0;
    private static final int CREATE_IDENTITY_FAIL_NO_VALID_DATA = 1;
    private static final int CREATE_IDENTITY_FAIL_MODULE_EXCEPTION = 2;
    private static final int CREATE_IDENTITY_SUCCESS = 3;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;

    private static final int CONTEXT_MENU_CAMERA = 1;
    private static final int CONTEXT_MENU_GALLERY = 2;
    private byte[] brokerImageByteArray;
    private Button createButton;
    private EditText mBrokerName;
    private ImageView mBrokerImage;
    private ImageView mphoto_header;
    private boolean isUpdate = false;
    private boolean contextMenuInUse = false;
    private Uri imageToUploadUri;
    private Bitmap imageBitmap;

    ExecutorService executorService;

    private ChatNetworkServicePluginRoot manager;
    private ActorProfile identity;

    public static CreateIntraUserIdentityFragment newInstance(ChatNetworkServicePluginRoot chatNetworkServicePluginRoot) {
        CreateIntraUserIdentityFragment createIntraUserIdentityFragment = new CreateIntraUserIdentityFragment();
        createIntraUserIdentityFragment.setManager(chatNetworkServicePluginRoot);
        return createIntraUserIdentityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newFixedThreadPool(3);
        identity = Core.getInstance().getProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootLayout = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(rootLayout);
        setUpIdentity();
        return rootLayout;
    }

    /**
     * Inicializa las vistas de este Fragment
     *
     * @param layout el layout de este Fragment que contiene las vistas
     */
    private void initViews(View layout) {
        createButton = (Button) layout.findViewById(R.id.create_crypto_broker_button);
        mBrokerName = (EditText) layout.findViewById(R.id.crypto_broker_name);
        mBrokerImage = (ImageView) layout.findViewById(R.id.img_photo);
        mphoto_header = (ImageView) layout.findViewById(R.id.img_photo_header);

        mBrokerName.requestFocus();
        registerForContextMenu(mBrokerImage);

        mBrokerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().openContextMenu(mBrokerImage);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View view) {

                if (Core.getInstance().getProfile() == null)
                    publishResult(createNewIdentity());
                else
                    publishResult(updateIdentity());
            }
        });
    }

    private void publishResult(final int resultKey){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (resultKey) {
                    case CREATE_IDENTITY_SUCCESS:
                        if (!isUpdate) {
                            Toast.makeText(getActivity(), "Identity created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
                        }
                        getActivity().onBackPressed();
                        break;
                    case CREATE_IDENTITY_FAIL_MODULE_EXCEPTION:
                        Toast.makeText(getActivity(), "Error creating identity", Toast.LENGTH_LONG).show();
                        break;
                    case CREATE_IDENTITY_FAIL_NO_VALID_DATA:
                        Toast.makeText(getActivity(), "La data no es valida", Toast.LENGTH_LONG).show();
                        break;
                    case CREATE_IDENTITY_FAIL_MODULE_IS_NULL:
                        Toast.makeText(getActivity(), "No se pudo acceder al module manager, es null", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void setUpIdentity() {
        try {//

            if (identity != null) {
                loadIdentity();
                isUpdate = true;
                createButton.setText("Save changes");
                createButton.setBackgroundColor(Color.parseColor("#7DD5CA"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIdentity(){
        if (identity.getPhoto() != null) {
            Bitmap bitmap = null;
            if (identity.getPhoto().length > 0) {
                bitmap = BitmapFactory.decodeByteArray(identity.getPhoto(), 0, identity.getPhoto().length);
                BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(mphoto_header,getResources(),0,true);
                bitmapWorkerTask.execute(identity.getPhoto());
                mphoto_header.setAlpha(150);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            brokerImageByteArray = toByteArray(bitmap);
            mBrokerImage.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), bitmap));

        }
        mBrokerName.setText(identity.getName());
//        mBrokerPhrase.setText(identity.getExtraData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            contextMenuInUse = true;

            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    if (imageToUploadUri != null) {
                        String provider = "com.android.providers.media.MediaProvider";
                        Uri selectedImage = imageToUploadUri;
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                getActivity().getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                getActivity().grantUriPermission(provider, selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                getActivity().grantUriPermission(provider, selectedImage, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                getActivity().grantUriPermission(provider, selectedImage, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                getActivity().requestPermissions(
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_IMAGE_CAPTURE);
                            }
                        }
                        getActivity().getContentResolver().notifyChange(selectedImage, null);
                        Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                        if (reducedSizeBitmap != null) {
                            imageBitmap = reducedSizeBitmap;
                        }
                    }
                    try {
                        if (checkCameraPermission()) {
                            if (checkWriteExternalPermission()) {
                                if (imageBitmap == null)
                                    Toast.makeText(getActivity(), "Error on upload image", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUEST_LOAD_IMAGE:
                    Uri selectedImage = data.getData();
                    try {
                        if (isAdded()) {
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            String provider = "com.android.providers.media.MediaProvider";
                            if (Build.VERSION.SDK_INT >= 23) {

                                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    getActivity().grantUriPermission(getActivity().getPackageName(), selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    getActivity().grantUriPermission(provider, selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    getActivity().grantUriPermission(provider, selectedImage, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    getActivity().grantUriPermission(provider, selectedImage, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                    getActivity().getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);
                                    getActivity().requestPermissions(
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_LOAD_IMAGE);
                                }
                            }
                            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage);
                            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, mBrokerImage.getWidth(), mBrokerImage.getHeight(), true);
                            brokerImageByteArray = toByteArray(imageBitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Error cargando la imagen", Toast.LENGTH_SHORT).show();
                    }

//                    Uri selectedImage = data.getData();
                    try {
                        if (isAdded()) {
                            if (imageBitmap.getWidth() < 192 || imageBitmap.getHeight() < 192)
                                Toast.makeText(getActivity(), "The image selected is too small. Please select a photo with height and width of at least 192x192", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error loading the image", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }

            final Bitmap finalImageBitmap = imageBitmap;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mBrokerImage != null && finalImageBitmap != null)
                        mBrokerImage.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), finalImageBitmap));
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose mode");
        menu.add(Menu.NONE, CONTEXT_MENU_GALLERY, Menu.NONE, "Gallery");

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(!contextMenuInUse) {
            switch (item.getItemId()) {
                case CONTEXT_MENU_CAMERA:
                    dispatchTakePictureIntent();
                    contextMenuInUse = true;
                    return true;
                case CONTEXT_MENU_GALLERY:
                    loadImageFromGallery();
                    contextMenuInUse = true;
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Crea una nueva identidad para un crypto broker
     *
     * @return key con el resultado de la operacion:<br/><br/>
     * <code>CREATE_IDENTITY_SUCCESS</code>: Se creo exitosamente una identidad <br/>
     * <code>CREATE_IDENTITY_FAIL_MODULE_EXCEPTION</code>: Se genero una excepcion cuando se ejecuto el metodo para crear la identidad en el Module Manager <br/>
     * <code>CREATE_IDENTITY_FAIL_MODULE_IS_NULL</code>: No se tiene una referencia al Module Manager <br/>
     * <code>CREATE_IDENTITY_FAIL_NO_VALID_DATA</code>: Los datos ingresados para crear la identidad no son validos (faltan datos, no tiene el formato correcto, etc) <br/>
     */
    private int createNewIdentity() {

        final String brokerNameText = mBrokerName.getText().toString();

        boolean dataIsValid = validateIdentityData(brokerNameText, brokerImageByteArray);

        if (dataIsValid) {
            final ActorProfile profile = new ActorProfile();
            profile.setIdentityPublicKey(UUID.randomUUID().toString());
            System.out.println("I will try to register an actor with pk " + profile.getIdentityPublicKey());
            profile.setActorType(Actors.CHAT.getCode());
            profile.setName(mBrokerName.getText().toString());
            profile.setAlias("Alias chat");
            //This represents a valid image
            profile.setPhoto(brokerImageByteArray);
            profile.setExtraData("Test extra data");
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Utils.saveActorProfileSettings(getActivity(), profile);
                    Core.getInstance().setProfile(profile);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Registering profile...",Toast.LENGTH_SHORT).show();
                            createButton.setText("Save changes");
                        }
                    });
                }
            });

            return CREATE_IDENTITY_SUCCESS;

        }
        return CREATE_IDENTITY_FAIL_NO_VALID_DATA;

    }

    public int updateIdentity(){
        final String brokerNameText = mBrokerName.getText().toString();

        boolean dataIsValid = validateIdentityData(brokerNameText, brokerImageByteArray);

        if (dataIsValid) {
            final ActorProfile profile = Core.getInstance().getProfile();
            System.out.println("I will try to update an actor with pk " + profile.getIdentityPublicKey());
            profile.setName(mBrokerName.getText().toString());
            profile.setAlias("Alias chat");
            //This represents a valid image
            profile.setPhoto(brokerImageByteArray);
            profile.setExtraData("Test extra data");
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Utils.saveActorProfileSettings(getActivity(), profile);
                    Core.getInstance().updateProfile(profile);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Registering profile...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return CREATE_IDENTITY_SUCCESS;
        }else return CREATE_IDENTITY_FAIL_NO_VALID_DATA;
    }

    private void dispatchTakePictureIntent() {
        Log.i(TAG, "Opening Camera app to take the picture...");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void loadImageFromGallery() {
        Log.i(TAG, "Loading Image from Gallery...");

        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImageIntent, REQUEST_LOAD_IMAGE);
    }

    private boolean validateIdentityData(String brokerNameText, byte[] brokerImageBytes) {
        if (brokerNameText.isEmpty())
            return false;
        if (brokerImageBytes == null)
            return true;
        if (brokerImageBytes.length > 0)
            return true;

        return true;
    }

    /**
     * Bitmap to byte[]
     *
     * @param bitmap Bitmap
     * @return byte array
     */
    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    private Bitmap getBitmap(String path) {
        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 3000000; // 1.2MP
            in = getActivity().getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getActivity().getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    private boolean checkCameraPermission() {
        String permission = "android.permission.CAMERA";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean checkWriteExternalPermission() {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void setManager(ChatNetworkServicePluginRoot manager) {
        this.manager = manager;
    }
}
