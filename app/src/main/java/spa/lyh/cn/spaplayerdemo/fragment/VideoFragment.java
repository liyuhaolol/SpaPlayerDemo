package spa.lyh.cn.spaplayerdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayer.SpaPlayer;

public class VideoFragment extends Fragment {
    SpaPlayer player;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        player = view.findViewById(R.id.spaplayer);
        player.setUp(Global.url,"聪明的小学神");
        ImageLoadUtil.displayImage(getActivity(),Global.pic,player.posterImageView);
    }
}
