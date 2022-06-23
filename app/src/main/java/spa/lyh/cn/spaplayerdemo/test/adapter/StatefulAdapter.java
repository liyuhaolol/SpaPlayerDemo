package spa.lyh.cn.spaplayerdemo.test.adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;

public interface StatefulAdapter {
    /** Saves adapter state */
    @NonNull
    Parcelable saveState();

    /** Restores adapter state */
    void restoreState(@NonNull Parcelable savedState);
}
