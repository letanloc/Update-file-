package efood.com.demoupdateimage;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //
    String URl = "http://10.0.3.226:5010/update";
    ImageView DemoImage;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public String PosJson(String Base) {
        String Reqjson = "{'images'" + ":'" + Base + "'}";
        return Reqjson;
    }

    ImageView imageView;
    Button btnButton;

    private void init() {
        DemoImage = (ImageView) findViewById(R.id.imageUpdate);
        progressBar = (android.widget.ProgressBar) findViewById(R.id.pro);
        imageView = (ImageView) findViewById(R.id.imageUpdate);
        btnButton = (Button) findViewById(R.id.btnUpdateImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeImage();
            }
        });

    }

    // thuc hien tao duongf dẩn đên file
    final String uploadFilePath = "/mnt/sdcard/";


    // action post
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.e("SÂSASSA", response.body().string());
        return response.body().string();
    }

    OkHttpClient client = new OkHttpClient();


    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        new UpdateImage().execute();
//        new ASYNTACK().execute();


    }

    ProgressBar progressBar;

    public void TakeImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 1);


        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"), 1);

    }

    Bitmap bm;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(thumbnail);
            } else if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                imageView.setImageBitmap(bm);
                btnButton.setVisibility(View.VISIBLE);

                btnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                        byte[] ba = bao.toByteArray();
                        String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
                        progressBar.setVisibility(View.VISIBLE);
                        SimpleDateFormat dateFormat =new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                        Date date =new Date();
                        Ion.with(MainActivity.this)
                                .load(URl)
                                .setBodyParameter("images", ba1)
                                .setBodyParameter("titles","HELLO"+"_"+dateFormat.format(date.getTime()) )
//                .setBodyParameter("foo", "bar")
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });

                    }
                });
//                    progressBar
            }
        }
    }


//     public void init(){}


//    public class ASYNTACK extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//            ByteArrayOutputStream bao = new ByteArrayOutputStream();
//            bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//            byte[] ba = bao.toByteArray();
//            String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
//
//            try {
//                post(URl, PosJson(ba1));
//
//            } catch (Exception e) {
//                Log.e("SÂSSAAS", e + "s");
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressBar.setVisibility(View.GONE);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//    }


//    class UpdateImage extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                URL url = new URL(URl);
////                Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
////                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                // convert image to basesixfour
//                ;
//
//                BitmapDrawable drawable1 = (BitmapDrawable) DemoImage.getDrawable();
//                Bitmap bitmap = drawable1.getBitmap();
//                // change  url openconetion to  Urlconetion
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
//                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
//                connection.setDoOutput(true);
//                //   ouput string
//                //   updat base
//
//                Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                ByteArrayOutputStream bao = new ByteArrayOutputStream();
//                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//                byte[] ba = bao.toByteArray();
//                String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
//
//                String urlParameters = "ima ges=" + ba1; //ConvertBaseSixFour(bitmap);
//                Log.e("BASE ", ConvertBaseSixFour(bitmap));
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters);
//
//                dStream.flush();
//                dStream.close();
//                int responseCode = connection.getResponseCode();
//
//
//                final StringBuilder output = new StringBuilder("Request URL " + url);
//                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
//                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
//                output.append(System.getProperty("line.separator") + "Type " + "POST");
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = "";
//                StringBuilder responseOutput = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    responseOutput.append(line);
//                }
//                br.close();
//                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//
//            } catch (Exception e) {
//                Log.e("data error", e.toString());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressBar.setVisibility(View.GONE);
//        }
//    }


    //     public  String  Example
    private String ConvertBaseSixFour(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        return imgString;
    }

    // get api address
    String TAG = "HELLO";

    private static String md5(String s) {
        try {

            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d(TAG, inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }
}
