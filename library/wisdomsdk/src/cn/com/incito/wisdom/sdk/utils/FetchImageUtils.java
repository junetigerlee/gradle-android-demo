package cn.com.incito.wisdom.sdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class FetchImageUtils {
	/** The launch code when taking a picture */
	private static final int CAMERA_WITH_DATA = 3023;

	/** The launch code when picking a photo and the raw data is returned */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;

	private static int DEFAULT_IMAGE_SIZE = 320;

	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");

	private Activity mActivity;
	private File mCurrentPhotoFile;
	private OnPickFinishedCallback callback;
	private int photox = DEFAULT_IMAGE_SIZE;
	private int photoy = DEFAULT_IMAGE_SIZE;

	public static interface OnPickFinishedCallback {
		public void onPickSuccessed(Bitmap bm);

		public void onPickFailed();

		public void onPickCancel();
	}

	public FetchImageUtils(Activity activity) {
		mActivity = activity;
	}

	/**
	 * 用户�?��在Activity中的onActivityResult 方法中调用此方法�?
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			if (Activity.RESULT_CANCELED == resultCode) {
				if (callback != null) {
					callback.onPickCancel();
				}
			} else {
				if (data == null) {
					if (callback != null) {
						callback.onPickFailed();
					}
					return;
				}
//				Bitmap bm = data.getParcelableExtra("data");
//				if (callback != null) {
//					callback.onPickSuccessed(bm);
//				}
				Uri uri = data.getData();
				Log.e("uri", uri.toString());
				Bitmap bm = getSmallBitmap(getAbsoluteImagePath(uri));
				if (callback != null) {
					callback.onPickSuccessed(bm);
				}
				
			}
			break;
		case CAMERA_WITH_DATA:
			if (Activity.RESULT_CANCELED == resultCode) {
				if (callback != null) {
					callback.onPickCancel();
				}
			} else {

				// doCropPhoto(mCurrentPhotoFile);
				Bitmap bm=getSmallBitmap(mCurrentPhotoFile.getAbsolutePath());
				if (callback != null) {
					callback.onPickSuccessed(bm);
				}

			}
			break;
		}
	}

	/**
	 * Sends a newly acquired photo to Gallery for cropping
	 */
	private void doCropPhoto(File f) {
		try {
			// Add the image to the media store
			MediaScannerConnection.scanFile(mActivity,
					new String[] { f.getAbsolutePath() },
					new String[] { null }, null);

			// Launch gallery to crop the photo
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			mActivity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onPickFailed();
			}
		}
	}

	/**
	 * Constructs an intent for image cropping.
	 */
	private Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", photox);
		intent.putExtra("aspectY", photoy);
		intent.putExtra("outputX", photox);
		intent.putExtra("outputY", photoy);
		intent.putExtra("return-data", true);
		return intent;
	}

	public void doTakePhoto(OnPickFinishedCallback callback) {
		doTakePhoto(callback, DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);
	}

	/**
	 * Launches Camera to take a picture and store it in a file.
	 */
	public void doTakePhoto(OnPickFinishedCallback callback, int width,
			int height) {
		this.callback = callback;
		this.photox = width;
		this.photoy = height;
		try {
			// Launch camera to take photo for selected contact
			PHOTO_DIR.mkdirs();
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			mActivity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onPickFailed();
			}
		}
	}

	public void doPickPhotoFromGallery(OnPickFinishedCallback callback) {
		doPickPhotoFromGallery(callback, DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);
	}

	/**
	 * Launches Gallery to pick a photo.
	 */
	public void doPickPhotoFromGallery(OnPickFinishedCallback callback,
			int width, int height) {
		this.callback = callback;
		this.photox = width;
		this.photoy = height;
		try {
			// Launch picker to choose photo for selected contact
//			final Intent intent = getPhotoPickIntent();
			//no corp
			final Intent intent=getPhotoPickIntentNoCorp();
			mActivity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onPickFailed();
			}
		}
	}

	/**
	 * Constructs an intent for picking a photo from Gallery, cropping it and
	 * returning the bitmap.
	 */
	private final Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", photox);
		intent.putExtra("aspectY", photoy);
		intent.putExtra("outputX", photox);
		intent.putExtra("outputY", photoy);
		intent.putExtra("return-data", true);
		return intent;
	}
	
	/**
	 * Constructs an intent for picking a photo from Gallery, cropping it and
	 * returning the bitmap.
	 */
	private final Intent getPhotoPickIntentNoCorp() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}

	/**
	 * Create a file name for the icon photo using current time.
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss", Locale.getDefault());
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * Constructs an intent for capturing a photo and storing it in a temporary
	 * file.
	 */
	private Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * ����ͼƬ������ֵ
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private  int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // Raw height and width of image  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
  
        if (height > reqHeight || width > reqWidth) {  
  
            // Calculate ratios of height and width to requested height and  
            // width  
            final int heightRatio = Math.round((float) height  
                    / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
  
            // Choose the smallest ratio as inSampleSize value, this will  
            // guarantee  
            // a final image with both dimensions larger than or equal to the  
            // requested height and width.  
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;  
        }  
  
        return inSampleSize;  
    }  
	
	
	/**
	 * ����ͼƬ��ת   
	 * @param path
	 * @return
	 */
	private  int readPictureDegree(String path) {    
        int degree  = 0;    
        try {    
                ExifInterface exifInterface = new ExifInterface(path);    
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);    
                switch (orientation) {    
                case ExifInterface.ORIENTATION_ROTATE_90:    
                        degree = 90;    
                        break;    
                case ExifInterface.ORIENTATION_ROTATE_180:    
                        degree = 180;    
                        break;    
                case ExifInterface.ORIENTATION_ROTATE_270:    
                        degree = 270;    
                        break;    
                }    
        } catch (IOException e) {    
                e.printStackTrace();    
        }    
        return degree;    
    } 
	
	
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){  
        if(bitmap == null)  
            return null ;  
          
        int w = bitmap.getWidth();  
        int h = bitmap.getHeight();  
  
        // Setting post rotate to 90  
        Matrix mtx = new Matrix();  
        mtx.postRotate(rotate);  
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);  
    } 
	
	
	/**
	 * ѹ���ļ�
	 * @param filePath
	 * @return
	 */
	private Bitmap getSmallBitmap(String filePath) {  
        
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(filePath, options);  
  
        // Calculate inSampleSize  
        options.inSampleSize = calculateInSampleSize(options, DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);  
  
        // Decode bitmap with inSampleSize set  
        options.inJustDecodeBounds = false;  
          
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);  
        if(bm == null){  
            return  null;  
        }  
        int degree = readPictureDegree(filePath);  
        bm = rotateBitmap(bm,degree) ;  
        ByteArrayOutputStream baos = null ;  
        try{  
            baos = new ByteArrayOutputStream();  
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);  
              
        }finally{  
            try {  
                if(baos != null)  
                    baos.close() ;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return bm ;  
  
    }
	
	
	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = mActivity.managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			return cursor.getString(column_index);
		} else {
			// ����α�Ϊ��˵����ȡ���Ѿ��Ǿ��·����
			return uri.getPath();
		}
	}

}
