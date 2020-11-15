package com.aspegrenide.multiknapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String DATABASE_MESSAGES = "messages";
    private static final String DATABASE_ENHETER = "enheter";
    private static final String DATABASE_KUNDER = "customers";

    private static final String LOG_TAG = "M3 MainActivity";

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQ_CODE_SPEECH_INPUT = 2;

    private ImageView cameraImage;
    private VideoView videoView;
    private EditText tvTextMessage;
    private EditText tvTextMessageFA;
    private EditText tvTextMessageEN;
    private CheckBox chkKometen;
    private CheckBox chkAteljeCity;
    private CheckBox chkMullvaden;
    private CheckBox chkDunckers;

    private MediaController mediaControls;
    private Uri videoUri;
    private String videoPath;
    private Message currentMessage;
    private ArrayList<Message> messages;

    ArrayList<Enhet> enheter;
    ArrayList<Kund> kunder;


    // for debuggint
    String[] units = {"kometen", "city"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go look for data
        enheter = new ArrayList<Enhet>();
        kunder = new ArrayList<Kund>();
        setUpEnheterDatabaseListener();
        setUpKunderDatabaseListener();

        videoView = (VideoView) findViewById(R.id.videoView);
        //cameraImage = (ImageView) findViewById(R.id.imgViewHeader);
        tvTextMessage = (EditText) findViewById(R.id.tvTextMessage);
        tvTextMessageFA = (EditText) findViewById(R.id.tvTextMessageFA);
        tvTextMessageEN = (EditText) findViewById(R.id.tvTextMessageEN);

        tvTextMessage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentMessage.setMessageTextSV(tvTextMessage.getText().toString());
                kickOffTranslations();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        // fixa checkboxar för enheterna
        chkAteljeCity = (CheckBox) findViewById(R.id.chkEnhetAteljeCity);
        chkKometen = (CheckBox) findViewById(R.id.chkEnhetKometen);
        chkDunckers = (CheckBox) findViewById(R.id.chkEnhetDunckers);
        chkMullvaden = (CheckBox) findViewById(R.id.chkEnhetMullvaden);

        currentMessage = new Message();

        // DEBIG'
        tvTextMessage.setText("Hej allihop! På fredag är det personalfest på kometen");
        //currentMessage.setMessageTextSV("Hej allihop! På fredag är det personalfest på kometen");


        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = new MediaController(MainActivity.this);
            mediaControls.setAnchorView(videoView);
        }
        // set the media controller for video view
        videoView.setMediaController(mediaControls);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File folder = new File(dir.getAbsolutePath() + "/Camera");
        File mve = new File(dir.getAbsolutePath() + "/Camera/20200901_101555.mp4");

        File file[] = folder.listFiles();
        Log.d(LOG_TAG, "mve = " + mve);
//        videoView.setVideoPath(mve.getPath());
        // set the uri for the video view
        // use prerecorded c
        // Funkar inte Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/DCIM/Camera/20200830_093117.mp4");
        // Funkar inte videoView.setVideoURI(uri);
        // start a video
        // Funkar inte videoView.start();

        String[] projection = {MediaStore.Video.Media._ID};
        Cursor cursor = new CursorLoader(this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, // Return all rows
                null, null).loadInBackground();
        Log.d(LOG_TAG, "cursor = " + cursor);
        String mvPth = getVideoList();
        videoView.setVideoPath(mvPth);

    }

    private String getVideoList() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        ArrayList<String> pathArrList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
                return cursor.getString(0);
            }
            cursor.close();
        }
        Log.e("all path", pathArrList.toString());
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: " + units[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getAllUnits() {

    }

    public void recordVideo(View view) {
        dispatchTakeVideoIntent();
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    public void playVideo(View view) {
        playVideo();
    }

    public void playVideo() {
//        cameraImage.setVisibility(View.INVISIBLE);
//        videoView.setVisibility(View.VISIBLE);
        videoView.start();
    }

    public void generateTextFromVideo(View view) {
        generateTextFromVideo();
    }

    public void generateTextFromVideo() {
        //code for google voice recognition
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

        playVideo();
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> result = null;
        String Val = null;

        Log.d(LOG_TAG, "On activity result");

        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE: {
                if (resultCode == RESULT_OK && null != data) {
                    videoUri = data.getData();

                    videoPath = videoUri.toString();
                    videoView.setVideoURI(videoUri);

                    Log.d(LOG_TAG, "uri = " + videoUri);
                    Log.d(LOG_TAG, "path = " + videoPath);

                    currentMessage.setVideoPath(videoPath);
                    // cool now get the text
                    generateTextFromVideo();


                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvTextMessage.setText(result.get(0));
                    currentMessage.setMessageTextSV(result.get(0));
                    Log.d(LOG_TAG, "speech recognized = " + result);
                    suggestCheckboxes();

                    // kick off translation!
                    Log.d(LOG_TAG, "KICK OFF TRANSLATION = ");
                    kickOffTranslations();
                    //     cameraImage.setVisibility(View.VISIBLE);
                    //     videoView.setVisibility(View.INVISIBLE);
                }
                break;
            }
        }
    }

    private void suggestCheckboxes() {
        String mText = currentMessage.getMessageTextSV();
        for (Enhet e : enheter) {
            // kolla om det finns någon omnämning i meddelandet
            // typ: Hej alla på Kometen, då ska kometen checkas
            if (mText.toLowerCase().contains(e.getNamn().toLowerCase())) {
                Log.d(LOG_TAG, "Meddelande nämner enheten, föreslå: " + e.getNamn());
                e.setChecked(true);
                // now make sure the correct checkbox is clicked (suggested)
                if (e.getNamn().equalsIgnoreCase("Kometen")) {
                    chkKometen.setChecked(true);
                }
                if (e.getNamn().equalsIgnoreCase("Ateljé City")) {
                    chkAteljeCity.setChecked(true);
                }
                if (e.getNamn().equalsIgnoreCase("Mullvaden")) {
                    chkMullvaden.setChecked(true);
                }
                if (e.getNamn().equalsIgnoreCase("Dunckers")) {
                    chkDunckers.setChecked(true);
                }
            }
        }
    }

    public void saveMessage(View view) {
        Date now = new Date();
        currentMessage.setTimeStamp(now.getTime());
        DataManager dm = new DataManager();
        dm.writeMessageToFirebase(currentMessage, DATABASE_MESSAGES);
    }


    public void sendEmail(View view) {
//        sendEmail(currentMessage, null);
    }


    public void skapaMeddelanden(View view) {
        //translateTextToEnglish(text.getText().toString());
        Log.d(LOG_TAG, " skapa meddelande knappen tryckt");
        skapaMeddelanden();
    }

    public void skapaMeddelanden() {
        // heavy lifting
        // 1) Lista alla kunder som är kopplade till de valda enheterna
        // 2) För varje kandidat hämta språk och översätt meddelande till alla språk

        Log.d(LOG_TAG, " skapa meddelande");

        redUtVemSomSkaHaVad();

    }

    public void kickOffTranslations() {
        Log.d(LOG_TAG, "Skapa meddelande, translate text = " + currentMessage.getMessageTextSV());
        translateSvenskaToEnglish(currentMessage.getMessageTextSV());
        translateSvenskaToFarsi(currentMessage.getMessageTextSV());
    }

    private void redUtVemSomSkaHaVad() {
        Log.d(LOG_TAG, " redUtVemSomSkaHaVad");

        ArrayList<Kund> kandidater = getKandidater();
        // 3) för varke kandidat kolla sms eller email
        // getAllEmailCustomers("FA")
        ArrayList<Kund> emailRecipientsFA = new ArrayList<Kund>();
        ArrayList<Kund> emailRecipientsEN = new ArrayList<Kund>();
        ArrayList<Kund> emailRecipientsSV = new ArrayList<Kund>();
        ArrayList<Kund> smsRecipientsFA = new ArrayList<Kund>();
        ArrayList<Kund> smsRecipientsEN = new ArrayList<Kund>();
        ArrayList<Kund> smsRecipientsSV = new ArrayList<Kund>();

        for (Kund k : kandidater) {
            if (k.getLanguage().equalsIgnoreCase("FA")) {
                if (k.getWantedMedia().equalsIgnoreCase("E-post")) {
                    emailRecipientsFA.add(k);
                } else {
                    smsRecipientsFA.add(k);
                }
            }
            if (k.getLanguage().equalsIgnoreCase("EN")) {
                if (k.getWantedMedia().equalsIgnoreCase("E-post")) {
                    emailRecipientsEN.add(k);
                } else {
                    smsRecipientsEN.add(k);
                }
            }
            if (k.getLanguage().equalsIgnoreCase("SV")) {
                if (k.getWantedMedia().equalsIgnoreCase("E-post")) {
                    emailRecipientsSV.add(k);
                } else {
                    smsRecipientsSV.add(k);
                }
            }
        }
        Log.d(LOG_TAG, "email svenska till " + emailRecipientsSV);
        Log.d(LOG_TAG, "email eng till " + emailRecipientsEN);
        Log.d(LOG_TAG, "email farsi till " + emailRecipientsFA);

        Log.d(LOG_TAG, "sms svenska till " + smsRecipientsSV);
        Log.d(LOG_TAG, "sms eng till " + smsRecipientsEN);
        Log.d(LOG_TAG, "sms farsi till " + smsRecipientsFA);

        String mSubject = "";
        String mBody = "";

        // engelska
        if (emailRecipientsEN.size() > 0) {
            sendEmail(currentMessage.getMessageSubjectEN(), currentMessage.getMessageTextEN(), videoPath, emailRecipientsEN);
        }
        if (emailRecipientsFA.size() > 0) {
            sendEmail(currentMessage.getMessageSubjectFA(), currentMessage.getMessageTextFA(), videoPath, emailRecipientsFA);
        }
        if (emailRecipientsSV.size() > 0) {
            sendEmail(currentMessage.getMessageSubjectSV(), currentMessage.getMessageTextSV(), videoPath, emailRecipientsSV);
        }

        if (smsRecipientsSV.size() > 0) {
            sendSms(currentMessage.getMessageSubjectSV(), currentMessage.getMessageTextSV(), smsRecipientsEN);
        }
        if (smsRecipientsEN.size() > 0) {
            sendSms(currentMessage.getMessageSubjectEN(), currentMessage.getMessageTextEN(), smsRecipientsEN);
        }
        if (smsRecipientsFA.size() > 0) {
            sendSms(currentMessage.getMessageSubjectFA(), currentMessage.getMessageTextFA(), smsRecipientsEN);
        }

    }

    public void translateSvenskaToEnglish(String text) {
        //input är svensk...
        Log.d(LOG_TAG, "translateSvenskaToEnglish text = " + text);
        String languageCode = "SV";
        String languageTargetCode = "EN";
        downloadTranslatorAndTranslate(languageCode, languageTargetCode, text);
    }

    public void translateSvenskaToFarsi(String text) {
        //input är svensk...
        Log.d(LOG_TAG, "translateSvenskaToFarsi text = " + text);
        String languageCode = "SV";
        String languageTargetCode = "FA";
        downloadTranslatorAndTranslate(languageCode, languageTargetCode, text);
    }


    private ArrayList<Kund> getKandidater() {
        ArrayList<Kund> kandidater = new ArrayList<Kund>();
        for (Enhet e : enheter) {
            if (e.isChecked()) {
                // välj alla kunder som är kopplade
                for (Kund k : kunder) {
                    if (k.getEnhetDV().equalsIgnoreCase(e.getNamn())) {
                        if (!kandidater.contains(k)) {
                            kandidater.add(k);
                        }
                    }
                    if (k.getEnhetTP().equalsIgnoreCase(e.getNamn())) {
                        if (!kandidater.contains(k)) {
                            kandidater.add(k);
                        }
                    }
                }
            }
        }
        // kandidater innehåller nu alla kunder som är kopplade till enheterna som
        return kandidater;
    }


    // Translation
    public void translateText(FirebaseTranslator langTranslator, final String targetLanguage, String inputString) {
        //translate source text to english
        Log.d(LOG_TAG, "translate CALLED ");
        langTranslator.translate(inputString)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                Log.d(LOG_TAG, "translate to + " + targetLanguage);
                                Log.d(LOG_TAG, "translate ok + " + translatedText);
                                updateMessageWithTranslation(currentMessage, translatedText, targetLanguage);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(LOG_TAG, "translate NOK + " + e.toString());
                                Toast.makeText(MainActivity.this,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

    }

    private void updateMessageWithTranslation(Message mCurrentMessage, String translatedText, String targetLanguage) {
        if (targetLanguage.equalsIgnoreCase("FA")) {
            mCurrentMessage.setMessageTextFA(translatedText);
            Log.d(LOG_TAG, "added farsi message " + currentMessage.getMessageTextFA());
            tvTextMessageFA.setText(translatedText);

        }
        if (targetLanguage.equalsIgnoreCase("EN")) {
            mCurrentMessage.setMessageTextEN(translatedText);
            Log.d(LOG_TAG, "added english message " + currentMessage.getMessageTextEN());
            tvTextMessageEN.setText(translatedText);
        }

    }

    public void downloadTranslatorAndTranslate(String langCode, final String langTargetCode, final String inputString) {
        //get source language id from bcp code
        int sourceLanguage = FirebaseTranslateLanguage
                .languageForLanguageCode(langCode);

        int targetLanguage = FirebaseTranslateLanguage
                .languageForLanguageCode(langTargetCode);

        //create translator for source and target languages
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguage)
                        .setTargetLanguage(targetLanguage)
                        .build();
        final FirebaseTranslator langTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        //download language models if needed
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        langTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Log.d("translator", "downloaded lang  model");
                                //after making sure language models are available
                                //perform translation
                                translateText(langTranslator, langTargetCode, inputString);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
    }


    public void sendSms(View view) {

    }

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        // add checks
        return str.substring(0, str.length() - chars);
    }

    public void sendSms(String mSubject, String mBody, ArrayList<Kund> mottagare) {
        String recipients = "";
        for (Kund m: mottagare) {
            recipients += m.getPhoneNumberSMS() + ",";
        }
        removeLastChar(recipients);

        Uri uri = Uri.parse("smsto:" + recipients);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", mBody);
        startActivity(it);
    }

    public void sendEmail(String mSubject, String mBody, String videoPath, ArrayList<Kund> mottagare) {

        ArrayList<String> listOfRecipients = new ArrayList<String>();
        //add the epostadresser
        for (Kund k : mottagare) {
            listOfRecipients.add(k.getEpostadress());
        }
        String[] recipients = listOfRecipients.toArray(new String[0]);

//        Uri attachement = Uri.parse(videoPath);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, recipients);
        i.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        i.putExtra(Intent.EXTRA_TEXT, mBody);
        i.putExtra(Intent.EXTRA_STREAM, videoUri); // borde ju skickas med...

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    // database below
    private void setUpEnheterDatabaseListener() {

        // load data from database about enheter
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        databaseReference.child(DATABASE_ENHETER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "Groups changed and kicked back, or init call?");
                // get all children at this level
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //shake hands with all items
                enheter.clear();
                for (DataSnapshot child : children) {
                    try {
                        Enhet e = child.getValue(Enhet.class);
                        if (e.getNamn() == null) {
                            Log.d(LOG_TAG, "Dont add group corrupt " + e.toString());
                        } else {
                            enheter.add(e);

                            Log.d(LOG_TAG, "Add group " + e.toString());
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error  " + e.toString());
                    }

                    int i = 0;
                    units = new String[enheter.size()];
                    for (Enhet e : enheter) {
                        units[i++] = e.getNamn();
                        Log.d(LOG_TAG, "add to units  " + e);
                    }
                    Log.d(LOG_TAG, "units  " + units);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // database below
    private void setUpKunderDatabaseListener() {

        // load data from database about kunder
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        databaseReference.child(DATABASE_KUNDER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "Groups changed and kicked back, or init call?");
                // get all children at this level
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //shake hands with all items
                kunder.clear();
                for (DataSnapshot child : children) {
                    try {
                        Kund k = child.getValue(Kund.class);
                        if (k.getFullName() == null) {
                            Log.d(LOG_TAG, "Dont add group corrupt " + k.toString());
                        } else {
                            kunder.add(k);
                            Log.d(LOG_TAG, "Add to kunder " + k.toString());
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error  " + e.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void chkKlicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.chkEnhetKometen:
                getEnhet("Kometen").setChecked(checked);
                break;
            case R.id.chkEnhetAteljeCity:
                getEnhet("Ateljé City").setChecked(checked);
        }
    }

    private Enhet getEnhet(String name) {
        for(Enhet e: enheter) {
            if (e.getNamn().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}