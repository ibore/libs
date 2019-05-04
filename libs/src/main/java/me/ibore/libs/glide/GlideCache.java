package me.ibore.libs.glide;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class GlideCache {

    private static final LruCache<Key, String> loadIdToSafeHash = new LruCache<Key, String>(1000);

    public static String getCacheKey(Key key) {
        String safeKey;
        synchronized (loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(key);
        }
        if (safeKey == null) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                key.updateDiskCacheKey(messageDigest);
                safeKey = Util.sha256BytesToHex(messageDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            synchronized (loadIdToSafeHash) {
                loadIdToSafeHash.put(key, safeKey);
            }
        }
        return safeKey;
    }


    public static class CacheKey implements Key {
        private GlideUrl sourceKey;
        private EmptySignature signature;

        public CacheKey(GlideUrl sourceKey, EmptySignature signature) {
            this.sourceKey = sourceKey;
            this.signature = signature;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            sourceKey.updateDiskCacheKey(messageDigest);
            signature.updateDiskCacheKey(messageDigest);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CacheKey) {
                CacheKey other = (CacheKey) o;
                return sourceKey == other.sourceKey && signature == other.signature;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = sourceKey.hashCode();
            result = 31 * result + signature.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return ("DataCacheKey{"
                    + "sourceKey=" + sourceKey
                    + ", signature=" + signature
                    + '}');
        }
    }
}
