package in.entrylog.entrylog.main.adhocVisitors.adhoccamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.gpio.Ioctl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import in.entrylog.entrylog.R;

public class AdhocCameraActivity  extends AppCompatActivity implements SurfaceHolder.Callback{

    private SurfaceView cameraV;
    private Camera mCamera;
    private ImageButton takePic;
    private Point screenResolution;
    private Point cameraResolution;
    private int previewFormat;
    private String previewFormatString;
    private boolean isLight;
    private ImageView iv;
    final static private int NEW_PICTURE = 1;
    private String mCameraFileName;
    String s;
    ImageButton save;
    Uri URI;

    ImageButton cancel;
    private static final int REQUEST_FOR_ACTIVITY_CODE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=2;
    String imageName = "BGS";

    //brightness camera
    SeekBar seekbar;
    TextView progress;
    Context context;
    int Brightness;

    private boolean isTakePic;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap b = (Bitmap) msg.obj;
                    iv.setImageBitmap(b);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv.setDrawingCacheEnabled(true);
                            Bitmap bitmap = iv.getDrawingCache();

                            // bitmap = Bitmap.createBitmap(480, 800,
                            // Bitmap.Config.ARGB_8888);

                            //To save the media Files

                            /*String root = Environment.getExternalStorageDirectory()
                                    .toString();
                            File newDir = new File(root + "/Collage_Maker");
                            newDir.mkdirs();
                            Random gen = new Random();
                            int n = 10000;
                            n = gen.nextInt(n);
                            String fotoname = "cm_" + n + ".jpg";
                            File file = new File(newDir, fotoname);
                            s = file.getAbsolutePath();
                            Log.i("Path of saved image.", s);
                            System.err.print("Path of saved image." + s);

                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                                out.flush();
                                Toast.makeText(getApplicationContext(), "Photo Saved " + fotoname,
                                        Toast.LENGTH_SHORT).show();
                                out.close();
                            } catch (Exception e) {

                                Log.e("Exception", e.toString());
                            }
                            scanPhoto(getApplicationContext(),s);*/
                        }
                    });


                    break;
                case 1:
                    Toast.makeText(AdhocCameraActivity.this, "Takepic", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc_camera);
        cameraV = (MySurfaceView1) findViewById(R.id.sv1);
        SurfaceHolder holder = cameraV.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        RotateAnimation ra = new RotateAnimation(0, 90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setFillAfter(true);
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        // screenResolution = new Point(dm.widthPixels*dm.density,
        // display.getHeight());
        screenResolution = new Point(64, 128);
        takePic = (ImageButton) findViewById(R.id.take_pi_btn1);
        Button light = (Button) findViewById(R.id.light1);
        iv = (ImageView) findViewById(R.id.iv1);
        cancel= (ImageButton) findViewById(R.id.cancel_camera1);

        //setBtnEnabledFalse();
        save = (ImageButton) findViewById(R.id.save1);
        save.setVisibility(View.GONE);


        //brightness

        seekbar = (SeekBar)findViewById(R.id.seekBar11);
        progress = (TextView)findViewById(R.id.textView11);
        context = getApplicationContext();
        //Getting Current screen brightness.
        Brightness = Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0
        );

        //Setting up current screen brightness to seekbar;
        seekbar.setProgress(Brightness);

        //Setting up current screen brightness to TextView;
        progress.setText("Screen Brightness : " + Brightness);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                progress.setText("Screen Brightness : " + i);

                //Changing Brightness on seekbar movement.

                Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,i
                );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        light.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (!isLight) {

                        Ioctl.activate(23, 1);
                        isLight = true;
                    } else {
                        Ioctl.activate(23, 0);
                        isLight = false;
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdhocCameraActivity.this.finish();
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isTakePic) {
                    isTakePic = true;
                    save.setVisibility(View.VISIBLE);

                }
                // setBtnEnabledTrue();
                // mCamera.takePicture(new ShutterCallback() {
                //
                // @Override
                // public void onShutter() {
                // // TODO Auto-generated method stub
                // // ����
                // }
                // }, new PictureCallback() {
                //
                // @Override
                // public void onPictureTaken(byte[] data, Camera camera) {
                // // TODO Auto-generated method stub
                //
                // }
                // }, new PictureCallback() {
                //
                // @Override
                // public void onPictureTaken(byte[] data, Camera camera) {
                // // TODO Auto-generated method stub
                // Bitmap b = BitmapFactory.decodeByteArray(data, 0,
                // data.length);
                // if (b != null) {
                //
                // handler.sendMessage(handler.obtainMessage(0,
                // rotate90(b)));
                // }
                //
                // mCamera.startPreview();
                // }
                // });
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                iv.setDrawingCacheEnabled(true);
                Bitmap bitmap = iv.getDrawingCache();
                // bitmap = Bitmap.createBitmap(480, 800,
                // Bitmap.Config.ARGB_8888);
                String root = Environment.getExternalStorageDirectory()
                        .toString();
                File newDir = new File(root + "/Collage_Maker");
                newDir.mkdirs();
                Random gen = new Random();
                int n = 10000;
                n = gen.nextInt(n);
                String fotoname = "cm_" + n + ".jpg";
                File file = new File(newDir, fotoname);
                s = file.getAbsolutePath();
                Log.i("Path of saved image.", s);
                System.err.print("Path of saved image." + s);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    out.flush();
                    // Toast.makeText(getApplicationContext(), "Photo Saved " + fotoname,Toast.LENGTH_SHORT).show();
                    out.close();
                } catch (Exception e) {

                    Log.e("Exception", e.toString());
                }
                scanPhoto(getApplicationContext(),s);
                String image = s;
                Intent intent = new Intent();
                intent.putExtra("picture", image);
                setResult(RESULT_OK, intent);
                finish();




                //startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


                /*String root = Environment.getExternalStorageDirectory()
                        .toString();
                File newDir = new File(root + "/Collage_Maker");
                newDir.mkdirs();
                Random gen = new Random();
                int n = 10000;
                n = gen.nextInt(n);
                String fotoname = "cm_" + n + ".jpg";
                File file = new File(newDir, fotoname);
                String s = file.getAbsolutePath();
                Log.i("Path of saved image.", s);
                System.err.print("Path of saved image." + s);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    out.flush();
                    Toast.makeText(getApplicationContext(), "Photo Saved " + fotoname,
                            Toast.LENGTH_SHORT).show();
                    out.close();
                } catch (Exception e) {

                    Log.e("Exception", e.toString());
                }
                scanPhoto(getApplicationContext(),s);*/
            }
        });


    }


    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    private Bitmap getFromCache(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); // Make sure to call Bitmap.createBitmap before disabling the cache, as the Bitmap will be recycled once it is disabled again
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == NEW_PICTURE)
        {
            // return from file upload
            if (resultCode == Activity.RESULT_OK)
            {
                Uri uri = null;
                if (data != null)
                {
                    uri = data.getData();
                }
                if (uri == null && mCameraFileName != null)
                {
                    uri = Uri.fromFile(new File(mCameraFileName));
                }
                File file = new File(mCameraFileName);
                if (!file.exists()) {
                    file.mkdir();
                }

            }
        }}

    private void setBtnEnabledFalse() {
        takePic.setEnabled(true);
        save.setEnabled(false);
        cancel.setEnabled(false);

    }
    private void setBtnEnabledTrue() {
        takePic.setEnabled(false);
        save.setEnabled(true);
        cancel.setEnabled(true);

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // TODO Auto-generated method stub

        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // TODO Auto-generated method stub
                    if (isTakePic) {
                        YuvImage yI = new YuvImage(
                                data,
                                mCamera.getParameters().getPreviewFormat(),
                                mCamera.getParameters().getPreviewSize().width,
                                mCamera.getParameters().getPreviewSize().height,
                                null);
                        Bitmap b = rawByteArray2RGBABitmap(yI.getYuvData(),
                                yI.getWidth(), yI.getHeight());
                        handler.sendEmptyMessage(1);
                        if (b != null) {
                            handler.sendMessage(handler.obtainMessage(0,
                                    rotate90(b)));

                        }
                        isTakePic = false;
                    }
                }
            });
            mCamera.startPreview();
            mCamera.autoFocus(new Camera.AutoFocusCallback() {

                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    // TODO Auto-generated method stub
                    if (success) {
                        // �Խ��ɹ�
                        Toast.makeText(AdhocCameraActivity.this, "Camera is ready",
                                Toast.LENGTH_SHORT).show();

                    } else {

                    }
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(AdhocCameraActivity.this, "Camera is not ready!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null)
            return;
        try {
            // TODO Auto-generated method stub
            Camera.Parameters para = mCamera.getParameters();
            // para.setPreviewSize(width, height);
            List<Camera.Size> vSList = para.getSupportedPictureSizes();
            for (int num = 0; num < vSList.size(); num++) {
                Camera.Size vS = vSList.get(num);
                if (vS != null) {
                    // para.setPreviewSize(vS.width, vS.height);
                    break;
                }
            }
            // if (this.getResources().getConfiguration().orientation !=
            // Configuration.ORIENTATION_LANDSCAPE) {
            // //para.set("orientation", "portrait");
            // mCamera.setDisplayOrientation(90);
            // } else {
            // //para.set("orientation", "landscape");
            // mCamera.setDisplayOrientation(0);
            // }
            para.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(para);
            mCamera.setDisplayOrientation(270);
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                mCamera.release();
                cameraV = null;
            }
            mCamera.startPreview();
        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        // TODO Auto-generated method stub
        try {
            if (mCamera != null)
                finish();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            finish();
            // Ioctl.activate(23, 0);
        } catch (Exception e) {
            finish();
            // TODO: handle exception
        }


    }

    private Bitmap rotate90(Bitmap src) {
        Matrix m = new Matrix();
        m.setRotate(-90, (float) src.getWidth() / 2, (float) src.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                    src.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    /**
     * ��Ƶ֡��ʽת��:YuvImage ת Bitmap
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    public Bitmap rawByteArray2RGBABitmap(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width
                        + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width
                        + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128)
                        - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        Bitmap bmp = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private  void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }
}