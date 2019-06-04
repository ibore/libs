package me.ibore.libs.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public final class PhotoUtils {

    private PhotoUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void captureCamera(@NonNull FragmentActivity activity, @NonNull final PhotoCallBack callback) {
        final DelegateFragment delegate = findDelegate(activity);
        if (delegate != null) {
            delegate.setAttachCallBack(new AttachCallBack() {
                @Override
                public void onAttach() {
                    delegate.captureCamera(callback);
                }
            });
        }
    }

    public static void captureCamera(@NonNull Fragment fragment, @NonNull final PhotoCallBack callback) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isFinishing()) {
            final DelegateFragment delegate = findDelegate(activity);
            if (delegate != null) {
                delegate.setAttachCallBack(new AttachCallBack() {
                    @Override
                    public void onAttach() {
                        delegate.captureCamera(callback);
                    }
                });
            }
        }
    }

    public static void captureCameraForSquare(@NonNull FragmentActivity activity, @NonNull final PhotoCallBack callback) {
        final DelegateFragment delegate = findDelegate(activity);
        if (delegate != null) {
            delegate.setAttachCallBack(new AttachCallBack() {
                @Override
                public void onAttach() {
                    delegate.captureCameraForSquare(callback);
                }
            });
        }
    }

    public static void captureCameraForSquare(@NonNull Fragment fragment, @NonNull final PhotoCallBack callback) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isFinishing()) {
            final DelegateFragment delegate = findDelegate(activity);
            if (delegate != null) {
                delegate.setAttachCallBack(new AttachCallBack() {
                    @Override
                    public void onAttach() {
                        delegate.captureCameraForSquare(callback);
                    }
                });
            }
        }
    }

    public static void captureCameraForCrop(@NonNull FragmentActivity activity, @NonNull final PhotoCallBack callback, final int aspectX, final int aspectY) {
        final DelegateFragment delegate = findDelegate(activity);
        if (delegate != null) {
            delegate.setAttachCallBack(new AttachCallBack() {
                @Override
                public void onAttach() {
                    delegate.captureCameraForCrop(callback, true, aspectX, aspectY);
                }
            });
        }
    }

    public static void captureCameraForCrop(@NonNull Fragment fragment, @NonNull final PhotoCallBack callback, final int aspectX, final int aspectY) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isFinishing()) {
            final DelegateFragment delegate = findDelegate(activity);
            if (delegate != null) {
                delegate.setAttachCallBack(new AttachCallBack() {
                    @Override
                    public void onAttach() {
                        delegate.captureCameraForCrop(callback, true, aspectX, aspectY);
                    }
                });
            }
        }
    }

    public static void captureGallery(@NonNull FragmentActivity activity, @NonNull final PhotoCallBack callback) {
        final DelegateFragment delegate = findDelegate(activity);
        if (delegate != null) {
            delegate.setAttachCallBack(new AttachCallBack() {
                @Override
                public void onAttach() {
                    delegate.captureGallery(callback);
                }
            });
        }
    }

    public static void captureGallery(@NonNull Fragment fragment, @NonNull final PhotoCallBack callback) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isFinishing()) {
            final DelegateFragment delegate = findDelegate(activity);
            if (delegate != null) {
                delegate.setAttachCallBack(new AttachCallBack() {
                    @Override
                    public void onAttach() {
                        delegate.captureGallery(callback);
                    }
                });
            }
        }
    }

    public static void captureGalleryForSquare(@NonNull FragmentActivity activity, @NonNull final PhotoCallBack callback) {
        final DelegateFragment delegate = findDelegate(activity);
        if (delegate != null) {
            delegate.setAttachCallBack(new AttachCallBack() {
                @Override
                public void onAttach() {
                    delegate.captureGalleryForSquare(callback);
                }
            });
        }
    }

    public static void captureGalleryForSquare(@NonNull Fragment fragment, @NonNull final PhotoCallBack callback) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isFinishing()) {
            final DelegateFragment delegate = findDelegate(activity);
            if (delegate != null) {
                delegate.setAttachCallBack(new AttachCallBack() {
                    @Override
                    public void onAttach() {
                        delegate.captureGalleryForSquare(callback);
                    }
                });
            }
        }
    }

    public static void captureGalleryForCorp(@NonNull FragmentActivity activity, @NonNull final PhotoCallBack callback, final int aspectX, final int aspectY) {
        final DelegateFragment delegate = findDelegate(activity);
        if (delegate != null) {
            delegate.setAttachCallBack(new AttachCallBack() {
                @Override
                public void onAttach() {
                    delegate.captureGalleryForCorp(callback, true, aspectX, aspectY);
                }
            });
        }
    }

    public static void captureGalleryForCorp(@NonNull Fragment fragment, @NonNull final PhotoCallBack callback, final int aspectX, final int aspectY) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isFinishing()) {
            final DelegateFragment delegate = findDelegate(activity);
            if (delegate != null) {
                delegate.setAttachCallBack(new AttachCallBack() {
                    @Override
                    public void onAttach() {
                        delegate.captureGalleryForCorp(callback, true, aspectX, aspectY);
                    }
                });
            }
        }
    }

    private static DelegateFragment findDelegate(FragmentActivity activity) {
        DelegateFragment fragment = null;
        if (activity != null && !activity.isFinishing()) {
            FragmentManager fm = activity.getSupportFragmentManager();
            fragment = (DelegateFragment) fm.findFragmentByTag(DelegateFragment.class.getSimpleName());
            if (fragment == null) {
                fragment = DelegateFragment.newInstance();
                fm.beginTransaction()
                        .add(fragment, DelegateFragment.class.getSimpleName())
                        .commitAllowingStateLoss();
            }
        }
        return fragment;
    }

    public interface PhotoCallBack {
        void onSuccess(File file);
    }

    private interface AttachCallBack {
        void onAttach();
    }

    public static class DelegateFragment extends Fragment {
        private final int REQUEST_CAPTURE_CAMERA = 0;
        private final int REQUEST_CAPTURE_GALLERY = 1;
        private final int REQUEST_CAPTURE_CAMERA_AND_CROP = 2;
        private final int REQUEST_CAPTURE_GALLERY_AND_CROP = 3;
        private final int REQUEST_CAPTURE_CROP = 4;

        private final int NOT_CORP = -1;

        private File captureCameraFile;
        private File cropFile;

        private int aspectX;
        private int aspectY;

        private Context mContext;

        private AttachCallBack attachCallBack;
        private PhotoCallBack photoCallBack;

        static DelegateFragment newInstance() {
            Bundle args = new Bundle();
            DelegateFragment fragment = new DelegateFragment();
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * 有可能拍照会在 onAttach 之前所以需要确保在 onAttach 后执行
         *
         * @param attachCallBack
         */
        void setAttachCallBack(AttachCallBack attachCallBack) {
            photoCallBack = null;
            this.attachCallBack = attachCallBack;
            if (isAdded()) {
                attachCallBack.onAttach();
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = context;
            if (attachCallBack != null) {
                attachCallBack.onAttach();
            }
        }

        void captureCamera(PhotoCallBack callBack) {
            captureCameraForCrop(callBack, false, NOT_CORP, NOT_CORP);
        }

        void captureCameraForSquare(PhotoCallBack callBack) {
            captureCameraForCrop(callBack, true, 1, 1);
        }

        void captureCameraForCrop(PhotoCallBack callBack, boolean isCrop, int aspectX, int aspectY) {
            photoCallBack = callBack;
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            captureCameraFile = new File(mContext.getExternalCacheDir(), "captureCameraTemp.jpeg");
            Intent captureCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri captureUri = UriUtils.file2Uri(captureCameraFile);
            captureCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureUri);
            startActivityForResult(captureCameraIntent, isCrop ? REQUEST_CAPTURE_CAMERA_AND_CROP : REQUEST_CAPTURE_CAMERA);
        }

        void captureGallery(PhotoCallBack callBack) {
            captureGalleryForCorp(callBack, false, NOT_CORP, NOT_CORP);
        }

        void captureGalleryForSquare(PhotoCallBack callBack) {
            captureGalleryForCorp(callBack, true, 1, 1);
        }

        void captureGalleryForCorp(PhotoCallBack callBack, boolean isCrop, int aspectX, int aspectY) {
            photoCallBack = callBack;
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, isCrop ? REQUEST_CAPTURE_GALLERY_AND_CROP : REQUEST_CAPTURE_GALLERY);
        }

        void cropPicture(Uri uri) {
            cropFile = new File(mContext.getExternalCacheDir(), "cropTemp.jpeg");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            if (aspectX > 0 && aspectY > 0) {
                intent.putExtra("aspectX", aspectX);
                intent.putExtra("aspectY", aspectY);
                aspectX = 0;
                aspectY = 0;
            }
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, REQUEST_CAPTURE_CROP);
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (requestCode == REQUEST_CAPTURE_CAMERA) {
                if (photoCallBack != null) {
                    // TODO 未完成
                    //ImageUtils.correctImage(captureCameraFile.getAbsolutePath());
                    photoCallBack.onSuccess(captureCameraFile);
                }
            } else if (requestCode == REQUEST_CAPTURE_GALLERY) {
                File file = UriUtils.uri2File(data.getData());
                if (photoCallBack != null) {
                    // TODO 未完成
                    //ImageUtils.correctImage(file.getAbsolutePath());
                    photoCallBack.onSuccess(file);
                }
            } else if (requestCode == REQUEST_CAPTURE_CAMERA_AND_CROP) {
                cropPicture(UriUtils.file2Uri(captureCameraFile));
            } else if (requestCode == REQUEST_CAPTURE_GALLERY_AND_CROP) {
                cropPicture(data.getData());
            } else if (requestCode == REQUEST_CAPTURE_CROP) {
                if (photoCallBack != null) {
                    photoCallBack.onSuccess(cropFile);
                }
            }
        }
    }
}
